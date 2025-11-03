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

import cr.ac.una.koffeefxws.model.ProductGroupDTO;
import cr.ac.una.koffeefxws.service.ProductGroupService;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;
import cr.ac.una.koffeefxws.util.Secure;

/**
 * @author quesadx
 */
@Secure
@Path("/ProductGroupController")
@Tag(name = "ProductGroups", description = "Operations on product groups")
@SecurityRequirement(name = "jwt-auth")
public class ProductGroupController {

  @EJB ProductGroupService productGroupService;

  @Secure
  @GET
  @Path("/productgroup/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets a product group by ID")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Product group found",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = ProductGroupDTO.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Product group not found",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response getProductGroup(
      @Parameter(description = "Product group ID") @PathParam("id") Long id) {
    try {
      Respuesta r = productGroupService.getProductGroup(id);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      ProductGroupDTO productGroup = (ProductGroupDTO) r.getResultado("ProductGroup");
      return Response.ok(productGroup).build();
    } catch (Exception ex) {
      Logger.getLogger(ProductGroupController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting the product group.")
          .build();
    }
  }

  @Secure
  @GET
  @Path("/productgroups")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets all product groups")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Product groups found",
        content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    @ApiResponse(
        responseCode = "404",
        description = "No product groups registered",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response getProductGroups() {
    try {
      Respuesta r = productGroupService.getProductGroups();
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      return Response.ok(
              new GenericEntity<List<ProductGroupDTO>>(
                  (List<ProductGroupDTO>) r.getResultado("ProductGroups")) {})
          .build();
    } catch (Exception ex) {
      Logger.getLogger(ProductGroupController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting product groups")
          .build();
    }
  }

  @Secure
  @POST
  @Path("/productgroup")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Creates or updates a product group")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Product group saved successfully",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = ProductGroupDTO.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Product group not found",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response guardarProductGroup(ProductGroupDTO productGroup) {
    try {
      Respuesta r = productGroupService.guardarProductGroup(productGroup);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      ProductGroupDTO productGroupDto = (ProductGroupDTO) r.getResultado("ProductGroup");
      return Response.ok(productGroupDto).build();
    } catch (Exception ex) {
      Logger.getLogger(ProductGroupController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error saving the product group.")
          .build();
    }
  }

  @Secure
  @DELETE
  @Path("/productgroup/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Deletes a product group")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Product group deleted successfully"),
    @ApiResponse(
        responseCode = "404",
        description = "Product group not found",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    @ApiResponse(
        responseCode = "500",
        description = "Internal error",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
  })
  public Response eliminarProductGroup(
      @Parameter(description = "Product group ID") @PathParam("id") Long id) {
    try {
      Respuesta r = productGroupService.eliminarProductGroup(id);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      return Response.ok().build();
    } catch (Exception ex) {
      Logger.getLogger(ProductGroupController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error deleting the product group.")
          .build();
    }
  }
}
