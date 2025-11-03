package cr.ac.una.koffeefxws.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
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

import cr.ac.una.koffeefxws.model.DiningAreaDTO;
import cr.ac.una.koffeefxws.service.DiningAreaService;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;
import cr.ac.una.koffeefxws.util.Secure;

/**
 * @author quesadx
 */
@Secure
@Path("/DiningAreaController")
@Tag(name = "DiningAreas", description = "Operations on dining areas")
@SecurityRequirement(name = "jwt-auth")
public class DiningAreaController {

  @EJB DiningAreaService diningAreaService;

  @Secure
  @GET
  @Path("/diningarea/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets a dining area by ID")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Dining area found",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = DiningAreaDTO.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Dining area not found",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response getDiningArea(
      @Parameter(description = "Dining area ID") @PathParam("id") Long id) {
    try {
      Respuesta r = diningAreaService.getDiningArea(id);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      DiningAreaDTO diningArea = (DiningAreaDTO) r.getResultado("DiningArea");
      return Response.ok(diningArea).build();
    } catch (Exception ex) {
      Logger.getLogger(DiningAreaController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting the dining area.")
          .build();
    }
  }

  @Secure
  @GET
  @Path("/diningareas")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets all dining areas")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Dining areas found",
        content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    @ApiResponse(
        responseCode = "404",
        description = "No dining areas registered",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response getDiningAreas() {
    try {
      Respuesta r = diningAreaService.getDiningAreas();
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      return Response.ok(
              new GenericEntity<List<DiningAreaDTO>>(
                  (List<DiningAreaDTO>) r.getResultado("DiningAreas")) {})
          .build();
    } catch (Exception ex) {
      Logger.getLogger(DiningAreaController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting dining areas")
          .build();
    }
  }

  @Secure
  @GET
  @Path("/diningareas/active")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets all active dining areas")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Active dining areas found",
        content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    @ApiResponse(
        responseCode = "404",
        description = "No active dining areas",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response getActiveDiningAreas() {
    try {
      Respuesta r = diningAreaService.getActiveDiningAreas();
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      return Response.ok(
              new GenericEntity<List<DiningAreaDTO>>(
                  (List<DiningAreaDTO>) r.getResultado("DiningAreas")) {})
          .build();
    } catch (Exception ex) {
      Logger.getLogger(DiningAreaController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting active dining areas")
          .build();
    }
  }

  @Secure
  @POST
  @Path("/diningarea")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Creates or updates a dining area")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Dining area saved successfully",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = DiningAreaDTO.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Dining area not found",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response guardarDiningArea(DiningAreaDTO diningArea) {
    try {
      Respuesta r = diningAreaService.guardarDiningArea(diningArea);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      DiningAreaDTO diningAreaDto = (DiningAreaDTO) r.getResultado("DiningArea");
      return Response.ok(diningAreaDto).build();
    } catch (Exception ex) {
      Logger.getLogger(DiningAreaController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error saving the dining area.")
          .build();
    }
  }

  @Secure
  @DELETE
  @Path("/diningarea/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Deletes a dining area")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Dining area deleted successfully"),
    @ApiResponse(
        responseCode = "404",
        description = "Dining area not found",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response eliminarDiningArea(
      @Parameter(description = "Dining area ID") @PathParam("id") Long id) {
    try {
      Respuesta r = diningAreaService.eliminarDiningArea(id);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      return Response.ok().build();
    } catch (Exception ex) {
      Logger.getLogger(DiningAreaController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error deleting the dining area.")
          .build();
    }
  }
}
