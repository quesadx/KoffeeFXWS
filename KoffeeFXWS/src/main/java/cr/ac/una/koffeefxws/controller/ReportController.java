package cr.ac.una.koffeefxws.controller;

import cr.ac.una.koffeefxws.service.ReportService;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;
import cr.ac.una.koffeefxws.util.Secure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    @EJB
    ReportService reportService;

    /**
     * Método privado para generar el PDF de la factura
     * 
     * @param orderId ID de la orden
     * @param contentDisposition "attachment" para descarga, "inline" para visualización
     * @return Response con el PDF o mensaje de error
     */
    private Response generarPDF(Long orderId, String contentDisposition) {
        // Validar que orderId no sea nulo
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
                LOG.log(Level.WARNING, "Error generando factura para orden {0}: {1}", 
                    new Object[]{orderId, r.getMensaje()});
                return Response.status(r.getCodigoRespuesta().getValue())
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\": \"" + r.getMensaje() + "\"}")
                    .build();
            }
            
            byte[] pdfBytes = (byte[]) r.getResultado("PDFBytes");
            
            LOG.log(Level.INFO, "Factura PDF generada exitosamente para orden {0}", orderId);
            
            return Response.ok(pdfBytes)
                .header("Content-Disposition", contentDisposition + "; filename=factura_" + orderId + ".pdf")
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

    /**
     * Genera y descarga una factura en formato PDF
     * 
     * @param orderId ID de la orden para generar la factura
     * @return Response con el PDF como bytes o mensaje de error
     */
    @GET
    @Path("/invoice/{orderId}/pdf")
    @Produces("application/pdf")
    @Operation(description = "Genera y descarga una factura en formato PDF para una orden específica")
    public Response generarFacturaPDF(
            @Parameter(description = "ID de la orden", required = true) 
            @PathParam("orderId") Long orderId) {
        return generarPDF(orderId, "attachment");
    }

    /**
     * Genera y retorna la factura en formato PDF para visualización en línea
     * 
     * @param orderId ID de la orden para generar la factura
     * @return Response con el PDF para visualización en navegador
     */
    @GET
    @Path("/invoice/{orderId}/preview")
    @Produces("application/pdf")
    @Operation(description = "Genera y retorna una factura en formato PDF para visualización en línea (sin descarga)")
    public Response previsualizarFacturaPDF(
            @Parameter(description = "ID de la orden", required = true) 
            @PathParam("orderId") Long orderId) {
        return generarPDF(orderId, "inline");
    }
}
