package cr.ac.una.koffeefxws.controller;

import cr.ac.una.koffeefxws.model.CustomerDTO;
import cr.ac.una.koffeefxws.service.CustomerService;
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
@Path("/CustomerController")
@Tag(name = "Customers", description = "Operaciones sobre clientes")
@SecurityRequirement(name = "jwt-auth")
public class CustomerController {

    @EJB
    CustomerService customerService;

    @GET
    @Path("/customer/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene un cliente por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CustomerDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(responseCode = "500", description = "Error interno", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response getCustomer(
            @Parameter(description = "ID del cliente")
            @PathParam("id") Long id) {
        try {
            Respuesta r = customerService.getCustomer(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje()).build();
            }
            CustomerDTO customer = (CustomerDTO) r.getResultado("Customer");
            return Response.ok(customer).build();
        } catch (Exception ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error obteniendo el cliente.").build();
        }
    }

    @GET
    @Path("/customer/email/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene un cliente por email")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CustomerDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(responseCode = "500", description = "Error interno", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response getCustomerByEmail(
            @Parameter(description = "Email del cliente")
            @PathParam("email") String email) {
        try {
            Respuesta r = customerService.getCustomerByEmail(email);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje()).build();
            }
            CustomerDTO customer = (CustomerDTO) r.getResultado("Customer");
            return Response.ok(customer).build();
        } catch (Exception ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error obteniendo el cliente.").build();
        }
    }

    @GET
    @Path("/customers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene todos los clientes")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Clientes encontrados", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
        @ApiResponse(responseCode = "404", description = "No hay clientes registrados", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(responseCode = "500", description = "Error interno", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response getCustomers() {
        try {
            Respuesta r = customerService.getCustomers();
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje()).build();
            }
            return Response.ok(new GenericEntity<List<CustomerDTO>>((List<CustomerDTO>) r.getResultado("Customers")) {
            }).build();
        } catch (Exception ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error obteniendo los clientes").build();
        }
    }

    @POST
    @Path("/customer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Guarda o actualiza un cliente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente guardado exitosamente", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CustomerDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(responseCode = "500", description = "Error interno", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response guardarCustomer(CustomerDTO customer) {
        try {
            Respuesta r = customerService.guardarCustomer(customer);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje()).build();
            }
            CustomerDTO customerDto = (CustomerDTO) r.getResultado("Customer");
            return Response.ok(customerDto).build();
        } catch (Exception ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error guardando el cliente.").build();
        }
    }

    @DELETE
    @Path("/customer/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Elimina un cliente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(responseCode = "500", description = "Error interno", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response eliminarCustomer(
            @Parameter(description = "ID del cliente")
            @PathParam("id") Long id) {
        try {
            Respuesta r = customerService.eliminarCustomer(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue())
                        .entity(r.getMensaje()).build();
            }
            return Response.ok().build();
        } catch (Exception ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error eliminando el cliente.").build();
        }
    }
}
