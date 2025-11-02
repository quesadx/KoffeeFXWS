/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.koffeefxws.service;

import cr.ac.una.koffeefxws.model.AppUserDTO;
import cr.ac.una.koffeefxws.model.CashOpeningDTO;
import cr.ac.una.koffeefxws.model.CustomerOrderDTO;
import cr.ac.una.koffeefxws.model.InvoiceDTO;
import cr.ac.una.koffeefxws.model.OrderItemDTO;
import cr.ac.una.koffeefxws.model.ProductDTO;
import cr.ac.una.koffeefxws.model.ProductSalesDTO;
import cr.ac.una.koffeefxws.model.SystemParameterDTO;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;
import jakarta.ejb.EJB;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author krist
 */
@Stateless
@LocalBean
public class ReportService {

    private static final Logger LOG = Logger.getLogger(
        ReportService.class.getName()
    );

    @EJB
    CustomerOrderService orderService;

    @EJB
    SystemParameterService paramService;

    @EJB
    CashOpeningService cashOpeningService;

    @EJB
    InvoiceService invoiceService;

    @EJB
    AppUserService appUserService;

    @EJB
    ProductService productService;

    /**
     * Prepara los parámetros y el datasource para generar el reporte
     *
     * @param orderId ID de la orden
     * @return Respuesta con JasperPrint listo para exportar
     */
    private Respuesta prepararReporte(Long orderId) {
        try {
            // 1. Obtener CustomerOrderDTO por orderId
            Respuesta orderResponse = orderService.getCustomerOrder(orderId);
            if (!orderResponse.getEstado()) {
                return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No se encontró la orden con ID: " + orderId,
                    "prepararReporte " + orderResponse.getMensaje()
                );
            }

            CustomerOrderDTO order =
                (CustomerOrderDTO) orderResponse.getResultado("CustomerOrder");

            // 2. Extraer InvoiceDTO de la orden
            InvoiceDTO invoice = order.getInvoice();
            if (invoice == null) {
                return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_NOENCONTRADO,
                    "La orden no tiene una factura asociada.",
                    "prepararReporte NoInvoice"
                );
            }

