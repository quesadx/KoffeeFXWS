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

import cr.ac.una.koffeefxws.model.InvoiceDTO;
import cr.ac.una.koffeefxws.service.InvoiceService;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;
import cr.ac.una.koffeefxws.util.Secure;

/**
 * @author quesadx
 */
@Secure
@Path("/InvoiceController")
@Tag(name = "Invoices", description = "Operations on invoices")
@SecurityRequirement(name = "jwt-auth")
public class InvoiceController {

  @EJB InvoiceService invoiceService;

  @Secure
  @GET
  @Path("/invoice/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets an invoice by ID")
  public Response getInvoice(@Parameter(description = "Invoice ID") @PathParam("id") Long id) {
    try {
      Respuesta r = invoiceService.getInvoice(id);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      InvoiceDTO dto = (InvoiceDTO) r.getResultado("Invoice");
      return Response.ok(dto).build();
    } catch (Exception ex) {
      Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting the invoice.")
          .build();
    }
  }

  @Secure
  @GET
  @Path("/invoice/number/{num}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets an invoice by number")
  public Response getInvoiceByNumber(
      @Parameter(description = "Invoice number") @PathParam("num") String num) {
    try {
      Respuesta r = invoiceService.getInvoiceByNumber(num);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      InvoiceDTO dto = (InvoiceDTO) r.getResultado("Invoice");
      return Response.ok(dto).build();
    } catch (Exception ex) {
      Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting the invoice.")
          .build();
    }
  }

  @Secure
  @GET
  @Path("/invoice/order/{orderId}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets an invoice by order ID")
  public Response getInvoiceByOrder(
      @Parameter(description = "Order ID") @PathParam("orderId") Long orderId) {
    try {
      Respuesta r = invoiceService.getInvoiceByOrderId(orderId);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      InvoiceDTO dto = (InvoiceDTO) r.getResultado("Invoice");
      return Response.ok(dto).build();
    } catch (Exception ex) {
      Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting the invoice by order.")
          .build();
    }
  }

  @Secure
  @GET
  @Path("/invoices")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Gets all invoices")
  public Response getInvoices() {
    try {
      Respuesta r = invoiceService.getInvoices();
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      return Response.ok(
              new GenericEntity<List<InvoiceDTO>>((List<InvoiceDTO>) r.getResultado("Invoices")) {})
          .build();
    } catch (Exception ex) {
      Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error getting invoices")
          .build();
    }
  }

  @Secure
  @POST
  @Path("/invoice")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Creates or updates an invoice")
  public Response guardarInvoice(InvoiceDTO dto) {
    try {
      Respuesta r = invoiceService.guardarInvoice(dto);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      InvoiceDTO saved = (InvoiceDTO) r.getResultado("Invoice");
      return Response.ok(saved).build();
    } catch (Exception ex) {
      Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error saving the invoice.")
          .build();
    }
  }

  @Secure
  @DELETE
  @Path("/invoice/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Deletes an invoice")
  public Response eliminarInvoice(@Parameter(description = "Invoice ID") @PathParam("id") Long id) {
    try {
      Respuesta r = invoiceService.eliminarInvoice(id);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      return Response.ok().build();
    } catch (Exception ex) {
      Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error deleting the invoice.")
          .build();
    }
  }
}
