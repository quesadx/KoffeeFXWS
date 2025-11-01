/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.koffeefxws.service;

import cr.ac.una.koffeefxws.model.CustomerOrderDTO;
import cr.ac.una.koffeefxws.model.InvoiceDTO;
import cr.ac.una.koffeefxws.model.OrderItemDTO;
import cr.ac.una.koffeefxws.model.SystemParameterDTO;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;
import jakarta.ejb.EJB;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import java.io.FileOutputStream;
import java.io.InputStream;
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
}