            // 3. Crear JRBeanCollectionDataSource con orden.getOrderItems()
            List<OrderItemDTO> orderItems = order.getOrderItems();
            if (orderItems == null || orderItems.isEmpty()) {
                return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_NOENCONTRADO,
                    "La orden no tiene items.",
                    "prepararReporte NoItems"
                );
            }
            JRBeanCollectionDataSource dataSource =
                new JRBeanCollectionDataSource(orderItems);

            // 4. Obtener parámetros del sistema y convertir a Map
            Respuesta paramsResponse = paramService.getSystemParameters();
            Map<String, String> paramsMap = new HashMap<>();

            if (paramsResponse.getEstado()) {
                @SuppressWarnings("unchecked")
                List<SystemParameterDTO> systemParams = (List<
                    SystemParameterDTO
                >) paramsResponse.getResultado("SystemParameters");
                for (SystemParameterDTO param : systemParams) {
                    paramsMap.put(param.getParamName(), param.getParamValue());
                }
            }

            // 5. Preparar Map de parámetros para el reporte
            Map<String, Object> parameters = new HashMap<>();

            // Parámetros de la factura - Información básica
            parameters.put(
                "invoiceNumber",
                invoice.getInvoiceNumber() != null
                    ? invoice.getInvoiceNumber()
                    : ""
            );
            parameters.put("invoiceDate", invoice.getCreatedAt()); // LocalDate directamente
            parameters.put(
                "customerName",
                order.getCustomerName() != null
                    ? order.getCustomerName()
                    : "Cliente General"
            );
            parameters.put(
                "paymentMethod",
                invoice.getPaymentMethod() != null
                    ? invoice.getPaymentMethod()
                    : "CASH"
            );

            // Montos calculados (NO tasas)
            parameters.put(
                "subtotal",
                invoice.getSubtotal() != null ? invoice.getSubtotal() : 0.0
            );
            parameters.put(
                "taxAmount",
                invoice.getTaxAmount() != null ? invoice.getTaxAmount() : 0.0
            );
            parameters.put(
                "serviceAmount",
                invoice.getServiceAmount() != null
                    ? invoice.getServiceAmount()
                    : 0.0
            );
            parameters.put(
                "discountAmount",
                invoice.getDiscountAmount() != null
                    ? invoice.getDiscountAmount()
                    : 0.0
            );
            parameters.put(
                "total",
                invoice.getTotal() != null ? invoice.getTotal() : 0.0
            );

            // Información de pago
            parameters.put(
                "amountReceived",
                invoice.getAmountReceived() != null
                    ? invoice.getAmountReceived()
                    : 0.0
            );
            parameters.put(
                "changeAmount",
                invoice.getChangeAmount() != null
                    ? invoice.getChangeAmount()
                    : 0.0
            );

            // Tasas (por si se necesitan en el reporte para mostrar porcentajes)
            parameters.put(
                "taxRate",
                invoice.getTaxRate() != null ? invoice.getTaxRate() : 0.0
            );
            parameters.put(
                "serviceRate",
                invoice.getServiceRate() != null
                    ? invoice.getServiceRate()
                    : 0.0
            );
            parameters.put(
                "discountRate",
                invoice.getDiscountRate() != null
                    ? invoice.getDiscountRate()
                    : 0.0
            );

            // Parámetros de la compañía desde SystemParameters
            parameters.put(
                "companyName",
                paramsMap.getOrDefault("company.name", "KoffeeFX")
            );
            parameters.put(
                "companyAddress",
                paramsMap.getOrDefault("company.address", "")
            );
            parameters.put(
                "companyPhoneNumber",
                paramsMap.getOrDefault("company.phone", "")
            );
            parameters.put(
                "companyEmail",
                paramsMap.getOrDefault("company.email", "")
            );

            // 6. Compilar el informe desde /reports/Invoice.jrxml
            InputStream reportStream = getClass().getResourceAsStream(
                "/reports/Invoice.jrxml"
            );
            if (reportStream == null) {
                return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_INTERNO,
                    "No se encontró el archivo de reporte Invoice.jrxml",
                    "prepararReporte FileNotFound"
                );
            }

            LOG.log(
                Level.INFO,
                "Compilando reporte Invoice.jrxml con lenguaje Java (JDT compiler)"
            );
            JasperReport jasperReport = JasperCompileManager.compileReport(
                reportStream
            );

            // 7. Llenar el reporte con los parámetros y el dataSource
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                parameters,
                dataSource
            );

            return new Respuesta(
                true,
                CodigoRespuesta.CORRECTO,
                "",
                "",
                "JasperPrint",
                jasperPrint
            );
        } catch (Exception ex) {
            LOG.log(
                Level.SEVERE,
                "Ocurrió un error al preparar el reporte.",
                ex
            );
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_INTERNO,
                "Ocurrió un error al preparar el reporte.",
                "prepararReporte " + ex.getMessage()
            );
        }
    }

    /**
     * Genera una factura en PDF usando JasperReports
     *
     * @param orderId ID de la orden para generar la factura
     * @param outputPath Ruta donde se guardará el archivo PDF
     * @return Respuesta con el resultado de la operación
     */
    public Respuesta generarFacturaPDF(Long orderId, String outputPath) {
        try {
            Respuesta reporteResponse = prepararReporte(orderId);
            if (!reporteResponse.getEstado()) {
                return reporteResponse;
            }

            JasperPrint jasperPrint =
                (JasperPrint) reporteResponse.getResultado("JasperPrint");

            // Exportar a PDF en outputPath
            try (
                FileOutputStream outputStream = new FileOutputStream(outputPath)
            ) {
                JasperExportManager.exportReportToPdfStream(
                    jasperPrint,
                    outputStream
                );
            }

            LOG.log(
                Level.INFO,
                "Factura PDF generada exitosamente en: {0}",
                outputPath
            );
            return new Respuesta(
                true,
                CodigoRespuesta.CORRECTO,
                "Factura generada exitosamente.",
                "",
                "OutputPath",
                outputPath
            );
        } catch (Exception ex) {
            LOG.log(
                Level.SEVERE,
                "Ocurrió un error al generar la factura PDF.",
                ex
            );
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_INTERNO,
                "Ocurrió un error al generar la factura PDF.",
                "generarFacturaPDF " + ex.getMessage()
            );
        }
    }

    /**
     * Genera una factura en PDF y retorna el array de bytes
     *
     * @param orderId ID de la orden para generar la factura
     * @return Respuesta con el array de bytes del PDF
     */
    public Respuesta generarFacturaPDFBytes(Long orderId) {
        try {
            Respuesta reporteResponse = prepararReporte(orderId);
            if (!reporteResponse.getEstado()) {
                return reporteResponse;
            }

            JasperPrint jasperPrint =
                (JasperPrint) reporteResponse.getResultado("JasperPrint");

            // Exportar a bytes
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(
                jasperPrint
            );

            LOG.log(Level.INFO, "Factura PDF generada exitosamente como bytes");
            return new Respuesta(
                true,
                CodigoRespuesta.CORRECTO,
                "Factura generada exitosamente.",
                "",
                "PDFBytes",
                pdfBytes
            );
        } catch (Exception ex) {
            LOG.log(
                Level.SEVERE,
                "Ocurrió un error al generar la factura PDF.",
                ex
            );
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_INTERNO,
                "Ocurrió un error al generar la factura PDF.",
                "generarFacturaPDFBytes " + ex.getMessage()
            );
        }
    }

    /**
     * Prepara los parámetros y el datasource para generar el reporte de cierre de caja
     *
     * @param cashOpeningId ID de la apertura de caja
     * @return Respuesta con JasperPrint listo para exportar
     */
    private Respuesta prepararReporteCashierClosing(Long cashOpeningId) {
        try {
            // 1. Obtener CashOpening por ID
            Respuesta cashOpeningResponse = cashOpeningService.getCashOpening(
                cashOpeningId
            );
            if (!cashOpeningResponse.getEstado()) {
                return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No se encontró la apertura de caja con ID: " + cashOpeningId,
                    "prepararReporteCashierClosing " + cashOpeningResponse.getMensaje()
                );
            }

            // 2. Obtener todas las facturas del servicio y filtrar por userId
            Respuesta invoicesResponse = invoiceService.getInvoices();
            if (!invoicesResponse.getEstado()) {
                return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No se encontraron facturas.",
                    "prepararReporteCashierClosing " + invoicesResponse.getMensaje()
                );
            }

            @SuppressWarnings("unchecked")
            List<InvoiceDTO> allInvoices = (List<InvoiceDTO>) invoicesResponse.getResultado(
                "Invoices"
            );

            if (allInvoices == null || allInvoices.isEmpty()) {
                return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No existen facturas disponibles.",
                    "prepararReporteCashierClosing NoInvoices"
                );
            }

            // Log para validar que customerName esté siendo populado
            for (InvoiceDTO invoice : allInvoices) {
                LOG.log(
                    Level.INFO,
                    "Invoice: " +
                        invoice.getInvoiceNumber() +
                        " | Customer: " +
                        (invoice.getCustomerName() != null
                            ? invoice.getCustomerName()
                            : "NULL") +
                        " | Total: " +
                        invoice.getTotal()
                );
            }

            // 3. Crear JRBeanCollectionDataSource con las facturas
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
                allInvoices
            );

            // 4. Obtener parámetros del sistema
            Respuesta paramsResponse = paramService.getSystemParameters();
            Map<String, String> paramsMap = new HashMap<>();

            if (paramsResponse.getEstado()) {
                @SuppressWarnings("unchecked")
                List<SystemParameterDTO> systemParams = (List<
                    SystemParameterDTO
                >) paramsResponse.getResultado("SystemParameters");
                for (SystemParameterDTO param : systemParams) {
                    paramsMap.put(param.getParamName(), param.getParamValue());
                }
            }

            // 5. Preparar Map de parámetros para el reporte de cierre de caja
            Map<String, Object> parameters = new HashMap<>();

            // Obtener datos de la apertura de caja
            CashOpeningDTO cashOpening = (CashOpeningDTO) cashOpeningResponse.getResultado(
                "CashOpening"
            );

            // Parámetros de identificación del cajero y fecha
            // Obtener nombre completo del usuario (firstName + lastName)
            String cashierName = "N/A";
            if (cashOpening.getUserId() != null) {
                Respuesta userResponse = appUserService.getAppUser(cashOpening.getUserId());
                if (userResponse.getEstado()) {
                    AppUserDTO user = (AppUserDTO) userResponse.getResultado("AppUser");
                    if (user != null && user.getFirstName() != null && user.getLastName() != null) {
                        cashierName = user.getFirstName() + " " + user.getLastName();
                    }
                }
            }
            parameters.put("cashierName", cashierName);
            parameters.put(
                "closingDate",
                cashOpening.getClosingDate() != null
                    ? cashOpening.getClosingDate()
                    : java.time.LocalDate.now()
            );

            // Parámetros financieros (montos)
            // Calcular suma total de todas las facturas
            Double systemGrandTotal = 0.0;
            for (InvoiceDTO invoice : allInvoices) {
                if (invoice.getTotal() != null) {
                    systemGrandTotal += invoice.getTotal().doubleValue();
                }
            }

            // cashierDeclaredTotal: lo que el cajero contó (closingAmount de la apertura de caja)
            Double cashierDeclaredTotal = cashOpening.getClosingAmount() != null
                ? cashOpening.getClosingAmount().doubleValue()
                : 0.0;

            // totalDifference: systemGrandTotal - cashierDeclaredTotal
            Double totalDifference = systemGrandTotal - cashierDeclaredTotal;

            // differenceStatus: "Correcto" si es 0, "Sobrante" si es positivo (cajero contó más), "Faltante" si es negativo (cajero contó menos)
            String differenceStatus = "Pendiente";
            if (totalDifference == 0.0) {
                differenceStatus = "Correcto";
            } else if (totalDifference > 0) {
                differenceStatus = "Faltante";
            } else if (totalDifference < 0) {
                differenceStatus = "Sobrante";
            }

            parameters.put("systemGrandTotal", systemGrandTotal);
            parameters.put("cashierDeclaredTotal", cashierDeclaredTotal);
            parameters.put("totalDifference", Math.abs(totalDifference));
            parameters.put("differenceStatus", differenceStatus);

            // Parámetros de la compañía
            parameters.put(
                "companyName",
                paramsMap.getOrDefault("company.name", "KoffeeFX")
            );

            // 6. Compilar el informe desde /reports/Cashier-Report.jrxml
            InputStream reportStream = getClass().getResourceAsStream(
                "/reports/Cashier-Report.jrxml"
            );
            if (reportStream == null) {
                return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_INTERNO,
                    "No se encontró el archivo de reporte Cashier-Report.jrxml",
                    "prepararReporteCashierClosing FileNotFound"
                );
            }

            LOG.log(
                Level.INFO,
                "Compilando reporte Cashier-Report.jrxml con lenguaje Java (JDT compiler)"
            );
            JasperReport jasperReport = JasperCompileManager.compileReport(
                reportStream
            );

            // 7. Llenar el reporte con los parámetros y el dataSource
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                parameters,
                dataSource
            );

            return new Respuesta(
                true,
                CodigoRespuesta.CORRECTO,
                "",
                "",
                "JasperPrint",
                jasperPrint
            );
        } catch (Exception ex) {
            LOG.log(
                Level.SEVERE,
                "Ocurrió un error al preparar el reporte de cierre de caja.",
                ex
            );
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_INTERNO,
                "Ocurrió un error al preparar el reporte de cierre de caja.",
                "prepararReporteCashierClosing " + ex.getMessage()
            );
        }
    }

    /**
     * Genera un reporte de cierre de caja en PDF y retorna el array de bytes
     *
     * @param cashOpeningId ID de la apertura de caja para generar el reporte
     * @return Respuesta con el array de bytes del PDF
     */
    public Respuesta generarReporteCashierPDFBytes(Long cashOpeningId) {
        try {
            Respuesta reporteResponse = prepararReporteCashierClosing(
                cashOpeningId
            );
            if (!reporteResponse.getEstado()) {
                return reporteResponse;
            }

            JasperPrint jasperPrint =
                (JasperPrint) reporteResponse.getResultado("JasperPrint");

            // Exportar a bytes
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(
                jasperPrint
            );

            LOG.log(
                Level.INFO,
                "Reporte de cierre de caja PDF generado exitosamente como bytes"
            );
            return new Respuesta(
                true,
                CodigoRespuesta.CORRECTO,
                "Reporte generado exitosamente.",
                "",
                "PDFBytes",
                pdfBytes
            );
        } catch (Exception ex) {
            LOG.log(
                Level.SEVERE,
                "Ocurrió un error al generar el reporte de cierre de caja PDF.",
                ex
            );
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_INTERNO,
                "Ocurrió un error al generar el reporte de cierre de caja PDF.",
                "generarReporteCashierPDFBytes " + ex.getMessage()
            );
        }
    }

    /**
     * Prepara los parámetros y el datasource para generar el reporte de facturas por fecha
     *
     * @param dateFrom Fecha inicial del período
     * @param dateTo Fecha final del período
     * @return Respuesta con JasperPrint listo para exportar
     */
    private Respuesta prepararReporteInvoicesByDate(LocalDate dateFrom, LocalDate dateTo) {
        try {
            // 1. Validar parámetros
            if (dateFrom == null || dateTo == null) {
                return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_CLIENTE,
                    "Las fechas de inicio y fin son obligatorias",
                    "prepararReporteInvoicesByDate NullDates"
                );
            }

            if (dateFrom.isAfter(dateTo)) {
                return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_CLIENTE,
                    "La fecha de inicio no puede ser posterior a la fecha de fin",
                    "prepararReporteInvoicesByDate InvalidDates"
                );
            }

            // 2. Obtener todas las facturas
            Respuesta invoicesResponse = invoiceService.getInvoices();
            if (!invoicesResponse.getEstado()) {
                return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No se encontraron facturas.",
                    "prepararReporteInvoicesByDate " + invoicesResponse.getMensaje()
                );
            }

            @SuppressWarnings("unchecked")
            List<InvoiceDTO> allInvoices = (List<InvoiceDTO>) invoicesResponse.getResultado(
                "Invoices"
            );

            // 3. Filtrar facturas por rango de fechas
            List<InvoiceDTO> invoicesByDateRange = new ArrayList<>();
            if (allInvoices != null) {
                for (InvoiceDTO invoice : allInvoices) {
                    if (
                        invoice.getCreatedAt() != null &&
                        !invoice.getCreatedAt().isBefore(dateFrom) &&
                        !invoice.getCreatedAt().isAfter(dateTo)
                    ) {
                        invoicesByDateRange.add(invoice);
                    }
                }
            }

            if (invoicesByDateRange.isEmpty()) {
                return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No existen facturas en el período especificado.",
                    "prepararReporteInvoicesByDate NoInvoicesInRange"
                );
            }

            // 4. Crear JRBeanCollectionDataSource con las facturas filtradas
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
                invoicesByDateRange
            );

            // 5. Obtener parámetros del sistema
            Respuesta paramsResponse = paramService.getSystemParameters();
            Map<String, String> paramsMap = new HashMap<>();

            if (paramsResponse.getEstado()) {
                @SuppressWarnings("unchecked")
                List<SystemParameterDTO> systemParams = (List<
                    SystemParameterDTO
                >) paramsResponse.getResultado("SystemParameters");
                for (SystemParameterDTO param : systemParams) {
                    paramsMap.put(param.getParamName(), param.getParamValue());
                }
            }

            // 6. Calcular totales para el resumen
            Double totalAmount = 0.0;
            Double totalSubtotal = 0.0;
            Double totalTaxAmount = 0.0;
            Double totalServiceAmount = 0.0;
            Double totalDiscountAmount = 0.0;
            Integer totalRecords = invoicesByDateRange.size();

            for (InvoiceDTO invoice : invoicesByDateRange) {
                if (invoice.getTotal() != null) {
                    totalAmount += invoice.getTotal();
                }
                if (invoice.getSubtotal() != null) {
                    totalSubtotal += invoice.getSubtotal();
                }
                if (invoice.getTaxAmount() != null) {
                    totalTaxAmount += invoice.getTaxAmount();
                }
                if (invoice.getServiceAmount() != null) {
                    totalServiceAmount += invoice.getServiceAmount();
                }
                if (invoice.getDiscountAmount() != null) {
                    totalDiscountAmount += invoice.getDiscountAmount();
                }
            }

            Double averageTicket = totalRecords > 0 ? totalAmount / totalRecords : 0.0;

            // 7. Preparar Map de parámetros para el reporte
            Map<String, Object> parameters = new HashMap<>();

            // Parámetros de fechas
            parameters.put("dateFrom", dateFrom);
            parameters.put("dateTo", dateTo);

            // Parámetros de la compañía
            parameters.put(
                "companyName",
                paramsMap.getOrDefault("company.name", "KoffeeFX")
            );
            parameters.put(
                "restaurantAddress",
                paramsMap.getOrDefault("company.address", "")
            );
            parameters.put(
                "restaurantPhone",
                paramsMap.getOrDefault("company.phone", "")
            );

            // Moneda
            parameters.put("currency", "₡");

            // Parámetros de totales
            parameters.put("totalRecords", totalRecords);
            parameters.put("totalAmount", totalAmount);
            parameters.put("totalSubtotal", totalSubtotal);
            parameters.put("totalTaxAmount", totalTaxAmount);
            parameters.put("totalServiceAmount", totalServiceAmount);
            parameters.put("totalDiscountAmount", totalDiscountAmount);
            parameters.put("averageTicket", averageTicket);

            // Parámetros de generación
            parameters.put("reportGeneratedAt", LocalDate.now());
            parameters.put("generatedByUser", System.getProperty("user.name", "System"));

            // 8. Compilar el informe desde /reports/InvoicesByDate.jrxml
            InputStream reportStream = getClass().getResourceAsStream(
                "/reports/InvoicesByDate.jrxml"
            );
            if (reportStream == null) {
                return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_INTERNO,
                    "No se encontró el archivo de reporte InvoicesByDate.jrxml",
                    "prepararReporteInvoicesByDate FileNotFound"
                );
            }

            LOG.log(
                Level.INFO,
                "Compilando reporte InvoicesByDate.jrxml con lenguaje Java (JDT compiler)"
            );
            JasperReport jasperReport = JasperCompileManager.compileReport(
                reportStream
            );

            // 9. Llenar el reporte con los parámetros y el dataSource
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                parameters,
                dataSource
            );

            return new Respuesta(
                true,
                CodigoRespuesta.CORRECTO,
                "",
                "",
                "JasperPrint",
                jasperPrint
            );
        } catch (Exception ex) {
            LOG.log(
                Level.SEVERE,
                "Ocurrió un error al preparar el reporte de facturas por fecha.",
                ex
            );
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_INTERNO,
                "Ocurrió un error al preparar el reporte de facturas por fecha.",
                "prepararReporteInvoicesByDate " + ex.getMessage()
            );
        }
    }

    /**
     * Genera un reporte de facturas por fecha en PDF y retorna el array de bytes
     *
     * @param dateFrom Fecha inicial del período
     * @param dateTo Fecha final del período
     * @return Respuesta con el array de bytes del PDF
     */
    public Respuesta generarReporteInvoicesByDatePDFBytes(LocalDate dateFrom, LocalDate dateTo) {
        try {
            Respuesta reporteResponse = prepararReporteInvoicesByDate(dateFrom, dateTo);
            if (!reporteResponse.getEstado()) {
                return reporteResponse;
            }

            JasperPrint jasperPrint =
                (JasperPrint) reporteResponse.getResultado("JasperPrint");

            // Exportar a bytes
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

            LOG.log(
                Level.INFO,
                "Reporte de facturas por fecha PDF generado exitosamente como bytes"
            );
            return new Respuesta(
                true,
                CodigoRespuesta.CORRECTO,
                "Reporte generado exitosamente.",
                "",
                "PDFBytes",
                pdfBytes
            );
        } catch (Exception ex) {
            LOG.log(
                Level.SEVERE,
                "Ocurrió un error al generar el reporte de facturas por fecha PDF.",
                ex
            );
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_INTERNO,
                "Ocurrió un error al generar el reporte de facturas por fecha PDF.",
                "generarReporteInvoicesByDatePDFBytes " + ex.getMessage()
            );
        }
    }

    /**
     * Prepara los parámetros y el datasource para generar el reporte de productos más vendidos
     *
     * @param dateFrom Fecha inicial del período
     * @param dateTo Fecha final del período
     * @return Respuesta con JasperPrint listo para exportar
     */
    private Respuesta prepararReporteProductosMasVendidos(LocalDate dateFrom, LocalDate dateTo) {
        try {
            // 1. Validar parámetros
            if (dateFrom == null || dateTo == null) {
                return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_CLIENTE,
                    "Las fechas de inicio y fin son obligatorias",
                    "prepararReporteProductosMasVendidos NullDates"
                );
            }

            if (dateFrom.isAfter(dateTo)) {
                return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_CLIENTE,
                    "La fecha de inicio no puede ser posterior a la fecha de fin",
                    "prepararReporteProductosMasVendidos InvalidDates"
                );
            }

            // 2. Obtener todas las facturas en el rango de fechas
            Respuesta invoicesResponse = invoiceService.getInvoices();
            if (!invoicesResponse.getEstado()) {
                return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No se encontraron facturas.",
                    "prepararReporteProductosMasVendidos " + invoicesResponse.getMensaje()
                );
            }

            @SuppressWarnings("unchecked")
            List<InvoiceDTO> allInvoices = (List<InvoiceDTO>) invoicesResponse.getResultado(
                "Invoices"
            );

            // 3. Filtrar facturas por rango de fechas
            List<InvoiceDTO> invoicesByDateRange = new ArrayList<>();
            if (allInvoices != null) {
                for (InvoiceDTO invoice : allInvoices) {
                    if (
                        invoice.getCreatedAt() != null &&
                        !invoice.getCreatedAt().isBefore(dateFrom) &&
                        !invoice.getCreatedAt().isAfter(dateTo)
                    ) {
                        invoicesByDateRange.add(invoice);
                    }
                }
            }

            if (invoicesByDateRange.isEmpty()) {
                return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No existen facturas en el período especificado.",
                    "prepararReporteProductosMasVendidos NoInvoicesInRange"
                );
            }

            // 4. Agrupar y agregar datos de ventas por producto
            Map<String, ProductSalesDTO> productSalesMap = new HashMap<>();

            for (InvoiceDTO invoice : invoicesByDateRange) {
                // Obtener la orden del cliente asociada
                Respuesta orderResponse = orderService.getCustomerOrder(
                    invoice.getCustomerOrderId()
                );

                if (orderResponse.getEstado()) {
                    CustomerOrderDTO order =
                        (CustomerOrderDTO) orderResponse.getResultado("CustomerOrder");

                    if (order != null && order.getOrderItems() != null) {
                        // Iterar sobre los items de la orden
                        for (OrderItemDTO item : order.getOrderItems()) {
                            String productKey = item.getProductName();

                            // Obtener el producto para acceder al grupo/categoría
                            String productGroupName = "Sin Categoría";
                            if (item.getProductId() != null) {
                                try {
                                    Respuesta productResponse = productService.getProduct(
                                        item.getProductId()
                                    );
                                    if (productResponse.getEstado()) {
                                        ProductDTO product = (ProductDTO) productResponse.getResultado(
                                            "Product"
                                        );
                                        if (
                                            product != null &&
                                            product.getProductGroupName() != null
                                        ) {
                                            productGroupName = product.getProductGroupName();
                                        }
                                    }
                                } catch (Exception ex) {
                                    LOG.log(
                                        Level.WARNING,
                                        "No se pudo obtener grupo de producto para item " +
                                            item.getProductId(),
                                        ex
                                    );
                                }
                            }

                            if (!productSalesMap.containsKey(productKey)) {
                                // Crear nuevo registro de producto
                                ProductSalesDTO productSales = new ProductSalesDTO(
                                    item.getProductName(),
                                    productGroupName,
                                    item.getQuantity(),
                                    item.getUnitPrice()
                                );
                                productSalesMap.put(productKey, productSales);
                            } else {
                                // Agregar a registro existente
                                ProductSalesDTO existing = productSalesMap.get(productKey);
                                existing.addQuantity(item.getQuantity());
                                if (item.getUnitPrice() != null) {
                                    existing.setAvgPrice(item.getUnitPrice());
                                }
                            }
                        }
                    }
                }
            }

            if (productSalesMap.isEmpty()) {
                return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No hay productos vendidos en el período especificado.",
                    "prepararReporteProductosMasVendidos NoProducts"
                );
            }

            // 5. Convertir a lista y ordenar descendente por cantidad vendida
            List<ProductSalesDTO> productSalesList = new ArrayList<>(productSalesMap.values());
            productSalesList.sort((a, b) -> {
                Integer qtdA = a.getTotalQuantitySold() != null ? a.getTotalQuantitySold() : 0;
                Integer qtdB = b.getTotalQuantitySold() != null ? b.getTotalQuantitySold() : 0;
                return qtdB.compareTo(qtdA); // Descendente
            });

            // 6. Calcular totales para el resumen
            Integer totalProducts = productSalesList.size();
            Integer totalQuantity = productSalesList
                .stream()
                .mapToInt(p -> p.getTotalQuantitySold() != null ? p.getTotalQuantitySold() : 0)
                .sum();
            Double totalRevenue = productSalesList
                .stream()
                .mapToDouble(p -> p.getTotalRevenue() != null ? p.getTotalRevenue() : 0.0)
                .sum();

            // 7. Obtener datos de la compañía
            Respuesta paramsResponse = paramService.getSystemParameters();
            Map<String, String> paramsMap = new HashMap<>();

            if (paramsResponse.getEstado()) {
                @SuppressWarnings("unchecked")
                List<SystemParameterDTO> systemParams = (List<
                    SystemParameterDTO
                >) paramsResponse.getResultado("SystemParameters");
                for (SystemParameterDTO param : systemParams) {
                    paramsMap.put(param.getParamName(), param.getParamValue());
                }
            }

            String companyName = paramsMap.getOrDefault("company.name", "KoffeeFX");
            String address = paramsMap.getOrDefault("company.address", "");
            String phone = paramsMap.getOrDefault("company.phone", "");

            // 8. Preparar Map de parámetros para el reporte
            Map<String, Object> parameters = new HashMap<>();

            // Parámetros de fechas
            parameters.put("dateFrom", dateFrom);
            parameters.put("dateTo", dateTo);

            // Parámetros de la compañía
            parameters.put("companyName", companyName);
            parameters.put("restaurantAddress", address);
            parameters.put("restaurantPhone", phone);

            // Moneda
            parameters.put("currency", "₡");

            // Parámetros de totales
            parameters.put("totalProductsSold", totalProducts);
            parameters.put("totalUnitsQuantity", totalQuantity);
            parameters.put("totalRevenue", totalRevenue);

            // Parámetros de generación
            parameters.put("reportGeneratedAt", LocalDate.now());
            parameters.put("generatedByUser", System.getProperty("user.name", "System"));

            // 9. Crear JRBeanCollectionDataSource con los productos
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
                productSalesList
            );

            // 10. Compilar el informe desde /reports/Products-Report.jrxml
            InputStream reportStream = getClass().getResourceAsStream(
                "/reports/Products-Report.jrxml"
            );
            if (reportStream == null) {
                return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_INTERNO,
                    "No se encontró el archivo de reporte Products-Report.jrxml",
                    "prepararReporteProductosMasVendidos FileNotFound"
                );
            }

            LOG.log(
                Level.INFO,
                "Compilando reporte Products-Report.jrxml con lenguaje Java (JDT compiler)"
            );
            JasperReport jasperReport = JasperCompileManager.compileReport(
                reportStream
            );

            // 11. Llenar el reporte con los parámetros y el dataSource
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                parameters,
                dataSource
            );

            return new Respuesta(
                true,
                CodigoRespuesta.CORRECTO,
                "",
                "",
                "JasperPrint",
                jasperPrint
            );
        } catch (Exception ex) {
            LOG.log(
                Level.SEVERE,
                "Ocurrió un error al preparar el reporte de productos más vendidos.",
                ex
            );
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_INTERNO,
                "Ocurrió un error al preparar el reporte de productos más vendidos.",
                "prepararReporteProductosMasVendidos " + ex.getMessage()
            );
        }
    }

    /**
     * Genera un reporte de productos más vendidos en PDF y retorna el array de bytes
     *
     * @param dateFrom Fecha inicial del período
     * @param dateTo Fecha final del período
     * @return Respuesta con el array de bytes del PDF
     */
    public Respuesta generarReporteProductosMasVendidosPDFBytes(LocalDate dateFrom, LocalDate dateTo) {
        try {
            Respuesta reporteResponse = prepararReporteProductosMasVendidos(dateFrom, dateTo);
            if (!reporteResponse.getEstado()) {
                return reporteResponse;
            }

            JasperPrint jasperPrint =
                (JasperPrint) reporteResponse.getResultado("JasperPrint");

            // Exportar a bytes
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

            LOG.log(
                Level.INFO,
                "Reporte de productos más vendidos PDF generado exitosamente como bytes"
            );
            return new Respuesta(
                true,
                CodigoRespuesta.CORRECTO,
                "Reporte generado exitosamente.",
                "",
                "PDFBytes",
                pdfBytes
            );
        } catch (Exception ex) {
            LOG.log(
                Level.SEVERE,
                "Ocurrió un error al generar el reporte de productos más vendidos PDF.",
                ex
            );
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_INTERNO,
                "Ocurrió un error al generar el reporte de productos más vendidos PDF.",
                "generarReporteProductosMasVendidosPDFBytes " + ex.getMessage()
            );
        }
    }
}
