package cr.ac.una.koffeefxws.service;

import cr.ac.una.koffeefxws.model.Invoice;
import cr.ac.una.koffeefxws.model.InvoiceDTO;
import cr.ac.una.koffeefxws.model.Customer;
import cr.ac.una.koffeefxws.model.AppUser;
import cr.ac.una.koffeefxws.model.CustomerOrder;
import cr.ac.una.koffeefxws.model.CashOpening;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Invoice", new InvoiceDTO((Invoice) qryInvoice.getSingleResult()));

        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existe una factura con el código ingresado.", "getInvoice NoResultException");
        } catch (NonUniqueResultException ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar la factura.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar la factura.", "getInvoice NonUniqueResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar la factura.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar la factura.", "getInvoice " + ex.getMessage());
        }
    }

    public Respuesta getInvoiceByNumber(String invoiceNumber) {
        try {
            Query qryInvoice = em.createNamedQuery("Invoice.findByInvoiceNumber", Invoice.class);
            qryInvoice.setParameter("invoiceNumber", invoiceNumber);

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Invoice", new InvoiceDTO((Invoice) qryInvoice.getSingleResult()));

        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existe una factura con el número ingresado.", "getInvoiceByNumber NoResultException");
        } catch (NonUniqueResultException ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar la factura.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar la factura.", "getInvoiceByNumber NonUniqueResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar la factura.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar la factura.", "getInvoiceByNumber " + ex.getMessage());
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
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existen facturas con los criterios ingresados.", "getInvoices NoResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar las facturas.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar las facturas.", "getInvoices " + ex.getMessage());
        }
    }

    public Respuesta guardarInvoice(InvoiceDTO invoiceDto) {
        try {
            Invoice invoice;
            if (invoiceDto.getId() != null && invoiceDto.getId() > 0) {
                invoice = em.find(Invoice.class, invoiceDto.getId());
                if (invoice == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró la factura a modificar.", "guardarInvoice NoResultException");
                }
                invoice.actualizar(invoiceDto);
                
                // Handle FK relationships
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
                invoice = new Invoice(invoiceDto);
                
                // Handle FK relationships
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
                
                em.persist(invoice);
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Invoice", new InvoiceDTO(invoice));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al guardar la factura.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al guardar la factura.", "guardarInvoice " + ex.getMessage());
        }
    }

    public Respuesta eliminarInvoice(Long id) {
        try {
            Invoice invoice;
            if (id != null && id > 0) {
                invoice = em.find(Invoice.class, id);
                if (invoice == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró la factura a eliminar.", "eliminarInvoice NoResultException");
                }
                em.remove(invoice);
            } else {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "Debe cargar la factura a eliminar.", "eliminarInvoice NoResultException");
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al eliminar la factura.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al eliminar la factura.", "eliminarInvoice " + ex.getMessage());
        }
    }
}
