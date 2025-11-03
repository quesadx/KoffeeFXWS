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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import cr.ac.una.koffeefxws.model.OrderItemDTO;
import cr.ac.una.koffeefxws.service.OrderItemService;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;
import cr.ac.una.koffeefxws.util.Secure;

/**
 * @author quesadx
 */
@Secure
@Path("/OrderItemController")
@Tag(name = "OrderItems", description = "Operaciones sobre items de pedido")
@SecurityRequirement(name = "jwt-auth")
public class OrderItemController {

  @EJB OrderItemService orderItemService;

  @GET
  @Path("/orderitem/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Obtiene un item por ID")
  public Response getOrderItem(@Parameter(description = "ID del item") @PathParam("id") Long id) {
    try {
      Respuesta r = orderItemService.getOrderItem(id);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      OrderItemDTO dto = (OrderItemDTO) r.getResultado("OrderItem");
      return Response.ok(dto).build();
    } catch (Exception ex) {
      Logger.getLogger(OrderItemController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error obteniendo el item.")
          .build();
    }
  }

  @GET
  @Path("/orderitems")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Obtiene todos los items")
  public Response getOrderItems() {
    try {
      Respuesta r = orderItemService.getOrderItems();
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      return Response.ok(
              new GenericEntity<List<OrderItemDTO>>(
                  (List<OrderItemDTO>) r.getResultado("OrderItems")) {})
          .build();
    } catch (Exception ex) {
      Logger.getLogger(OrderItemController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error obteniendo los items")
          .build();
    }
  }

  @POST
  @Path("/orderitem")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Guarda o actualiza un item")
  public Response guardarOrderItem(OrderItemDTO dto) {
    try {
      Respuesta r = orderItemService.guardarOrderItem(dto);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      OrderItemDTO saved = (OrderItemDTO) r.getResultado("OrderItem");
      return Response.ok(saved).build();
    } catch (Exception ex) {
      Logger.getLogger(OrderItemController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error guardando el item.")
          .build();
    }
  }

  @DELETE
  @Path("/orderitem/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Elimina un item")
  public Response eliminarOrderItem(
      @Parameter(description = "ID del item") @PathParam("id") Long id) {
    try {
      Respuesta r = orderItemService.eliminarOrderItem(id);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      return Response.ok().build();
    } catch (Exception ex) {
      Logger.getLogger(OrderItemController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error eliminando el item.")
          .build();
    }
  }
}
