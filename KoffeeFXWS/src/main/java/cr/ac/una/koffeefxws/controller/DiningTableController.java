package cr.ac.una.koffeefxws.controller;

import cr.ac.una.koffeefxws.model.DiningTableDTO;
import cr.ac.una.koffeefxws.service.DiningTableService;
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
@Path("/DiningTableController")
@Tag(name = "DiningTables", description = "Operaciones sobre mesas de comedor")
@SecurityRequirement(name = "jwt-auth")
public class DiningTableController {

    @EJB
    DiningTableService diningTableService;

    @GET
    @Path("/diningtable/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene una mesa de comedor por ID")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Mesa de comedor encontrada",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = DiningTableDTO.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Mesa de comedor no encontrada",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
        }
    )
    public Response getDiningTable(
        @Parameter(description = "ID de la mesa de comedor") @PathParam(
            "id"
        ) Long id
    ) {
        try {
            Respuesta r = diningTableService.getDiningTable(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                    .entity(r.getMensaje())
                    .build();
            }
            DiningTableDTO diningTable = (DiningTableDTO) r.getResultado(
                "DiningTable"
            );
            return Response.ok(diningTable).build();
        } catch (Exception ex) {
            Logger.getLogger(DiningTableController.class.getName()).log(
                Level.SEVERE,
                null,
                ex
            );
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                .entity("Error obteniendo la mesa de comedor.")
                .build();
        }
    }

    @GET
    @Path("/diningtables")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene todas las mesas de comedor")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Mesas de comedor encontradas",
                content = @Content(mediaType = MediaType.APPLICATION_JSON)
            ),
            @ApiResponse(
                responseCode = "404",
                description = "No hay mesas de comedor registradas",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
        }
    )
    public Response getDiningTables() {
        try {
            Respuesta r = diningTableService.getDiningTables();
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                    .entity(r.getMensaje())
                    .build();
            }
            return Response.ok(
                new GenericEntity<List<DiningTableDTO>>(
                    (List<DiningTableDTO>) r.getResultado("DiningTables")
                ) {}
            ).build();
        } catch (Exception ex) {
            Logger.getLogger(DiningTableController.class.getName()).log(
                Level.SEVERE,
                null,
                ex
            );
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                .entity("Error obteniendo las mesas de comedor")
                .build();
        }
    }

    @POST
    @Path("/diningtable")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Guarda o actualiza una mesa de comedor")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Mesa de comedor guardada exitosamente",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = DiningTableDTO.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Mesa de comedor no encontrada",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
        }
    )
    public Response guardarDiningTable(DiningTableDTO diningTable) {
        try {
            Respuesta r = diningTableService.guardarDiningTable(diningTable);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                    .entity(r.getMensaje())
                    .build();
            }
            DiningTableDTO diningTableDto = (DiningTableDTO) r.getResultado(
                "DiningTable"
            );
            return Response.ok(diningTableDto).build();
        } catch (Exception ex) {
            Logger.getLogger(DiningTableController.class.getName()).log(
                Level.SEVERE,
                null,
                ex
            );
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                .entity("Error guardando la mesa de comedor.")
                .build();
        }
    }

    @DELETE
    @Path("/diningtable/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Elimina una mesa de comedor")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Mesa de comedor eliminada exitosamente"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Mesa de comedor no encontrada",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
        }
    )
    public Response eliminarDiningTable(
        @Parameter(description = "ID de la mesa de comedor") @PathParam(
            "id"
        ) Long id
    ) {
        try {
            Respuesta r = diningTableService.eliminarDiningTable(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                    .entity(r.getMensaje())
                    .build();
            }
            return Response.ok().build();
        } catch (Exception ex) {
            Logger.getLogger(DiningTableController.class.getName()).log(
                Level.SEVERE,
                null,
                ex
            );
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                .entity("Error eliminando la mesa de comedor.")
                .build();
        }
    }
}
