package cr.ac.una.koffeefxws.controller;

import cr.ac.una.koffeefxws.model.DiningAreaDTO;
import cr.ac.una.koffeefxws.service.DiningAreaService;
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
@Path("/DiningAreaController")
@Tag(name = "DiningAreas", description = "Operaciones sobre áreas de comedor")
@SecurityRequirement(name = "jwt-auth")
public class DiningAreaController {

    @EJB
    DiningAreaService diningAreaService;

    @GET
    @Path("/diningarea/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene un área de comedor por ID")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Área de comedor encontrada",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = DiningAreaDTO.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Área de comedor no encontrada",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
        }
    )
    public Response getDiningArea(
        @Parameter(description = "ID del área de comedor") @PathParam(
            "id"
        ) Long id
    ) {
        try {
            Respuesta r = diningAreaService.getDiningArea(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                    .entity(r.getMensaje())
                    .build();
            }
            DiningAreaDTO diningArea = (DiningAreaDTO) r.getResultado(
                "DiningArea"
            );
            return Response.ok(diningArea).build();
        } catch (Exception ex) {
            Logger.getLogger(DiningAreaController.class.getName()).log(
                Level.SEVERE,
                null,
                ex
            );
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                .entity("Error obteniendo el área de comedor.")
                .build();
        }
    }

    @GET
    @Path("/diningareas")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene todas las áreas de comedor")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Áreas de comedor encontradas",
                content = @Content(mediaType = MediaType.APPLICATION_JSON)
            ),
            @ApiResponse(
                responseCode = "404",
                description = "No hay áreas de comedor registradas",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
        }
    )
    public Response getDiningAreas() {
        try {
            Respuesta r = diningAreaService.getDiningAreas();
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                    .entity(r.getMensaje())
                    .build();
            }
            return Response.ok(
                new GenericEntity<List<DiningAreaDTO>>(
                    (List<DiningAreaDTO>) r.getResultado("DiningAreas")
                ) {}
            ).build();
        } catch (Exception ex) {
            Logger.getLogger(DiningAreaController.class.getName()).log(
                Level.SEVERE,
                null,
                ex
            );
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                .entity("Error obteniendo las áreas de comedor")
                .build();
        }
    }

    @GET
    @Path("/diningareas/active")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene todas las áreas de comedor activas")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Áreas de comedor activas encontradas",
                content = @Content(mediaType = MediaType.APPLICATION_JSON)
            ),
            @ApiResponse(
                responseCode = "404",
                description = "No hay áreas de comedor activas",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
        }
    )
    public Response getActiveDiningAreas() {
        try {
            Respuesta r = diningAreaService.getActiveDiningAreas();
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                    .entity(r.getMensaje())
                    .build();
            }
            return Response.ok(
                new GenericEntity<List<DiningAreaDTO>>(
                    (List<DiningAreaDTO>) r.getResultado("DiningAreas")
                ) {}
            ).build();
        } catch (Exception ex) {
            Logger.getLogger(DiningAreaController.class.getName()).log(
                Level.SEVERE,
                null,
                ex
            );
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                .entity("Error obteniendo las áreas de comedor activas")
                .build();
        }
    }

    @POST
    @Path("/diningarea")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Guarda o actualiza un área de comedor")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Área de comedor guardada exitosamente",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = DiningAreaDTO.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Área de comedor no encontrada",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
        }
    )
    public Response guardarDiningArea(DiningAreaDTO diningArea) {
        try {
            Respuesta r = diningAreaService.guardarDiningArea(diningArea);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                    .entity(r.getMensaje())
                    .build();
            }
            DiningAreaDTO diningAreaDto = (DiningAreaDTO) r.getResultado(
                "DiningArea"
            );
            return Response.ok(diningAreaDto).build();
        } catch (Exception ex) {
            Logger.getLogger(DiningAreaController.class.getName()).log(
                Level.SEVERE,
                null,
                ex
            );
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                .entity("Error guardando el área de comedor.")
                .build();
        }
    }

    @DELETE
    @Path("/diningarea/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Elimina un área de comedor")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Área de comedor eliminada exitosamente"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Área de comedor no encontrada",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
        }
    )
    public Response eliminarDiningArea(
        @Parameter(description = "ID del área de comedor") @PathParam(
            "id"
        ) Long id
    ) {
        try {
            Respuesta r = diningAreaService.eliminarDiningArea(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                    .entity(r.getMensaje())
                    .build();
            }
            return Response.ok().build();
        } catch (Exception ex) {
            Logger.getLogger(DiningAreaController.class.getName()).log(
                Level.SEVERE,
                null,
                ex
            );
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                .entity("Error eliminando el área de comedor.")
                .build();
        }
    }
}
