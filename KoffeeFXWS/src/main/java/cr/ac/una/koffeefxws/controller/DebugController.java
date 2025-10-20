package cr.ac.una.koffeefxws.controller;

import cr.ac.una.koffeefxws.model.*;
import cr.ac.una.koffeefxws.service.*;
import cr.ac.una.koffeefxws.util.Respuesta;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Comprehensive Debug Controller for testing all CRUD operations from JavaFX client
 */
@Path("/debug")
public class DebugController {

    private static final Logger LOG = Logger.getLogger(DebugController.class.getName());

    @EJB
    DiningTableService diningTableService;

    @EJB
    AppUserService appUserService;

    @EJB
    ProductService productService;

    @EJB
    ProductGroupService productGroupService;

    @EJB
    DiningAreaService diningAreaService;

    @EJB
    CustomerService customerService;

    @EJB
    SystemParameterService systemParameterService;

    @EJB
    RoleService roleService;

    @EJB
    CashOpeningService cashOpeningService;

    @EJB
    CustomerOrderService customerOrderService;

    @EJB
    OrderItemService orderItemService;

    @EJB
    InvoiceService invoiceService;

    // ========== DINING TABLE ENDPOINTS ==========
    
    @GET
    @Path("/diningTable/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getDiningTable(@PathParam("id") Long id) {
        try {
            cr.ac.una.koffeefxws.util.Respuesta res = diningTableService.getDiningTable(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting dining table", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/diningTables")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiningTables() {
        try {
            Respuesta res = diningTableService.getDiningTables();
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting dining tables", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("/diningTable")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarDiningTable(DiningTableDTO diningTableDto) {
        try {
            Respuesta res = diningTableService.guardarDiningTable(diningTableDto);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error saving dining table", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/diningTable/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarDiningTable(@PathParam("id") Long id) {
        try {
            Respuesta res = diningTableService.eliminarDiningTable(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error deleting dining table", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    // ========== APP USER ENDPOINTS ==========
    
    @GET
    @Path("/appUser/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAppUser(@PathParam("id") Long id) {
        try {
            Respuesta res = appUserService.getAppUser(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting app user", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/appUser/username/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAppUserByUsername(@PathParam("username") String username) {
        try {
            Respuesta res = appUserService.getAppUserByUsername(username);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting app user by username", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/appUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAppUsers() {
        try {
            Respuesta res = appUserService.getAppUsers();
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting app users", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("/appUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarAppUser(AppUserDTO appUserDto) {
        try {
            Respuesta res = appUserService.guardarAppUser(appUserDto);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error saving app user", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/appUser/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarAppUser(@PathParam("id") Long id) {
        try {
            Respuesta res = appUserService.eliminarAppUser(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error deleting app user", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    // ========== PRODUCT ENDPOINTS ==========
    
    @GET
    @Path("/product/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProduct(@PathParam("id") Long id) {
        try {
            Respuesta res = productService.getProduct(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting product", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/products")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts() {
        try {
            Respuesta res = productService.getProducts();
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting products", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("/product")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarProduct(ProductDTO productDto) {
        try {
            Respuesta res = productService.guardarProduct(productDto);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error saving product", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/product/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarProduct(@PathParam("id") Long id) {
        try {
            Respuesta res = productService.eliminarProduct(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error deleting product", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    // ========== PRODUCT GROUP ENDPOINTS ==========
    
    @GET
    @Path("/productGroup/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductGroup(@PathParam("id") Long id) {
        try {
            Respuesta res = productGroupService.getProductGroup(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting product group", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/productGroups")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductGroups() {
        try {
            Respuesta res = productGroupService.getProductGroups();
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting product groups", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("/productGroup")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarProductGroup(ProductGroupDTO productGroupDto) {
        try {
            Respuesta res = productGroupService.guardarProductGroup(productGroupDto);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error saving product group", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/productGroup/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarProductGroup(@PathParam("id") Long id) {
        try {
            Respuesta res = productGroupService.eliminarProductGroup(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error deleting product group", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    // ========== DINING AREA ENDPOINTS ==========
    
    @GET
    @Path("/diningArea/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiningArea(@PathParam("id") Long id) {
        try {
            Respuesta res = diningAreaService.getDiningArea(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting dining area", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/diningAreas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiningAreas() {
        try {
            Respuesta res = diningAreaService.getDiningAreas();
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting dining areas", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("/diningArea")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarDiningArea(DiningAreaDTO diningAreaDto) {
        try {
            Respuesta res = diningAreaService.guardarDiningArea(diningAreaDto);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error saving dining area", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/diningArea/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarDiningArea(@PathParam("id") Long id) {
        try {
            Respuesta res = diningAreaService.eliminarDiningArea(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error deleting dining area", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    // ========== CUSTOMER ENDPOINTS ==========
    
    @GET
    @Path("/customer/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomer(@PathParam("id") Long id) {
        try {
            Respuesta res = customerService.getCustomer(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting customer", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/customers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomers() {
        try {
            Respuesta res = customerService.getCustomers();
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting customers", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("/customer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarCustomer(CustomerDTO customerDto) {
        try {
            Respuesta res = customerService.guardarCustomer(customerDto);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error saving customer", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/customer/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarCustomer(@PathParam("id") Long id) {
        try {
            Respuesta res = customerService.eliminarCustomer(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error deleting customer", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    // ========== SYSTEM PARAMETER ENDPOINTS ==========
    
    @GET
    @Path("/systemParameter/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSystemParameter(@PathParam("id") Long id) {
        try {
            Respuesta res = systemParameterService.getSystemParameter(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting system parameter", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/systemParameter/name/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSystemParameterByName(@PathParam("name") String name) {
        try {
            Respuesta res = systemParameterService.getSystemParameterByName(name);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting system parameter by name", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/systemParameters")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSystemParameters() {
        try {
            Respuesta res = systemParameterService.getSystemParameters();
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting system parameters", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("/systemParameter")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarSystemParameter(SystemParameterDTO systemParameterDto) {
        try {
            Respuesta res = systemParameterService.guardarSystemParameter(systemParameterDto);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error saving system parameter", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/systemParameter/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarSystemParameter(@PathParam("id") Long id) {
        try {
            Respuesta res = systemParameterService.eliminarSystemParameter(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error deleting system parameter", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    // ========== ROLE ENDPOINTS ==========
    
    @GET
    @Path("/role/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRole(@PathParam("id") Long id) {
        try {
            Respuesta res = roleService.getRole(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting role", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/roles")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoles() {
        try {
            Respuesta res = roleService.getRoles();
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting roles", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("/role")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarRole(RoleDTO roleDto) {
        try {
            Respuesta res = roleService.guardarRole(roleDto);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error saving role", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/role/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarRole(@PathParam("id") Long id) {
        try {
            Respuesta res = roleService.eliminarRole(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error deleting role", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    // ========== CASH OPENING ENDPOINTS ==========
    
    @GET
    @Path("/cashOpening/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCashOpening(@PathParam("id") Long id) {
        try {
            Respuesta res = cashOpeningService.getCashOpening(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting cash opening", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/cashOpenings")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCashOpenings() {
        try {
            Respuesta res = cashOpeningService.getCashOpenings();
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting cash openings", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/cashOpening/active/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActiveCashOpening(@PathParam("userId") Long userId) {
        try {
            Respuesta res = cashOpeningService.getActiveCashOpening(userId);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting active cash opening", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("/cashOpening")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarCashOpening(CashOpeningDTO cashOpeningDto) {
        try {
            Respuesta res = cashOpeningService.guardarCashOpening(cashOpeningDto);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error saving cash opening", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("/cashOpening/{id}/close")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response closeCashOpening(@PathParam("id") Long id, CashOpeningDTO cashOpeningDto) {
        try {
            Respuesta res = cashOpeningService.closeCashOpening(id, cashOpeningDto.getClosingAmount(), cashOpeningDto.getNotes());
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error closing cash opening", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/cashOpening/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarCashOpening(@PathParam("id") Long id) {
        try {
            Respuesta res = cashOpeningService.eliminarCashOpening(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error deleting cash opening", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    // ========== CUSTOMER ORDER ENDPOINTS ==========
    
    @GET
    @Path("/customerOrder/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerOrder(@PathParam("id") Long id) {
        try {
            Respuesta res = customerOrderService.getCustomerOrder(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting customer order", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/customerOrders")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerOrders() {
        try {
            Respuesta res = customerOrderService.getCustomerOrders();
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting customer orders", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("/customerOrder")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarCustomerOrder(CustomerOrderDTO customerOrderDto) {
        try {
            Respuesta res = customerOrderService.guardarCustomerOrder(customerOrderDto);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error saving customer order", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/customerOrder/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarCustomerOrder(@PathParam("id") Long id) {
        try {
            Respuesta res = customerOrderService.eliminarCustomerOrder(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error deleting customer order", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    // ========== ORDER ITEM ENDPOINTS ==========
    
    @GET
    @Path("/orderItem/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderItem(@PathParam("id") Long id) {
        try {
            Respuesta res = orderItemService.getOrderItem(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting order item", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/orderItems")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderItems() {
        try {
            Respuesta res = orderItemService.getOrderItems();
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting order items", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("/orderItem")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarOrderItem(OrderItemDTO orderItemDto) {
        try {
            Respuesta res = orderItemService.guardarOrderItem(orderItemDto);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error saving order item", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/orderItem/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarOrderItem(@PathParam("id") Long id) {
        try {
            Respuesta res = orderItemService.eliminarOrderItem(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error deleting order item", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    // ========== INVOICE ENDPOINTS ==========
    
    @GET
    @Path("/invoice/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInvoice(@PathParam("id") Long id) {
        try {
            Respuesta res = invoiceService.getInvoice(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting invoice", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/invoice/number/{invoiceNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInvoiceByNumber(@PathParam("invoiceNumber") String invoiceNumber) {
        try {
            Respuesta res = invoiceService.getInvoiceByNumber(invoiceNumber);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting invoice by number", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/invoices")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInvoices() {
        try {
            Respuesta res = invoiceService.getInvoices();
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error getting invoices", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("/invoice")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarInvoice(InvoiceDTO invoiceDto) {
        try {
            Respuesta res = invoiceService.guardarInvoice(invoiceDto);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error saving invoice", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/invoice/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarInvoice(@PathParam("id") Long id) {
        try {
            Respuesta res = invoiceService.eliminarInvoice(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error deleting invoice", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
}
