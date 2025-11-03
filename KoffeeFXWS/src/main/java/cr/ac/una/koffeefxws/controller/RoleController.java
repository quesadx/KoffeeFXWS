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

import cr.ac.una.koffeefxws.model.RoleDTO;
import cr.ac.una.koffeefxws.service.RoleService;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;
import cr.ac.una.koffeefxws.util.Secure;

/**
 * @author quesadx
 */
@Deprecated
@Secure
@Path("/DEPRECATED_RoleController") // Ya no se usa
@Tag(
        name = "Roles (DEPRECATED)",
        description = "Controlador de roles en desuso; utilice USER_ROLE en AppUser")
@SecurityRequirement(name = "jwt-auth")
public class RoleController {

    @EJB RoleService roleService;

    @GET
    @Path("/role/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene un rol por ID")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Rol encontrado",
                content =
                        @Content(
                                mediaType = MediaType.APPLICATION_JSON,
                                schema = @Schema(implementation = RoleDTO.class))),
        @ApiResponse(
                responseCode = "404",
                description = "Rol no encontrado",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    })
    public Response getRole(@Parameter(description = "ID del rol") @PathParam("id") Long id) {
        try {
            Respuesta r = roleService.getRole(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje())
                        .build();
            }
            RoleDTO role = (RoleDTO) r.getResultado("Role");
            return Response.ok(role).build();
        } catch (Exception ex) {
            Logger.getLogger(RoleController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error obteniendo el rol.")
                    .build();
        }
    }

    @GET
    @Path("/role/code/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene un rol por código")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Rol encontrado",
                content =
                        @Content(
                                mediaType = MediaType.APPLICATION_JSON,
                                schema = @Schema(implementation = RoleDTO.class))),
        @ApiResponse(
                responseCode = "404",
                description = "Rol no encontrado",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    })
    public Response getRoleByCode(
            @Parameter(description = "Código del rol") @PathParam("code") String code) {
        try {
            Respuesta r = roleService.getRoleByCode(code);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje())
                        .build();
            }
            RoleDTO role = (RoleDTO) r.getResultado("Role");
            return Response.ok(role).build();
        } catch (Exception ex) {
            Logger.getLogger(RoleController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error obteniendo el rol.")
                    .build();
        }
    }

    @GET
    @Path("/roles")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene todos los roles")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Roles encontrados",
                content = @Content(mediaType = MediaType.APPLICATION_JSON)),
        @ApiResponse(
                responseCode = "404",
                description = "No hay roles registrados",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    })
    public Response getRoles() {
        try {
            Respuesta r = roleService.getRoles();
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje())
                        .build();
            }
            return Response.ok(
                            new GenericEntity<List<RoleDTO>>(
                                    (List<RoleDTO>) r.getResultado("Roles")) {})
                    .build();
        } catch (Exception ex) {
            Logger.getLogger(RoleController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error obteniendo los roles")
                    .build();
        }
    }

    @POST
    @Path("/role")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Guarda o actualiza un rol")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Rol guardado exitosamente",
                content =
                        @Content(
                                mediaType = MediaType.APPLICATION_JSON,
                                schema = @Schema(implementation = RoleDTO.class))),
        @ApiResponse(
                responseCode = "404",
                description = "Rol no encontrado",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    })
    public Response guardarRole(RoleDTO role) {
        try {
            Respuesta r = roleService.guardarRole(role);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje())
                        .build();
            }
            RoleDTO roleDto = (RoleDTO) r.getResultado("Role");
            return Response.ok(roleDto).build();
        } catch (Exception ex) {
            Logger.getLogger(RoleController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error guardando el rol.")
                    .build();
        }
    }

    @DELETE
    @Path("/role/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Elimina un rol")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Rol eliminado exitosamente"),
        @ApiResponse(
                responseCode = "404",
                description = "Rol no encontrado",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    })
    public Response eliminarRole(@Parameter(description = "ID del rol") @PathParam("id") Long id) {
        try {
            Respuesta r = roleService.eliminarRole(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje())
                        .build();
            }
            return Response.ok().build();
        } catch (Exception ex) {
            Logger.getLogger(RoleController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error eliminando el rol.")
                    .build();
        }
    }
}
