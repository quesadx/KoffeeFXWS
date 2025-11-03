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

/**
 * @author quesadx
 */
@Secure
@Path("/CashOpeningController")
@Tag(name = "CashOpenings", description = "Operations on cash openings")
@SecurityRequirement(name = "jwt-auth")
public class CashOpeningController {

  @EJB CashOpeningService cashOpeningService;

  @Secure
  @GET
  @Path("/cashOpening/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets a cash opening by ID")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Cash opening found",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = CashOpeningDTO.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Cash opening not found",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response getCashOpening(
      @Parameter(description = "Cash opening ID") @PathParam("id") Long id) {
    try {
      Respuesta r = cashOpeningService.getCashOpening(id);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      CashOpeningDTO dto = (CashOpeningDTO) r.getResultado("CashOpening");
      return Response.ok(dto).build();
    } catch (Exception ex) {
      Logger.getLogger(CashOpeningController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting the cash opening.")
          .build();
    }
  }

  @Secure
  @GET
  @Path("/cashOpenings")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets all cash openings")
  public Response getCashOpenings() {
    try {
      Respuesta r = cashOpeningService.getCashOpenings();
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      return Response.ok(
              new GenericEntity<List<CashOpeningDTO>>(
                  (List<CashOpeningDTO>) r.getResultado("CashOpenings")) {})
          .build();
    } catch (Exception ex) {
      Logger.getLogger(CashOpeningController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting cash openings")
          .build();
    }
  }

  @Secure
  @GET
  @Path("/cashOpening/active/{userId}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets a user's active cash opening")
  public Response getActiveCashOpening(
      @Parameter(description = "User ID") @PathParam("userId") Long userId) {
    try {
      Respuesta r = cashOpeningService.getActiveCashOpening(userId);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      CashOpeningDTO dto = (CashOpeningDTO) r.getResultado("CashOpening");
      return Response.ok(dto).build();
    } catch (Exception ex) {
      Logger.getLogger(CashOpeningController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting the active cash opening")
          .build();
    }
  }

  @Secure
  @POST
  @Path("/cashOpening")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Creates or updates a cash opening")
  public Response guardarCashOpening(CashOpeningDTO dto) {
    try {
      Respuesta r = cashOpeningService.guardarCashOpening(dto);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      CashOpeningDTO saved = (CashOpeningDTO) r.getResultado("CashOpening");
      return Response.ok(saved).build();
    } catch (Exception ex) {
      Logger.getLogger(CashOpeningController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error saving the cash opening.")
          .build();
    }
  }

  @Secure
  @PUT
  @Path("/cashOpening/close/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Closes a cash opening")
  public Response closeCashOpening(
      @Parameter(description = "Cash opening ID") @PathParam("id") Long id, CashOpeningDTO dto) {
    try {
      Long closingAmount =
          dto.getClosingAmount() != null ? dto.getClosingAmount().longValue() : null;
      String notes = dto.getNotes();
      Respuesta r = cashOpeningService.closeCashOpening(id, closingAmount, notes);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      CashOpeningDTO closed = (CashOpeningDTO) r.getResultado("CashOpening");
      return Response.ok(closed).build();
    } catch (Exception ex) {
      Logger.getLogger(CashOpeningController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error closing the cash opening.")
          .build();
    }
  }
}
