package cr.ac.una.koffeefxws.service;

import java.util.ArrayList;
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
import cr.ac.una.koffeefxws.model.CashOpening;
import cr.ac.una.koffeefxws.model.Customer;
import cr.ac.una.koffeefxws.model.CustomerOrder;
import cr.ac.una.koffeefxws.model.Invoice;
import cr.ac.una.koffeefxws.model.InvoiceDTO;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;

@Stateless
@LocalBean
public class InvoiceService {

  private static final Logger LOG = Logger.getLogger(InvoiceService.class.getName());

  @PersistenceContext(unitName = "KoffeeFXWSPU")
  private EntityManager em;

  public Respuesta getInvoice(Long id) {
    try {
      Query qryInvoice = em.createNamedQuery("Invoice.findById", Invoice.class);
      qryInvoice.setParameter("id", id);

      return new Respuesta(
          true,
          CodigoRespuesta.CORRECTO,
          "",
          "",
          "Invoice",
          new InvoiceDTO((Invoice) qryInvoice.getSingleResult()));
    } catch (NoResultException ex) {
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_NOENCONTRADO,
          "No invoice found with the provided ID.",
          "getInvoice NoResultException");
    } catch (NonUniqueResultException ex) {
      LOG.log(Level.SEVERE, "An error occurred while querying the invoice.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while querying the invoice.",
          "getInvoice NonUniqueResultException");
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "An error occurred while querying the invoice.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while querying the invoice.",
          "getInvoice " + ex.getMessage());
    }
  }

  public Respuesta getInvoiceByNumber(String invoiceNumber) {
    try {
      Query qryInvoice = em.createNamedQuery("Invoice.findByInvoiceNumber", Invoice.class);
      qryInvoice.setParameter("invoiceNumber", invoiceNumber);

      return new Respuesta(
          true,
          CodigoRespuesta.CORRECTO,
          "",
          "",
          "Invoice",
          new InvoiceDTO((Invoice) qryInvoice.getSingleResult()));
    } catch (NoResultException ex) {
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_NOENCONTRADO,
          "No invoice found with the provided number.",
          "getInvoiceByNumber NoResultException");
    } catch (NonUniqueResultException ex) {
      LOG.log(Level.SEVERE, "An error occurred while querying the invoice.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while querying the invoice.",
          "getInvoiceByNumber NonUniqueResultException");
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "An error occurred while querying the invoice.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while querying the invoice.",
          "getInvoiceByNumber " + ex.getMessage());
    }
  }

  public Respuesta getInvoices() {
    try {
      Query query = em.createNamedQuery("Invoice.findAll", Invoice.class);
      List<Invoice> invoices = (List<Invoice>) query.getResultList();
      List<InvoiceDTO> invoicesDto = new ArrayList<>();
      for (Invoice invoice : invoices) {
        invoicesDto.add(new InvoiceDTO(invoice));
      }
      return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Invoices", invoicesDto);
    } catch (NoResultException ex) {
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_NOENCONTRADO,
          "No invoices found with the provided criteria.",
          "getInvoices NoResultException");
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "An error occurred while querying invoices.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while querying invoices.",
          "getInvoices " + ex.getMessage());
    }
  }

  public Respuesta getInvoiceByOrderId(Long orderId) {
    try {
      Query qryInvoice = em.createNamedQuery("Invoice.findByOrderId", Invoice.class);
      qryInvoice.setParameter("orderId", orderId);
      Invoice invoice = (Invoice) qryInvoice.getSingleResult();
      return new Respuesta(
          true, CodigoRespuesta.CORRECTO, "", "", "Invoice", new InvoiceDTO(invoice));
    } catch (NoResultException ex) {
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_NOENCONTRADO,
          "No invoice associated with the specified order.",
          "getInvoiceByOrderId NoResultException");
    } catch (NonUniqueResultException ex) {
      LOG.log(Level.SEVERE, "An error occurred while querying the invoice by order.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while querying the invoice by order.",
          "getInvoiceByOrderId NonUniqueResultException");
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "An error occurred while querying the invoice by order.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while querying the invoice by order.",
          "getInvoiceByOrderId " + ex.getMessage());
    }
  }

  public Respuesta guardarInvoice(InvoiceDTO invoiceDto) {
    try {
      Invoice invoice;
      if (invoiceDto.getId() != null && invoiceDto.getId() > 0) {
        invoice = em.find(Invoice.class, invoiceDto.getId());
        if (invoice == null) {
          return new Respuesta(
              false,
              CodigoRespuesta.ERROR_NOENCONTRADO,
              "No invoice found to modify.",
              "guardarInvoice NoResultException");
        }
        invoice.actualizar(invoiceDto);

        if (invoiceDto.getCustomerId() != null) {
          Customer customer = em.find(Customer.class, invoiceDto.getCustomerId());
          if (customer != null) {
            invoice.setCustomerId(customer);
          }
        }

        if (invoiceDto.getCreatedBy() != null) {
          AppUser user = em.find(AppUser.class, invoiceDto.getCreatedBy());
          if (user != null) {
            invoice.setCreatedBy(user);
          }
        }

        if (invoiceDto.getCustomerOrderId() != null) {
          CustomerOrder order = em.find(CustomerOrder.class, invoiceDto.getCustomerOrderId());
          if (order != null) {
            invoice.setCustomerOrderId(order);
          }
        }

        if (invoiceDto.getCashOpeningId() != null) {
          CashOpening cashOpening = em.find(CashOpening.class, invoiceDto.getCashOpeningId());
          if (cashOpening != null) {
            invoice.setCashOpeningId(cashOpening);
          }
        }

        invoice = em.merge(invoice);
      } else {
        Invoice existing = null;
        if (invoiceDto.getCustomerOrderId() != null) {
          try {
            Query qryExisting = em.createNamedQuery("Invoice.findByOrderId", Invoice.class);
            qryExisting.setParameter("orderId", invoiceDto.getCustomerOrderId());
            existing = (Invoice) qryExisting.getSingleResult();
          } catch (NoResultException ignore) {
            existing = null;
          }
        }

        if (existing != null) {
          invoice = existing;
          invoice.actualizar(invoiceDto);
        } else {
          invoice = new Invoice(invoiceDto);
        }

        if (invoiceDto.getCustomerId() != null) {
          Customer customer = em.find(Customer.class, invoiceDto.getCustomerId());
          if (customer != null) {
            invoice.setCustomerId(customer);
          }
        }

        if (invoiceDto.getCreatedBy() != null) {
          AppUser user = em.find(AppUser.class, invoiceDto.getCreatedBy());
          if (user != null) {
            invoice.setCreatedBy(user);
          }
        }

        if (invoiceDto.getCustomerOrderId() != null) {
          CustomerOrder order = em.find(CustomerOrder.class, invoiceDto.getCustomerOrderId());
          if (order != null) {
            invoice.setCustomerOrderId(order);
            order.setInvoice(invoice);
            em.merge(order);
          }
        }

        if (invoiceDto.getCashOpeningId() != null) {
          CashOpening cashOpening = em.find(CashOpening.class, invoiceDto.getCashOpeningId());
          if (cashOpening != null) {
            invoice.setCashOpeningId(cashOpening);
          }
        }

        if (existing == null) {
          em.persist(invoice);
        } else {
          invoice = em.merge(invoice);
        }
      }
      em.flush();
      return new Respuesta(
          true, CodigoRespuesta.CORRECTO, "", "", "Invoice", new InvoiceDTO(invoice));
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "An error occurred while saving the invoice.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while saving the invoice.",
          "guardarInvoice " + ex.getMessage());
    }
  }

  public Respuesta eliminarInvoice(Long id) {
    try {
      Invoice invoice;
      if (id != null && id > 0) {
        invoice = em.find(Invoice.class, id);
        if (invoice == null) {
          return new Respuesta(
              false,
              CodigoRespuesta.ERROR_NOENCONTRADO,
              "No invoice found to delete.",
              "eliminarInvoice NoResultException");
        }
        em.remove(invoice);
      } else {
        return new Respuesta(
            false,
            CodigoRespuesta.ERROR_NOENCONTRADO,
            "You must provide the invoice to delete.",
            "eliminarInvoice NoResultException");
      }
      em.flush();
      return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "An error occurred while deleting the invoice.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while deleting the invoice.",
          "eliminarInvoice " + ex.getMessage());
    }
  }
}
