package cr.ac.una.koffeefxws.controller;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import cr.ac.una.koffeefxws.service.ReportService;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;
import cr.ac.una.koffeefxws.util.Secure;

/**
 * Controlador REST para la generación de reportes
 *
 * @author krist
 */
@Secure
@Path("/ReportController")
@Tag(name = "Reports", description = "Operaciones para generación de reportes")
@SecurityRequirement(name = "jwt-auth")
public class ReportController {

  private static final Logger LOG = Logger.getLogger(ReportController.class.getName());

  @EJB ReportService reportService;

  private Response generarPDF(Long orderId, String contentDisposition) {
    if (orderId == null) {
      LOG.log(Level.WARNING, "Se intentó generar factura con orderId nulo");
      return Response.status(CodigoRespuesta.ERROR_CLIENTE.getValue())
          .type(MediaType.APPLICATION_JSON)
          .entity("{\"error\": \"El ID de la orden es obligatorio\"}")
          .build();
    }

    try {
      Respuesta r = reportService.generarFacturaPDFBytes(orderId);

      if (!r.getEstado()) {
        LOG.log(
            Level.WARNING,
            "Error generando factura para orden {0}: {1}",
            new Object[] {orderId, r.getMensaje()});
        return Response.status(r.getCodigoRespuesta().getValue())
            .type(MediaType.APPLICATION_JSON)
            .entity("{\"error\": \"" + r.getMensaje() + "\"}")
            .build();
      }

      byte[] pdfBytes = (byte[]) r.getResultado("PDFBytes");

      LOG.log(Level.INFO, "Factura PDF generada exitosamente para orden {0}", orderId);

      return Response.ok(pdfBytes)
          .header(
              "Content-Disposition", contentDisposition + "; filename=factura_" + orderId + ".pdf")
          .header("Content-Type", "application/pdf")
          .build();
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "Error generando factura PDF para orden " + orderId, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .type(MediaType.APPLICATION_JSON)
          .entity("{\"error\": \"Error generando la factura PDF: " + ex.getMessage() + "\"}")
          .build();
    }
  }

  @GET
  @Path("/invoice/{orderId}/pdf")
  @Produces("application/pdf")
  @Operation(description = "Genera y descarga una factura en formato PDF para una orden específica")
  public Response generarFacturaPDF(
      @Parameter(description = "ID de la orden", required = true) @PathParam("orderId")
          Long orderId) {
    return generarPDF(orderId, "attachment");
  }

  @GET
  @Path("/invoice/{orderId}/preview")
  @Produces("application/pdf")
  @Operation(
      description =
          "Genera y retorna una factura en formato PDF para visualización en línea (sin descarga)")
  public Response previsualizarFacturaPDF(
      @Parameter(description = "ID de la orden", required = true) @PathParam("orderId")
          Long orderId) {
    return generarPDF(orderId, "inline");
  }

