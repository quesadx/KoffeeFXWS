package cr.ac.una.koffeefxws.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import cr.ac.una.koffeefxws.model.CashOpeningDTO;
import cr.ac.una.koffeefxws.service.CashOpeningService;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;
import cr.ac.una.koffeefxws.util.Secure;

@Secure
@Path("/CashOpeningController")
@Tag(name = "CashOpenings", description = "Operaciones sobre aperturas de caja")
@SecurityRequirement(name = "jwt-auth")
public class CashOpeningController {

    @EJB CashOpeningService cashOpeningService;

    @GET
    @Path("/cashOpening/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene una apertura de caja por ID")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Apertura de caja encontrada",
                content =
                        @Content(
                                mediaType = MediaType.APPLICATION_JSON,
                                schema = @Schema(implementation = CashOpeningDTO.class))),
        @ApiResponse(
                responseCode = "404",
                description = "Apertura de caja no encontrada",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    })
    public Response getCashOpening(
            @Parameter(description = "ID de la apertura") @PathParam("id") Long id) {
        try {
            Respuesta r = cashOpeningService.getCashOpening(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje())
                        .build();
            }
            CashOpeningDTO dto = (CashOpeningDTO) r.getResultado("CashOpening");
            return Response.ok(dto).build();
        } catch (Exception ex) {
            Logger.getLogger(CashOpeningController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error obteniendo la apertura de caja.")
                    .build();
        }
    }

    @GET
    @Path("/cashOpenings")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene todas las aperturas de caja")
    public Response getCashOpenings() {
        try {
            Respuesta r = cashOpeningService.getCashOpenings();
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje())
                        .build();
            }
            return Response.ok(
                            new GenericEntity<List<CashOpeningDTO>>(
                                    (List<CashOpeningDTO>) r.getResultado("CashOpenings")) {})
                    .build();
        } catch (Exception ex) {
            Logger.getLogger(CashOpeningController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error obteniendo las aperturas de caja")
                    .build();
        }
    }

    @GET
    @Path("/cashOpening/active/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene la apertura de caja activa de un usuario")
    public Response getActiveCashOpening(
            @Parameter(description = "ID del usuario") @PathParam("userId") Long userId) {
        try {
            Respuesta r = cashOpeningService.getActiveCashOpening(userId);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje())
                        .build();
            }
            CashOpeningDTO dto = (CashOpeningDTO) r.getResultado("CashOpening");
            return Response.ok(dto).build();
        } catch (Exception ex) {
            Logger.getLogger(CashOpeningController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error obteniendo la caja activa")
                    .build();
        }
    }

    @POST
    @Path("/cashOpening")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Guarda o actualiza una apertura de caja")
    public Response guardarCashOpening(CashOpeningDTO dto) {
        try {
            Respuesta r = cashOpeningService.guardarCashOpening(dto);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje())
                        .build();
            }
            CashOpeningDTO saved = (CashOpeningDTO) r.getResultado("CashOpening");
            return Response.ok(saved).build();
        } catch (Exception ex) {
            Logger.getLogger(CashOpeningController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error guardando la apertura de caja.")
                    .build();
        }
    }

    @PUT
    @Path("/cashOpening/close/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Cierra una apertura de caja")
    public Response closeCashOpening(
            @Parameter(description = "ID de la apertura") @PathParam("id") Long id,
            CashOpeningDTO dto) {
        try {
            Long closingAmount =
                    dto.getClosingAmount() != null ? dto.getClosingAmount().longValue() : null;
            String notes = dto.getNotes();
            Respuesta r = cashOpeningService.closeCashOpening(id, closingAmount, notes);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje())
                        .build();
            }
            CashOpeningDTO closed = (CashOpeningDTO) r.getResultado("CashOpening");
            return Response.ok(closed).build();
        } catch (Exception ex) {
            Logger.getLogger(CashOpeningController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error cerrando la apertura de caja.")
                    .build();
        }
    }
}
