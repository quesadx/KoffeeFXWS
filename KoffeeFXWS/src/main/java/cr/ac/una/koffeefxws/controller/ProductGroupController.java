package cr.ac.una.koffeefxws.controller;

import cr.ac.una.koffeefxws.model.ProductGroupDTO;
import cr.ac.una.koffeefxws.service.ProductGroupService;
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
@Path("/ProductGroupController")
@Tag(name = "ProductGroups", description = "Operaciones sobre grupos de productos")
@SecurityRequirement(name = "jwt-auth")
public class ProductGroupController {

    @EJB
    ProductGroupService productGroupService;

    @GET
    @Path("/productgroup/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene un grupo de productos por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Grupo de productos encontrado", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ProductGroupDTO.class))),
        @ApiResponse(responseCode = "404", description = "Grupo de productos no encontrado", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(responseCode = "500", description = "Error interno", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response getProductGroup(
            @Parameter(description = "ID del grupo de productos")
            @PathParam("id") Long id) {
        try {
            Respuesta r = productGroupService.getProductGroup(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje()).build();
            }
            ProductGroupDTO productGroup = (ProductGroupDTO) r.getResultado("ProductGroup");
            return Response.ok(productGroup).build();
        } catch (Exception ex) {
            Logger.getLogger(ProductGroupController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error obteniendo el grupo de productos.").build();
        }
    }

    @GET
    @Path("/productgroups")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene todos los grupos de productos")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Grupos de productos encontrados", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
        @ApiResponse(responseCode = "404", description = "No hay grupos de productos registrados", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(responseCode = "500", description = "Error interno", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response getProductGroups() {
        try {
            Respuesta r = productGroupService.getProductGroups();
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje()).build();
            }
            return Response.ok(new GenericEntity<List<ProductGroupDTO>>((List<ProductGroupDTO>) r.getResultado("ProductGroups")) {
            }).build();
        } catch (Exception ex) {
            Logger.getLogger(ProductGroupController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error obteniendo los grupos de productos").build();
        }
    }

    @POST
    @Path("/productgroup")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Guarda o actualiza un grupo de productos")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Grupo de productos guardado exitosamente", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ProductGroupDTO.class))),
        @ApiResponse(responseCode = "404", description = "Grupo de productos no encontrado", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(responseCode = "500", description = "Error interno", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response guardarProductGroup(ProductGroupDTO productGroup) {
        try {
            Respuesta r = productGroupService.guardarProductGroup(productGroup);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje()).build();
            }
            ProductGroupDTO productGroupDto = (ProductGroupDTO) r.getResultado("ProductGroup");
            return Response.ok(productGroupDto).build();
        } catch (Exception ex) {
            Logger.getLogger(ProductGroupController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error guardando el grupo de productos.").build();
        }
    }

    @DELETE
    @Path("/productgroup/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Elimina un grupo de productos")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Grupo de productos eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Grupo de productos no encontrado", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(responseCode = "500", description = "Error interno", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response eliminarProductGroup(
            @Parameter(description = "ID del grupo de productos")
            @PathParam("id") Long id) {
        try {
            Respuesta r = productGroupService.eliminarProductGroup(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje()).build();
            }
            return Response.ok().build();
        } catch (Exception ex) {
            Logger.getLogger(ProductGroupController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error eliminando el grupo de productos.").build();
        }
    }
}
