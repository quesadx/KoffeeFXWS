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

import cr.ac.una.koffeefxws.model.SystemParameterDTO;
import cr.ac.una.koffeefxws.service.SystemParameterService;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;
import cr.ac.una.koffeefxws.util.Secure;

/**
 * @author quesadx
 */
@Secure
@Path("/SystemParameterController")
@Tag(name = "SystemParameters", description = "Operations on system parameters")
@SecurityRequirement(name = "jwt-auth")
public class SystemParameterController {

  @EJB SystemParameterService systemParameterService;

  @Secure
  @GET
  @Path("/systemparameter/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets a system parameter by ID")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "System parameter found",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SystemParameterDTO.class))),
    @ApiResponse(
        responseCode = "404",
        description = "System parameter not found",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response getSystemParameter(
      @Parameter(description = "System parameter ID") @PathParam("id") Long id) {
    try {
      Respuesta r = systemParameterService.getSystemParameter(id);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      SystemParameterDTO systemParameter = (SystemParameterDTO) r.getResultado("SystemParameter");
      return Response.ok(systemParameter).build();
    } catch (Exception ex) {
      Logger.getLogger(SystemParameterController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting the system parameter.")
          .build();
    }
  }

  @Secure
  @GET
  @Path("/systemparameter/name/{paramName}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets a system parameter by name")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "System parameter found",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SystemParameterDTO.class))),
    @ApiResponse(
        responseCode = "404",
        description = "System parameter not found",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response getSystemParameterByName(
      @Parameter(description = "Parameter name") @PathParam("paramName") String paramName) {
    try {
      Respuesta r = systemParameterService.getSystemParameterByName(paramName);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      SystemParameterDTO systemParameter = (SystemParameterDTO) r.getResultado("SystemParameter");
      return Response.ok(systemParameter).build();
    } catch (Exception ex) {
      Logger.getLogger(SystemParameterController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting the system parameter.")
          .build();
    }
  }

  @Secure
  @GET
  @Path("/systemparameters")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets all system parameters")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "System parameters found",
        content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    @ApiResponse(
        responseCode = "404",
        description = "No system parameters registered",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response getSystemParameters() {
    try {
      Respuesta r = systemParameterService.getSystemParameters();
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      return Response.ok(
              new GenericEntity<List<SystemParameterDTO>>(
                  (List<SystemParameterDTO>) r.getResultado("SystemParameters")) {})
          .build();
    } catch (Exception ex) {
      Logger.getLogger(SystemParameterController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting system parameters")
          .build();
    }
  }

  @Secure
  @POST
  @Path("/systemparameter")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Creates or updates a system parameter")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "System parameter saved successfully",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SystemParameterDTO.class))),
    @ApiResponse(
        responseCode = "404",
        description = "System parameter not found",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response guardarSystemParameter(SystemParameterDTO systemParameter) {
    try {
      Respuesta r = systemParameterService.guardarSystemParameter(systemParameter);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      SystemParameterDTO systemParameterDto =
          (SystemParameterDTO) r.getResultado("SystemParameter");
      return Response.ok(systemParameterDto).build();
    } catch (Exception ex) {
      Logger.getLogger(SystemParameterController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error saving the system parameter.")
          .build();
    }
  }

  @Secure
  @DELETE
  @Path("/systemparameter/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Deletes a system parameter")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "System parameter deleted successfully"),
    @ApiResponse(
        responseCode = "404",
        description = "System parameter not found",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response eliminarSystemParameter(
      @Parameter(description = "System parameter ID") @PathParam("id") Long id) {
    try {
      Respuesta r = systemParameterService.eliminarSystemParameter(id);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      return Response.ok().build();
    } catch (Exception ex) {
      Logger.getLogger(SystemParameterController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error deleting the system parameter.")
          .build();
    }
  }
}