  private Response generarPDFCashierClosing(Long cashOpeningId, String contentDisposition) {
    if (cashOpeningId == null) {
      LOG.log(Level.WARNING, "Se intentó generar reporte de cierre con cashOpeningId nulo");
      return Response.status(CodigoRespuesta.ERROR_CLIENTE.getValue())
          .type(MediaType.APPLICATION_JSON)
          .entity("{\"error\": \"El ID de la apertura de caja es obligatorio\"}")
          .build();
    }

    try {
      Respuesta r = reportService.generarReporteCashierPDFBytes(cashOpeningId);

      if (!r.getEstado()) {
        LOG.log(
            Level.WARNING,
            "Error generando reporte de cierre para cashOpening {0}: {1}",
            new Object[] {cashOpeningId, r.getMensaje()});
        return Response.status(r.getCodigoRespuesta().getValue())
            .type(MediaType.APPLICATION_JSON)
            .entity("{\"error\": \"" + r.getMensaje() + "\"}")
            .build();
      }

      byte[] pdfBytes = (byte[]) r.getResultado("PDFBytes");

      LOG.log(
          Level.INFO,
          "Reporte de cierre de caja PDF generado exitosamente para cashOpening {0}",
          cashOpeningId);

      return Response.ok(pdfBytes)
          .header(
              "Content-Disposition",
              contentDisposition + "; filename=cierre_caja_" + cashOpeningId + ".pdf")
          .header("Content-Type", "application/pdf")
          .build();
    } catch (Exception ex) {
      LOG.log(
          Level.SEVERE,
          "Error generando reporte de cierre de caja PDF para cashOpening " + cashOpeningId,
          ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .type(MediaType.APPLICATION_JSON)
          .entity(
              "{\"error\": \"Error generando el reporte de cierre de caja PDF: "
                  + ex.getMessage()
                  + "\"}")
          .build();
    }
  }

  @GET
  @Path("/cashier-closing/{cashOpeningId}/pdf")
  @Produces("application/pdf")
  @Operation(description = "Genera y descarga un reporte de cierre de caja en formato PDF")
  public Response generarReporteCashierClosingPDF(
      @Parameter(description = "ID de la apertura de caja", required = true)
          @PathParam("cashOpeningId")
          Long cashOpeningId) {
    return generarPDFCashierClosing(cashOpeningId, "attachment");
  }

  @GET
  @Path("/cashier-closing/{cashOpeningId}/preview")
  @Produces("application/pdf")
  @Operation(
      description =
          "Genera y retorna un reporte de cierre de caja en formato PDF para visualización en línea (sin descarga)")
  public Response previsualizarReporteCashierClosingPDF(
      @Parameter(description = "ID de la apertura de caja", required = true)
          @PathParam("cashOpeningId")
          Long cashOpeningId) {
    return generarPDFCashierClosing(cashOpeningId, "inline");
  }

  private Response generarPDFInvoicesByDate(
      LocalDate dateFrom, LocalDate dateTo, String contentDisposition) {
    // Validar que las fechas no sean nulas
    if (dateFrom == null || dateTo == null) {
      LOG.log(Level.WARNING, "Se intentó generar reporte de facturas por fecha con fechas nulas");
      return Response.status(CodigoRespuesta.ERROR_CLIENTE.getValue())
          .type(MediaType.APPLICATION_JSON)
          .entity("{\"error\": \"Las fechas de inicio y fin son obligatorias\"}")
          .build();
    }

    try {
      LOG.log(
          Level.INFO,
          "Generando reporte de facturas por fecha: {0} a {1}",
          new Object[] {dateFrom, dateTo});

      Respuesta r = reportService.generarReporteInvoicesByDatePDFBytes(dateFrom, dateTo);

      if (!r.getEstado()) {
        LOG.log(
            Level.WARNING,
            "Error generando reporte de facturas por fecha {0} a {1}: {2}",
            new Object[] {dateFrom, dateTo, r.getMensaje()});
        LOG.log(Level.WARNING, "Detalle del error: {0}", r.getMensajeInterno());
        return Response.status(r.getCodigoRespuesta().getValue())
            .type(MediaType.APPLICATION_JSON)
            .entity(
                "{\"error\": \""
                    + r.getMensaje()
                    + "\", \"detalle\": \""
                    + r.getMensajeInterno()
                    + "\"}")
            .build();
      }

      byte[] pdfBytes = (byte[]) r.getResultado("PDFBytes");

      LOG.log(
          Level.INFO,
          "Reporte de facturas por fecha PDF generado exitosamente para período {0} a {1}",
          new Object[] {dateFrom, dateTo});

      return Response.ok(pdfBytes)
          .header(
              "Content-Disposition",
              contentDisposition + "; filename=facturas_" + dateFrom + "_a_" + dateTo + ".pdf")
          .header("Content-Type", "application/pdf")
          .build();
    } catch (Exception ex) {
      LOG.log(
          Level.SEVERE,
          "Error generando reporte de facturas por fecha para período " + dateFrom + " a " + dateTo,
          ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .type(MediaType.APPLICATION_JSON)
          .entity(
              "{\"error\": \"Error generando el reporte de facturas por fecha PDF: "
                  + ex.getMessage()
                  + "\"}")
          .build();
    }
  }

  @GET
  @Path("/invoices-by-date/{dateFrom}/{dateTo}/pdf")
  @Produces("application/pdf")
  @Operation(
      description =
          "Genera y descarga un reporte de facturas en un período de fechas en formato PDF")
  public Response generarInvoicesByDatePDF(
      @Parameter(description = "Fecha inicial (YYYY-MM-DD)", required = true) @PathParam("dateFrom")
          String dateFromStr,
      @Parameter(description = "Fecha final (YYYY-MM-DD)", required = true) @PathParam("dateTo")
          String dateToStr) {
    try {
      LocalDate dateFrom = LocalDate.parse(dateFromStr);
      LocalDate dateTo = LocalDate.parse(dateToStr);
      return generarPDFInvoicesByDate(dateFrom, dateTo, "attachment");
    } catch (Exception ex) {
      LOG.log(
          Level.WARNING,
          "Formato de fecha inválido: {0} a {1}",
          new Object[] {dateFromStr, dateToStr});
      return Response.status(CodigoRespuesta.ERROR_CLIENTE.getValue())
          .type(MediaType.APPLICATION_JSON)
          .entity("{\"error\": \"Formato de fecha inválido. Use YYYY-MM-DD\"}")
          .build();
    }
  }

  @GET
  @Path("/invoices-by-date/{dateFrom}/{dateTo}/preview")
  @Produces("application/pdf")
  @Operation(
      description =
          "Genera y retorna un reporte de facturas en un período de fechas en formato PDF para visualización en línea (sin descarga)")
  public Response previsualizarInvoicesByDatePDF(
      @Parameter(description = "Fecha inicial (YYYY-MM-DD)", required = true) @PathParam("dateFrom")
          String dateFromStr,
      @Parameter(description = "Fecha final (YYYY-MM-DD)", required = true) @PathParam("dateTo")
          String dateToStr) {
    try {
      LocalDate dateFrom = LocalDate.parse(dateFromStr);
      LocalDate dateTo = LocalDate.parse(dateToStr);
      return generarPDFInvoicesByDate(dateFrom, dateTo, "inline");
    } catch (Exception ex) {
      LOG.log(
          Level.WARNING,
          "Formato de fecha inválido: {0} a {1}",
          new Object[] {dateFromStr, dateToStr});
      return Response.status(CodigoRespuesta.ERROR_CLIENTE.getValue())
          .type(MediaType.APPLICATION_JSON)
          .entity("{\"error\": \"Formato de fecha inválido. Use YYYY-MM-DD\"}")
          .build();
    }
  }

  private Response generarPDFProductosMasVendidos(
      LocalDate dateFrom, LocalDate dateTo, String contentDisposition) {
    // Validar que las fechas no sean nulas
    if (dateFrom == null || dateTo == null) {
      LOG.log(
          Level.WARNING, "Se intentó generar reporte de productos más vendidos con fechas nulas");
      return Response.status(CodigoRespuesta.ERROR_CLIENTE.getValue())
          .type(MediaType.APPLICATION_JSON)
          .entity("{\"error\": \"Las fechas de inicio y fin son obligatorias\"}")
          .build();
    }

    try {
      Respuesta r = reportService.generarReporteProductosMasVendidosPDFBytes(dateFrom, dateTo);

      if (!r.getEstado()) {
        LOG.log(
            Level.WARNING,
            "Error generando reporte de productos más vendidos {0} a {1}: {2}",
            new Object[] {dateFrom, dateTo, r.getMensaje()});
        return Response.status(r.getCodigoRespuesta().getValue())
            .type(MediaType.APPLICATION_JSON)
            .entity("{\"error\": \"" + r.getMensaje() + "\"}")
            .build();
      }

      byte[] pdfBytes = (byte[]) r.getResultado("PDFBytes");

      LOG.log(
          Level.INFO,
          "Reporte de productos más vendidos PDF generado exitosamente para período {0} a {1}",
          new Object[] {dateFrom, dateTo});

      return Response.ok(pdfBytes)
          .header(
              "Content-Disposition",
              contentDisposition
                  + "; filename=productos_vendidos_"
                  + dateFrom
                  + "_a_"
                  + dateTo
                  + ".pdf")
          .header("Content-Type", "application/pdf")
          .build();
    } catch (Exception ex) {
      LOG.log(
          Level.SEVERE,
          "Error generando reporte de productos más vendidos para período "
              + dateFrom
              + " a "
              + dateTo,
          ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .type(MediaType.APPLICATION_JSON)
          .entity(
              "{\"error\": \"Error generando el reporte de productos más vendidos PDF: "
                  + ex.getMessage()
                  + "\"}")
          .build();
    }
  }

  @GET
  @Path("/products-sales/{dateFrom}/{dateTo}/pdf")
  @Produces("application/pdf")
  @Operation(
      description =
          "Genera y descarga un reporte de productos más vendidos en un período de fechas en formato PDF")
  public Response generarProductosMasVendidosPDF(
      @Parameter(description = "Fecha inicial (YYYY-MM-DD)", required = true) @PathParam("dateFrom")
          String dateFromStr,
      @Parameter(description = "Fecha final (YYYY-MM-DD)", required = true) @PathParam("dateTo")
          String dateToStr) {
    try {
      LocalDate dateFrom = LocalDate.parse(dateFromStr);
      LocalDate dateTo = LocalDate.parse(dateToStr);
      return generarPDFProductosMasVendidos(dateFrom, dateTo, "attachment");
    } catch (Exception ex) {
      LOG.log(
          Level.WARNING,
          "Formato de fecha inválido: {0} a {1}",
          new Object[] {dateFromStr, dateToStr});
      return Response.status(CodigoRespuesta.ERROR_CLIENTE.getValue())
          .type(MediaType.APPLICATION_JSON)
          .entity("{\"error\": \"Formato de fecha inválido. Use YYYY-MM-DD\"}")
          .build();
    }
  }

  @GET
  @Path("/products-sales/{dateFrom}/{dateTo}/preview")
  @Produces("application/pdf")
  @Operation(
      description =
          "Genera y retorna un reporte de productos más vendidos en un período de fechas en formato PDF para visualización en línea (sin descarga)")
  public Response previsualizarProductosMasVendidosPDF(
      @Parameter(description = "Fecha inicial (YYYY-MM-DD)", required = true) @PathParam("dateFrom")
          String dateFromStr,
      @Parameter(description = "Fecha final (YYYY-MM-DD)", required = true) @PathParam("dateTo")
          String dateToStr) {
    try {
      LocalDate dateFrom = LocalDate.parse(dateFromStr);
      LocalDate dateTo = LocalDate.parse(dateToStr);
      return generarPDFProductosMasVendidos(dateFrom, dateTo, "inline");
    } catch (Exception ex) {
      LOG.log(
          Level.WARNING,
          "Formato de fecha inválido: {0} a {1}",
          new Object[] {dateFromStr, dateToStr});
      return Response.status(CodigoRespuesta.ERROR_CLIENTE.getValue())
          .type(MediaType.APPLICATION_JSON)
          .entity("{\"error\": \"Formato de fecha inválido. Use YYYY-MM-DD\"}")
          .build();
    }
  }
}
