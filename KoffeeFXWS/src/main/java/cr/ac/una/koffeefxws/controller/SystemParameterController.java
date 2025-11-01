package cr.ac.una.koffeefxws.controller;

import cr.ac.una.koffeefxws.model.SystemParameterDTO;
import cr.ac.una.koffeefxws.service.SystemParameterService;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
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
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author quesadx
 */
@Secure
@Path("/SystemParameterController")
@Tag(
    name = "SystemParameters",
    description = "Operaciones sobre parámetros del sistema"
)
@SecurityRequirement(name = "jwt-auth")
public class SystemParameterController {

    @EJB
    SystemParameterService systemParameterService;

    @GET
    @Path("/systemparameter/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene un parámetro del sistema por ID")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Parámetro del sistema encontrado",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = SystemParameterDTO.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Parámetro del sistema no encontrado",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
        }
    )
    public Response getSystemParameter(
        @Parameter(description = "ID del parámetro del sistema") @PathParam(
            "id"
        ) Long id
    ) {
        try {
            Respuesta r = systemParameterService.getSystemParameter(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                    .entity(r.getMensaje())
                    .build();
            }
            SystemParameterDTO systemParameter =
                (SystemParameterDTO) r.getResultado("SystemParameter");
            return Response.ok(systemParameter).build();
        } catch (Exception ex) {
            Logger.getLogger(SystemParameterController.class.getName()).log(
                Level.SEVERE,
                null,
                ex
            );
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                .entity("Error obteniendo el parámetro del sistema.")
                .build();
        }
    }

    @GET
    @Path("/systemparameter/name/{paramName}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene un parámetro del sistema por nombre")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Parámetro del sistema encontrado",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = SystemParameterDTO.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Parámetro del sistema no encontrado",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
        }
    )
    public Response getSystemParameterByName(
        @Parameter(description = "Nombre del parámetro") @PathParam(
            "paramName"
        ) String paramName
    ) {
        try {
            Respuesta r = systemParameterService.getSystemParameterByName(
                paramName
            );
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                    .entity(r.getMensaje())
                    .build();
            }
            SystemParameterDTO systemParameter =
                (SystemParameterDTO) r.getResultado("SystemParameter");
            return Response.ok(systemParameter).build();
        } catch (Exception ex) {
            Logger.getLogger(SystemParameterController.class.getName()).log(
                Level.SEVERE,
                null,
                ex
            );
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                .entity("Error obteniendo el parámetro del sistema.")
                .build();
        }
    }

    @GET
    @Path("/systemparameters")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene todos los parámetros del sistema")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Parámetros del sistema encontrados",
                content = @Content(mediaType = MediaType.APPLICATION_JSON)
            ),
            @ApiResponse(
                responseCode = "404",
                description = "No hay parámetros del sistema registrados",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
        }
    )
    public Response getSystemParameters() {
        try {
            Respuesta r = systemParameterService.getSystemParameters();
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                    .entity(r.getMensaje())
                    .build();
            }
            return Response.ok(
                new GenericEntity<List<SystemParameterDTO>>(
                    (List<SystemParameterDTO>) r.getResultado(
                        "SystemParameters"
                    )
                ) {}
            ).build();
        } catch (Exception ex) {
            Logger.getLogger(SystemParameterController.class.getName()).log(
                Level.SEVERE,
                null,
                ex
            );
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                .entity("Error obteniendo los parámetros del sistema")
                .build();
        }
    }

    @POST
    @Path("/systemparameter")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Guarda o actualiza un parámetro del sistema")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Parámetro del sistema guardado exitosamente",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = SystemParameterDTO.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Parámetro del sistema no encontrado",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
        }
    )
    public Response guardarSystemParameter(SystemParameterDTO systemParameter) {
        try {
            Respuesta r = systemParameterService.guardarSystemParameter(
                systemParameter
            );
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                    .entity(r.getMensaje())
                    .build();
            }
            SystemParameterDTO systemParameterDto =
                (SystemParameterDTO) r.getResultado("SystemParameter");
            return Response.ok(systemParameterDto).build();
        } catch (Exception ex) {
            Logger.getLogger(SystemParameterController.class.getName()).log(
                Level.SEVERE,
                null,
                ex
            );
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                .entity("Error guardando el parámetro del sistema.")
                .build();
        }
    }

    @DELETE
    @Path("/systemparameter/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Elimina un parámetro del sistema")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Parámetro del sistema eliminado exitosamente"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Parámetro del sistema no encontrado",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
        }
    )
    public Response eliminarSystemParameter(
        @Parameter(description = "ID del parámetro del sistema") @PathParam(
            "id"
        ) Long id
    ) {
        try {
            Respuesta r = systemParameterService.eliminarSystemParameter(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                    .entity(r.getMensaje())
                    .build();
            }
            return Response.ok().build();
        } catch (Exception ex) {
            Logger.getLogger(SystemParameterController.class.getName()).log(
                Level.SEVERE,
                null,
                ex
            );
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                .entity("Error eliminando el parámetro del sistema.")
                .build();
        }
    }
}
