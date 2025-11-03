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
@Tag(name = "Products", description = "Operations on products")
@SecurityRequirement(name = "jwt-auth")
public class ProductController {

  @EJB ProductService productService;

  @Secure
  @GET
  @Path("/product/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets a product by ID")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Product found",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = ProductDTO.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Product not found",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response getProduct(@Parameter(description = "Product ID") @PathParam("id") Long id) {
    try {
      Respuesta r = productService.getProduct(id);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      ProductDTO product = (ProductDTO) r.getResultado("Product");
      return Response.ok(product).build();
    } catch (Exception ex) {
      Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting the product.")
          .build();
    }
  }

  @Secure
  @GET
  @Path("/products")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets all products")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Products found",
        content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    @ApiResponse(
        responseCode = "404",
        description = "No products registered",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response getProducts() {
    try {
      Respuesta r = productService.getProducts();
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      return Response.ok(
              new GenericEntity<List<ProductDTO>>((List<ProductDTO>) r.getResultado("Products")) {})
          .build();
    } catch (Exception ex) {
      Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting products")
          .build();
    }
  }

  @Secure
  @GET
  @Path("/products/active")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets all active products")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Products found",
        content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    @ApiResponse(
        responseCode = "404",
        description = "No active products",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response getActiveProducts() {
    try {
      Respuesta r = productService.getActiveProducts();
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      return Response.ok(
              new GenericEntity<List<ProductDTO>>((List<ProductDTO>) r.getResultado("Products")) {})
          .build();
    } catch (Exception ex) {
      Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting active products")
          .build();
    }
  }

  @Secure
  @POST
  @Path("/product")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Creates or updates a product")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Product saved successfully",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = ProductDTO.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Product not found",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response guardarProduct(ProductDTO product) {
    try {
      Respuesta r = productService.guardarProduct(product);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      ProductDTO productDto = (ProductDTO) r.getResultado("Product");
      return Response.ok(productDto).build();
    } catch (Exception ex) {
      Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error saving the product.")
          .build();
    }
  }

  @Secure
  @DELETE
  @Path("/product/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Deletes a product")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
    @ApiResponse(
        responseCode = "404",
        description = "Product not found",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response eliminarProduct(@Parameter(description = "Product ID") @PathParam("id") Long id) {
    try {
      Respuesta r = productService.eliminarProduct(id);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      return Response.ok().build();
    } catch (Exception ex) {
      Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error deleting the product.")
          .build();
    }
  }
}
