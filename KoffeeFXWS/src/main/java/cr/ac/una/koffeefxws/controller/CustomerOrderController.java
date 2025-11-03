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
@Tag(name = "CustomerOrders", description = "Operaciones sobre pedidos")
@SecurityRequirement(name = "jwt-auth")
public class CustomerOrderController {

    @EJB CustomerOrderService customerOrderService;

    @GET
    @Path("/order/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene un pedido por ID")
    public Response getCustomerOrder(
            @Parameter(description = "ID del pedido") @PathParam("id") Long id) {
        try {
            Respuesta r = customerOrderService.getCustomerOrder(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje())
                        .build();
            }
            CustomerOrderDTO dto = (CustomerOrderDTO) r.getResultado("CustomerOrder");
            return Response.ok(dto).build();
        } catch (Exception ex) {
            Logger.getLogger(CustomerOrderController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error obteniendo el pedido.")
                    .build();
        }
    }

    @GET
    @Path("/orders")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene todos los pedidos")
    public Response getCustomerOrders() {
        try {
            Respuesta r = customerOrderService.getCustomerOrders();
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje())
                        .build();
            }
            return Response.ok(
                            new GenericEntity<List<CustomerOrderDTO>>(
                                    (List<CustomerOrderDTO>) r.getResultado("CustomerOrders")) {})
                    .build();
        } catch (Exception ex) {
            Logger.getLogger(CustomerOrderController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error obteniendo los pedidos")
                    .build();
        }
    }

    @POST
    @Path("/order")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Guarda o actualiza un pedido")
    public Response guardarCustomerOrder(CustomerOrderDTO dto) {
        try {
            Respuesta r = customerOrderService.guardarCustomerOrder(dto);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje())
                        .build();
            }
            CustomerOrderDTO saved = (CustomerOrderDTO) r.getResultado("CustomerOrder");
            return Response.ok(saved).build();
        } catch (Exception ex) {
            Logger.getLogger(CustomerOrderController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error guardando el pedido.")
                    .build();
        }
    }

    @DELETE
    @Path("/order/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Elimina un pedido")
    public Response eliminarCustomerOrder(
            @Parameter(description = "ID del pedido") @PathParam("id") Long id) {
        try {
            Respuesta r = customerOrderService.eliminarCustomerOrder(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje())
                        .build();
            }
            return Response.ok().build();
        } catch (Exception ex) {
            Logger.getLogger(CustomerOrderController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error eliminando el pedido.")
                    .build();
        }
    }
}
