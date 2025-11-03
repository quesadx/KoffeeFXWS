/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.koffeefxws.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import cr.ac.una.koffeefxws.model.AppUser;
import cr.ac.una.koffeefxws.model.Customer;
import cr.ac.una.koffeefxws.model.CustomerOrder;
import cr.ac.una.koffeefxws.model.CustomerOrderDTO;
import cr.ac.una.koffeefxws.model.DiningArea;
import cr.ac.una.koffeefxws.model.DiningTable;
import cr.ac.una.koffeefxws.model.Invoice;
import cr.ac.una.koffeefxws.model.OrderItem;
import cr.ac.una.koffeefxws.model.OrderItemDTO;
import cr.ac.una.koffeefxws.model.Product;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;

/**
 * @author quesadx
 */
@Stateless
@LocalBean
public class CustomerOrderService {

  private static final Logger LOG = Logger.getLogger(CustomerOrderService.class.getName());

  @PersistenceContext(unitName = "KoffeeFXWSPU")
  private EntityManager em;

  public Respuesta getCustomerOrder(Long id) {
    try {
      Query qryOrder = em.createNamedQuery("CustomerOrder.findById", CustomerOrder.class);
      qryOrder.setParameter("id", id);

      CustomerOrder order = (CustomerOrder) qryOrder.getSingleResult();
      CustomerOrderDTO dto = new CustomerOrderDTO(order);

      // Attach invoice if exists
      if (order.getInvoice() != null) {
        dto.setInvoice(new cr.ac.una.koffeefxws.model.InvoiceDTO(order.getInvoice()));
      }

      // Load order items
      if (order.getOrderItemList() != null) {
        for (OrderItem item : order.getOrderItemList()) {
          dto.getOrderItems().add(new OrderItemDTO(item));
        }
      }

      return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "CustomerOrder", dto);
    } catch (NoResultException ex) {
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_NOENCONTRADO,
          "No existe un pedido con el código ingresado.",
          "getCustomerOrder NoResultException");
    } catch (NonUniqueResultException ex) {
      LOG.log(Level.SEVERE, "Ocurrió un error al consultar el pedido.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "Ocurrió un error al consultar el pedido.",
          "getCustomerOrder NonUniqueResultException");
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "Ocurrió un error al consultar el pedido.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "Ocurrió un error al consultar el pedido.",
          "getCustomerOrder " + ex.getMessage());
    }
  }

  public Respuesta getCustomerOrders() {
    try {
      Query query = em.createNamedQuery("CustomerOrder.findAll", CustomerOrder.class);
      List<CustomerOrder> orders = (List<CustomerOrder>) query.getResultList();
      List<CustomerOrderDTO> ordersDto = new ArrayList<>();
      for (CustomerOrder order : orders) {
        ordersDto.add(new CustomerOrderDTO(order));
      }

      return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "CustomerOrders", ordersDto);
    } catch (NoResultException ex) {
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_NOENCONTRADO,
          "No existen pedidos registrados.",
          "getCustomerOrders NoResultException");
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "Ocurrió un error al consultar los pedidos.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "Ocurrió un error al consultar los pedidos.",
          "getCustomerOrders " + ex.getMessage());
    }
  }

  public Respuesta guardarCustomerOrder(CustomerOrderDTO orderDto) {
    try {
      if (orderDto.getCreatedBy() == null) {
        return new Respuesta(
            false,
            CodigoRespuesta.ERROR_CLIENTE,
            "El ID del usuario creador es obligatorio.",
            "guardarCustomerOrder Validation");
      }

      AppUser user = em.find(AppUser.class, orderDto.getCreatedBy());
      if (user == null) {
        return new Respuesta(
            false,
            CodigoRespuesta.ERROR_NOENCONTRADO,
            "No existe un usuario con el ID indicado.",
            "guardarCustomerOrder Validation");
      }

      Customer customer = null;
      if (orderDto.getCustomerId() != null) {
        customer = em.find(Customer.class, orderDto.getCustomerId());
      }

      DiningArea diningArea = null;
      if (orderDto.getDiningAreaId() != null) {
        diningArea = em.find(DiningArea.class, orderDto.getDiningAreaId());
      }

      DiningTable diningTable = null;
      if (orderDto.getDiningTableId() != null) {
        diningTable = em.find(DiningTable.class, orderDto.getDiningTableId());
      }

      CustomerOrder order;
      if (orderDto.getId() != null && orderDto.getId() > 0) {
        order = em.find(CustomerOrder.class, orderDto.getId());
        if (order == null) {
          return new Respuesta(
              false,
              CodigoRespuesta.ERROR_NOENCONTRADO,
              "No se encontró el pedido a modificar.",
              "guardarCustomerOrder NoResultException");
        }
        order.actualizar(orderDto);
        order.setUpdatedAt(new Date());
        order = em.merge(order);
      } else {
        order = new CustomerOrder(orderDto);
        order.setCreatedAt(LocalDate.now());
        order.setCreatedBy(user);
        order.setCustomerId(customer);
        order.setDiningAreaId(diningArea);
        order.setDiningTableId(diningTable);
        if (order.getStatus() == null) {
          order.setStatus("PENDING");
        }
        em.persist(order);
      }

      // Handle invoice relationship if present in DTO
      if (orderDto.getInvoice() != null && orderDto.getInvoice().getId() != null) {
        Invoice invoice = em.find(Invoice.class, orderDto.getInvoice().getId());
        if (invoice != null) {
          order.setInvoice(invoice);
          LOG.log(
              Level.INFO,
              "Asociada factura {0} a orden {1}",
              new Object[] {invoice.getId(), order.getId()});
        }
      }

      // Merge the order to persist all changes including invoice relationship
      order = em.merge(order);

      // Handle order items (only for new orders or if items list is provided)
      if (orderDto.getOrderItems() != null && !orderDto.getOrderItems().isEmpty()) {
        for (OrderItemDTO itemDto : orderDto.getOrderItems()) {
          if (itemDto.getProductId() == null) {
            continue; // Skip items without product
          }

          Product product = em.find(Product.class, itemDto.getProductId());
          if (product == null) {
            continue; // Skip invalid products
          }

          OrderItem item;
          if (itemDto.getId() != null && itemDto.getId() > 0) {
            // Update existing item
            item = em.find(OrderItem.class, itemDto.getId());
            if (item != null) {
              item.actualizar(itemDto);
              item.setProductId(product);
              item = em.merge(item);
            }
          } else {
            // Create new item
            item = new OrderItem(itemDto);
            item.setCustomerOrderId(order);
            item.setProductId(product);
            if (item.getStatus() == null) {
              item.setStatus("PENDING");
            }
            em.persist(item);
          }
        }
      }

      em.flush();
      return new Respuesta(
          true, CodigoRespuesta.CORRECTO, "", "", "CustomerOrder", new CustomerOrderDTO(order));
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "Ocurrió un error al guardar el pedido.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "Ocurrió un error al guardar el pedido.",
          "guardarCustomerOrder " + ex.getMessage());
    }
  }

  public Respuesta eliminarCustomerOrder(Long id) {
    try {
      CustomerOrder order;
      if (id != null && id > 0) {
        order = em.find(CustomerOrder.class, id);
        if (order == null) {
          return new Respuesta(
              false,
              CodigoRespuesta.ERROR_NOENCONTRADO,
              "No se encontró el pedido a eliminar.",
              "eliminarCustomerOrder NoResultException");
        }
        em.remove(order);
      } else {
        return new Respuesta(
            false,
            CodigoRespuesta.ERROR_NOENCONTRADO,
            "Debe cargar el pedido a eliminar.",
            "eliminarCustomerOrder NoResultException");
      }
      em.flush();
      return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "Ocurrió un error al eliminar el pedido.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "Ocurrió un error al eliminar el pedido.",
          "eliminarCustomerOrder " + ex.getMessage());
    }
  }
}
