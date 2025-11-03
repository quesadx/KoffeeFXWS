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

import cr.ac.una.koffeefxws.model.CustomerOrderDTO;
import cr.ac.una.koffeefxws.service.CustomerOrderService;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;
import cr.ac.una.koffeefxws.util.Secure;

@Secure
@Path("/CustomerOrderController")
@Tag(name = "CustomerOrders", description = "Operations on customer orders")
@SecurityRequirement(name = "jwt-auth")
public class CustomerOrderController {

  @EJB CustomerOrderService customerOrderService;

  @Secure
  @GET
  @Path("/order/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets an order by ID")
  public Response getCustomerOrder(@Parameter(description = "Order ID") @PathParam("id") Long id) {
    try {
      Respuesta r = customerOrderService.getCustomerOrder(id);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      CustomerOrderDTO dto = (CustomerOrderDTO) r.getResultado("CustomerOrder");
      return Response.ok(dto).build();
    } catch (Exception ex) {
      Logger.getLogger(CustomerOrderController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting the order.")
          .build();
    }
  }

  @Secure
  @GET
  @Path("/orders")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets all orders")
  public Response getCustomerOrders() {
    try {
      Respuesta r = customerOrderService.getCustomerOrders();
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      return Response.ok(
              new GenericEntity<List<CustomerOrderDTO>>(
                  (List<CustomerOrderDTO>) r.getResultado("CustomerOrders")) {})
          .build();
    } catch (Exception ex) {
      Logger.getLogger(CustomerOrderController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting the orders")
          .build();
    }
  }

  @Secure
  @POST
  @Path("/order")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Creates or updates an order")
  public Response guardarCustomerOrder(CustomerOrderDTO dto) {
    try {
      Respuesta r = customerOrderService.guardarCustomerOrder(dto);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      CustomerOrderDTO saved = (CustomerOrderDTO) r.getResultado("CustomerOrder");
      return Response.ok(saved).build();
    } catch (Exception ex) {
      Logger.getLogger(CustomerOrderController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error saving the order.")
          .build();
    }
  }

  @Secure
  @DELETE
  @Path("/order/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Deletes an order")
  public Response eliminarCustomerOrder(
      @Parameter(description = "Order ID") @PathParam("id") Long id) {
    try {
      Respuesta r = customerOrderService.eliminarCustomerOrder(id);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      return Response.ok().build();
    } catch (Exception ex) {
      Logger.getLogger(CustomerOrderController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error deleting the order.")
          .build();
    }
  }
}
