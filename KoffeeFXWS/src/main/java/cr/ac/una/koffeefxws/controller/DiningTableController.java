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

import cr.ac.una.koffeefxws.model.DiningTableDTO;
import cr.ac.una.koffeefxws.service.DiningTableService;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;
import cr.ac.una.koffeefxws.util.Secure;

/**
 * @author quesadx
 */
@Secure
@Path("/DiningTableController")
@Tag(name = "DiningTables", description = "Operations on dining tables")
@SecurityRequirement(name = "jwt-auth")
public class DiningTableController {

  @EJB DiningTableService diningTableService;

  @Secure
  @GET
  @Path("/diningtable/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets a dining table by ID")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Dining table found",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = DiningTableDTO.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Dining table not found",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response getDiningTable(
      @Parameter(description = "Dining table ID") @PathParam("id") Long id) {
    try {
      Respuesta r = diningTableService.getDiningTable(id);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      DiningTableDTO diningTable = (DiningTableDTO) r.getResultado("DiningTable");
      return Response.ok(diningTable).build();
    } catch (Exception ex) {
      Logger.getLogger(DiningTableController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting the dining table.")
          .build();
    }
  }

  @Secure
  @GET
  @Path("/diningtables")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets all dining tables")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Dining tables found",
        content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    @ApiResponse(
        responseCode = "404",
        description = "No dining tables registered",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response getDiningTables() {
    try {
      Respuesta r = diningTableService.getDiningTables();
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      return Response.ok(
              new GenericEntity<List<DiningTableDTO>>(
                  (List<DiningTableDTO>) r.getResultado("DiningTables")) {})
          .build();
    } catch (Exception ex) {
      Logger.getLogger(DiningTableController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting dining tables")
          .build();
    }
  }

  @Secure
  @POST
  @Path("/diningtable")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Creates or updates a dining table")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Dining table saved successfully",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = DiningTableDTO.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Dining table not found",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response guardarDiningTable(DiningTableDTO diningTable) {
    try {
      Respuesta r = diningTableService.guardarDiningTable(diningTable);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      DiningTableDTO diningTableDto = (DiningTableDTO) r.getResultado("DiningTable");
      return Response.ok(diningTableDto).build();
    } catch (Exception ex) {
      Logger.getLogger(DiningTableController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error saving the dining table.")
          .build();
    }
  }

  @Secure
  @DELETE
  @Path("/diningtable/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Deletes a dining table")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Dining table deleted successfully"),
    @ApiResponse(
        responseCode = "404",
        description = "Dining table not found",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response eliminarDiningTable(
      @Parameter(description = "Dining table ID") @PathParam("id") Long id) {
    try {
      Respuesta r = diningTableService.eliminarDiningTable(id);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      return Response.ok().build();
    } catch (Exception ex) {
      Logger.getLogger(DiningTableController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error deleting the dining table.")
          .build();
    }
  }
}
