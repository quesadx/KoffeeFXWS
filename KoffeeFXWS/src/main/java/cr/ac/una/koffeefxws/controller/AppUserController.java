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
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import cr.ac.una.koffeefxws.model.AppUserDTO;
import cr.ac.una.koffeefxws.service.AppUserService;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.JwTokenHelper;
import cr.ac.una.koffeefxws.util.Respuesta;
import cr.ac.una.koffeefxws.util.Secure;

/**
 * @author quesadx
 */
@Secure
@Path("/AppUserController")
@Tag(name = "AppUsers", description = "Operaciones sobre usuarios de la aplicación")
@Tag(name = "AppUsers", description = "Operations on application users")
public class AppUserController {

  @EJB AppUserService appUserService;

  @Context SecurityContext securityContext;

  @GET
  @Path("/usuario/{usuario}/{clave}")
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(description = "Authenticates a user")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "User Authenticated",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = AppUserDTO.class))),
    @ApiResponse(
        responseCode = "404",
        description = "User Not Authenticated",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error during user authentication",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response validarUsuario(
      @Parameter(description = "Username") @PathParam("usuario") String usuario,
      @Parameter(description = "User password") @PathParam("clave") String clave) {
    try {
      Respuesta r = appUserService.validateUser(usuario, clave);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      AppUserDTO user = (AppUserDTO) r.getResultado("AppUser");
      user.setToken(JwTokenHelper.getInstance().generatePrivateKey(usuario));
      return Response.ok(user).build();
    } catch (Exception ex) {
      Logger.getLogger(AppUserController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error validating the user.")
          .build();
    }
  }

  @Secure
  @GET
  @Path("/renovar")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(description = "Generates a new token from a refresh token")
  @SecurityRequirement(name = "jwt-auth")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Token successfully renewed",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "401",
        description = "Could not renew the token",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Error renewing the token",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response renovarToken() {
    try {
      String usuarioRequest = securityContext.getUserPrincipal().getName();
      if (usuarioRequest != null && !usuarioRequest.isBlank()) {
        return Response.ok(JwTokenHelper.getInstance().generatePrivateKey(usuarioRequest)).build();
      } else {
        return Response.status(CodigoRespuesta.ERROR_PERMISOS.getValue())
            .entity("Could not renew the token")
            .build();
      }
    } catch (Exception ex) {
      Logger.getLogger(AppUserController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Could not renew the token")
          .build();
    }
  }

  @Secure
  @GET
  @Path("/appuser/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets a user by ID")
  @SecurityRequirement(name = "jwt-auth")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "User found",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = AppUserDTO.class))),
    @ApiResponse(
        responseCode = "404",
        description = "User not found",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response getAppUser(@Parameter(description = "User ID") @PathParam("id") Long id) {
    try {
      Respuesta r = appUserService.getAppUser(id);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      AppUserDTO user = (AppUserDTO) r.getResultado("AppUser");
      return Response.ok(user).build();
    } catch (Exception ex) {
      Logger.getLogger(AppUserController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting the user.")
          .build();
    }
  }

  @Secure
  @GET
  @Path("/appuser/username/{username}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets a user by username")
  @SecurityRequirement(name = "jwt-auth")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Usuario encontrado",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = AppUserDTO.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Usuario no encontrado",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Error interno",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response getAppUserByUsername(
      @Parameter(description = "Username") @PathParam("username") String username) {
    try {
      Respuesta r = appUserService.getAppUserByUsername(username);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      AppUserDTO user = (AppUserDTO) r.getResultado("AppUser");
      return Response.ok(user).build();
    } catch (Exception ex) {
      Logger.getLogger(AppUserController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting the user.")
          .build();
    }
  }

  @Secure
  @GET
  @Path("/appusers")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets all users")
  @SecurityRequirement(name = "jwt-auth")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Users found",
        content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    @ApiResponse(
        responseCode = "404",
        description = "No users registered",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response getAppUsers() {
    try {
      Respuesta r = appUserService.getAppUsers();
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      return Response.ok(
              new GenericEntity<List<AppUserDTO>>((List<AppUserDTO>) r.getResultado("AppUsers")) {})
          .build();
    } catch (Exception ex) {
      Logger.getLogger(AppUserController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting users")
          .build();
    }
  }

  @Secure
  @POST
  @Path("/appuser")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Creates or updates a user")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "User saved successfully",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = AppUserDTO.class))),
    @ApiResponse(
        responseCode = "404",
        description = "User not found",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response guardarAppUser(AppUserDTO appUser) {
    try {
      Respuesta r = appUserService.guardarAppUser(appUser);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      AppUserDTO appUserDto = (AppUserDTO) r.getResultado("AppUser");
      return Response.ok(appUserDto).build();
    } catch (Exception ex) {
      Logger.getLogger(AppUserController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error saving the user.")
          .build();
    }
  }

  @Secure
  @DELETE
  @Path("/appuser/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Deletes a user")
  @SecurityRequirement(name = "jwt-auth")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente"),
    @ApiResponse(responseCode = "200", description = "User deleted successfully"),
    @ApiResponse(
        responseCode = "404",
        description = "User not found",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response eliminarAppUser(@Parameter(description = "User ID") @PathParam("id") Long id) {
    try {
      Respuesta r = appUserService.eliminarAppUser(id);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      return Response.ok().build();
    } catch (Exception ex) {
      Logger.getLogger(AppUserController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error deleting the user.")
          .build();
    }
  }
}
