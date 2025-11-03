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
@Tag(name = "Invoices", description = "Operaciones sobre facturas")
@SecurityRequirement(name = "jwt-auth")
public class InvoiceController {

  @EJB InvoiceService invoiceService;

  @GET
  @Path("/invoice/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Obtiene una factura por ID")
  public Response getInvoice(
      @Parameter(description = "ID de la factura") @PathParam("id") Long id) {
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
          .entity("Error obteniendo la factura.")
          .build();
    }
  }

  @GET
  @Path("/invoice/number/{num}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Obtiene una factura por número")
  public Response getInvoiceByNumber(
      @Parameter(description = "Número de factura") @PathParam("num") String num) {
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
          .entity("Error obteniendo la factura.")
          .build();
    }
  }

  @GET
  @Path("/invoice/order/{orderId}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Obtiene una factura por ID de orden")
  public Response getInvoiceByOrder(
      @Parameter(description = "ID de la orden") @PathParam("orderId") Long orderId) {
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
          .entity("Error obteniendo la factura por orden.")
          .build();
    }
  }

  @GET
  @Path("/invoices")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Obtiene todas las facturas")
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
          .entity("Error obteniendo las facturas")
          .build();
    }
  }

  @POST
  @Path("/invoice")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Guarda o actualiza una factura")
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
          .entity("Error guardando la factura.")
          .build();
    }
  }

  @DELETE
  @Path("/invoice/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Elimina una factura")
  public Response eliminarInvoice(
      @Parameter(description = "ID de la factura") @PathParam("id") Long id) {
    try {
      Respuesta r = invoiceService.eliminarInvoice(id);
      if (!r.getEstado()) {
        return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
      }
      return Response.ok().build();
    } catch (Exception ex) {
      Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex);
      return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
          .entity("Error eliminando la factura.")
          .build();
    }
  }
}
