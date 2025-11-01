package cr.ac.una.koffeefxws.controller;

import cr.ac.una.koffeefxws.model.AppUserDTO;
import cr.ac.una.koffeefxws.service.AppUserService;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.JwTokenHelper;
import cr.ac.una.koffeefxws.util.Respuesta;
import cr.ac.una.koffeefxws.util.Secure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author quesadx
 */
@Path("/AppUserController")
@Tag(
    name = "AppUsers",
    description = "Operaciones sobre usuarios de la aplicación"
)
public class AppUserController {

    @EJB
    AppUserService appUserService;

    @Context
    SecurityContext securityContext;

    /**
     * Validar usuario (estilo UNAPlanilla): GET con ruta y parámetros de path
     */
    @GET
    @Path("/usuario/{usuario}/{clave}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Autentica un usuario")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Usuario Autenticado",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = AppUserDTO.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Usuario No Autenticado",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error interno durante autenticación del usuario",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
        }
    )
    public Response validarUsuario(
        @Parameter(description = "Codigo de usuario") @PathParam(
            "usuario"
        ) String usuario,
        @Parameter(description = "Clave de usuario") @PathParam(
            "clave"
        ) String clave
    ) {
        try {
            Respuesta r = appUserService.validateUser(usuario, clave);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                    .entity(r.getMensaje())
                    .build();
            }
            AppUserDTO user = (AppUserDTO) r.getResultado("AppUser");
            user.setToken(
                JwTokenHelper.getInstance().generatePrivateKey(usuario)
            );
            return Response.ok(user).build();
        } catch (Exception ex) {
            Logger.getLogger(AppUserController.class.getName()).log(
                Level.SEVERE,
                null,
                ex
            );
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                .entity("Error validando el usuario.")
                .build();
        }
    }

    @Secure
    @GET
    @Path("/renovar")
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(
        description = "Genera un nuevo token a partir de un token de renovación"
    )
    @SecurityRequirement(name = "jwt-auth")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Renovación exitosa del token",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @ApiResponse(
                responseCode = "401",
                description = "No se pudo renovar el token",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error renovando el token",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
        }
    )
    public Response renovarToken() {
        try {
            String usuarioRequest = securityContext
                .getUserPrincipal()
                .getName();
            if (usuarioRequest != null && !usuarioRequest.isBlank()) {
                return Response.ok(
                    JwTokenHelper.getInstance().generatePrivateKey(
                        usuarioRequest
                    )
                ).build();
            } else {
                return Response.status(
                    CodigoRespuesta.ERROR_PERMISOS.getValue()
                )
                    .entity("No se pudo renovar el token")
                    .build();
            }
        } catch (Exception ex) {
            Logger.getLogger(AppUserController.class.getName()).log(
                Level.SEVERE,
                null,
                ex
            );
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                .entity("No se pudo renovar el token")
                .build();
        }
    }

    @Secure
    @GET
    @Path("/appuser/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene un usuario por ID")
    @SecurityRequirement(name = "jwt-auth")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Usuario encontrado",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = AppUserDTO.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Usuario no encontrado",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
        }
    )
    public Response getAppUser(
        @Parameter(description = "ID del usuario") @PathParam("id") Long id
    ) {
        try {
            Respuesta r = appUserService.getAppUser(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                    .entity(r.getMensaje())
                    .build();
            }
            AppUserDTO user = (AppUserDTO) r.getResultado("AppUser");
            return Response.ok(user).build();
        } catch (Exception ex) {
            Logger.getLogger(AppUserController.class.getName()).log(
                Level.SEVERE,
                null,
                ex
            );
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                .entity("Error obteniendo el usuario.")
                .build();
        }
    }

    @Secure
    @GET
    @Path("/appuser/username/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene un usuario por username")
    @SecurityRequirement(name = "jwt-auth")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Usuario encontrado",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = AppUserDTO.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Usuario no encontrado",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
        }
    )
    public Response getAppUserByUsername(
        @Parameter(description = "Username del usuario") @PathParam(
            "username"
        ) String username
    ) {
        try {
            Respuesta r = appUserService.getAppUserByUsername(username);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                    .entity(r.getMensaje())
                    .build();
            }
            AppUserDTO user = (AppUserDTO) r.getResultado("AppUser");
            return Response.ok(user).build();
        } catch (Exception ex) {
            Logger.getLogger(AppUserController.class.getName()).log(
                Level.SEVERE,
                null,
                ex
            );
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                .entity("Error obteniendo el usuario.")
                .build();
        }
    }

    @Secure
    @GET
    @Path("/appusers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene todos los usuarios")
    @SecurityRequirement(name = "jwt-auth")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Usuarios encontrados",
                content = @Content(mediaType = MediaType.APPLICATION_JSON)
            ),
            @ApiResponse(
                responseCode = "404",
                description = "No hay usuarios registrados",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
        }
    )
    public Response getAppUsers() {
        try {
            Respuesta r = appUserService.getAppUsers();
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                    .entity(r.getMensaje())
                    .build();
            }
            return Response.ok(
                new GenericEntity<List<AppUserDTO>>(
                    (List<AppUserDTO>) r.getResultado("AppUsers")
                ) {}
            ).build();
        } catch (Exception ex) {
            Logger.getLogger(AppUserController.class.getName()).log(
                Level.SEVERE,
                null,
                ex
            );
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                .entity("Error obteniendo los usuarios")
                .build();
        }
    }

    /**
     * Save/update user - NO @Secure for initial user creation
     * In production, you should add role-based security here
     */
    @POST
    @Path("/appuser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Guarda o actualiza un usuario")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Usuario guardado exitosamente",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = AppUserDTO.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Usuario no encontrado",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
        }
    )
    public Response guardarAppUser(AppUserDTO appUser) {
        try {
            Respuesta r = appUserService.guardarAppUser(appUser);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                    .entity(r.getMensaje())
                    .build();
            }
            AppUserDTO appUserDto = (AppUserDTO) r.getResultado("AppUser");
            return Response.ok(appUserDto).build();
        } catch (Exception ex) {
            Logger.getLogger(AppUserController.class.getName()).log(
                Level.SEVERE,
                null,
                ex
            );
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                .entity("Error guardando el usuario.")
                .build();
        }
    }

    @Secure
    @DELETE
    @Path("/appuser/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Elimina un usuario")
    @SecurityRequirement(name = "jwt-auth")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Usuario eliminado exitosamente"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Usuario no encontrado",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
        }
    )
    public Response eliminarAppUser(
        @Parameter(description = "ID del usuario") @PathParam("id") Long id
    ) {
        try {
            Respuesta r = appUserService.eliminarAppUser(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                    .entity(r.getMensaje())
                    .build();
            }
            return Response.ok().build();
        } catch (Exception ex) {
            Logger.getLogger(AppUserController.class.getName()).log(
                Level.SEVERE,
                null,
                ex
            );
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                .entity("Error eliminando el usuario.")
                .build();
        }
    }
}
