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

import cr.ac.una.koffeefxws.model.ProductDTO;
import cr.ac.una.koffeefxws.service.ProductService;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;
import cr.ac.una.koffeefxws.util.Secure;

/**
 * @author quesadx
 */
@Secure
@Path("/ProductController")
@Tag(name = "Products", description = "Operaciones sobre productos")
@SecurityRequirement(name = "jwt-auth")
public class ProductController {

    @EJB ProductService productService;

    @GET
    @Path("/product/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene un producto por ID")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Producto encontrado",
                content =
                        @Content(
                                mediaType = MediaType.APPLICATION_JSON,
                                schema = @Schema(implementation = ProductDTO.class))),
        @ApiResponse(
                responseCode = "404",
                description = "Producto no encontrado",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    })
    public Response getProduct(
            @Parameter(description = "ID del producto") @PathParam("id") Long id) {
        try {
            Respuesta r = productService.getProduct(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje())
                        .build();
            }
            ProductDTO product = (ProductDTO) r.getResultado("Product");
            return Response.ok(product).build();
        } catch (Exception ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error obteniendo el producto.")
                    .build();
        }
    }

    @GET
    @Path("/products")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene todos los productos")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Productos encontrados",
                content = @Content(mediaType = MediaType.APPLICATION_JSON)),
        @ApiResponse(
                responseCode = "404",
                description = "No hay productos registrados",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    })
    public Response getProducts() {
        try {
            Respuesta r = productService.getProducts();
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje())
                        .build();
            }
            return Response.ok(
                            new GenericEntity<List<ProductDTO>>(
                                    (List<ProductDTO>) r.getResultado("Products")) {})
                    .build();
        } catch (Exception ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error obteniendo los productos")
                    .build();
        }
    }

    @GET
    @Path("/products/active")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene todos los productos activos")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Productos encontrados",
                content = @Content(mediaType = MediaType.APPLICATION_JSON)),
        @ApiResponse(
                responseCode = "404",
                description = "No hay productos activos",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    })
    public Response getActiveProducts() {
        try {
            Respuesta r = productService.getActiveProducts();
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje())
                        .build();
            }
            return Response.ok(
                            new GenericEntity<List<ProductDTO>>(
                                    (List<ProductDTO>) r.getResultado("Products")) {})
                    .build();
        } catch (Exception ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error obteniendo los productos activos")
                    .build();
        }
    }

    @POST
    @Path("/product")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Guarda o actualiza un producto")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Producto guardado exitosamente",
                content =
                        @Content(
                                mediaType = MediaType.APPLICATION_JSON,
                                schema = @Schema(implementation = ProductDTO.class))),
        @ApiResponse(
                responseCode = "404",
                description = "Producto no encontrado",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    })
    public Response guardarProduct(ProductDTO product) {
        try {
            Respuesta r = productService.guardarProduct(product);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje())
                        .build();
            }
            ProductDTO productDto = (ProductDTO) r.getResultado("Product");
            return Response.ok(productDto).build();
        } catch (Exception ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error guardando el producto.")
                    .build();
        }
    }

    @DELETE
    @Path("/product/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Elimina un producto")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente"),
        @ApiResponse(
                responseCode = "404",
                description = "Producto no encontrado",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    })
    public Response eliminarProduct(
            @Parameter(description = "ID del producto") @PathParam("id") Long id) {
        try {
            Respuesta r = productService.eliminarProduct(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje())
                        .build();
            }
            return Response.ok().build();
        } catch (Exception ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error eliminando el producto.")
                    .build();
        }
    }
}
