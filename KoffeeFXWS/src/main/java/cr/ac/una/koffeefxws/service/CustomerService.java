/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.koffeefxws.service;

import java.sql.SQLIntegrityConstraintViolationException;
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

import cr.ac.una.koffeefxws.model.Customer;
import cr.ac.una.koffeefxws.model.CustomerDTO;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;

/**
 * @author quesadx
 */
@Stateless
@LocalBean
public class CustomerService {

    private static final Logger LOG = Logger.getLogger(CustomerService.class.getName());

    @PersistenceContext(unitName = "KoffeeFXWSPU")
    private EntityManager em;

    public Respuesta getCustomer(Long id) {
        try {
            Query qryCustomer = em.createNamedQuery("Customer.findById", Customer.class);
            qryCustomer.setParameter("id", id);

            return new Respuesta(
                    true,
                    CodigoRespuesta.CORRECTO,
                    "",
                    "",
                    "Customer",
                    new CustomerDTO((Customer) qryCustomer.getSingleResult()));
        } catch (NoResultException ex) {
            return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No existe un cliente con el código ingresado.",
                    "getCustomer NoResultException");
        } catch (NonUniqueResultException ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar el cliente.", ex);
            return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al consultar el cliente.",
                    "getCustomer NonUniqueResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar el cliente.", ex);
            return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al consultar el cliente.",
                    "getCustomer " + ex.getMessage());
        }
    }

    public Respuesta getCustomerByEmail(String email) {
        try {
            Query qryCustomer = em.createNamedQuery("Customer.findByEmail", Customer.class);
            qryCustomer.setParameter("email", email);

            return new Respuesta(
                    true,
                    CodigoRespuesta.CORRECTO,
                    "",
                    "",
                    "Customer",
                    new CustomerDTO((Customer) qryCustomer.getSingleResult()));
        } catch (NoResultException ex) {
            return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No existe un cliente con el email ingresado.",
                    "getCustomerByEmail NoResultException");
        } catch (NonUniqueResultException ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar el cliente.", ex);
            return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al consultar el cliente.",
                    "getCustomerByEmail NonUniqueResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar el cliente.", ex);
            return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al consultar el cliente.",
                    "getCustomerByEmail " + ex.getMessage());
        }
    }

    public Respuesta getCustomers() {
        try {
            Query query = em.createNamedQuery("Customer.findAll", Customer.class);
            List<Customer> customers = (List<Customer>) query.getResultList();
            List<CustomerDTO> customersDto = new ArrayList<>();
            for (Customer customer : customers) {
                customersDto.add(new CustomerDTO(customer));
            }

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Customers", customersDto);
        } catch (NoResultException ex) {
            return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No existen clientes registrados.",
                    "getCustomers NoResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar los clientes.", ex);
            return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al consultar los clientes.",
                    "getCustomers " + ex.getMessage());
        }
    }

    public Respuesta guardarCustomer(CustomerDTO customerDto) {
        try {
            Customer customer;
            if (customerDto.getId() == null || customerDto.getId() == 0) {
                customer = new Customer(customerDto);
                em.persist(customer);
            } else {
                customer = em.find(Customer.class, customerDto.getId());
                if (customer == null) {
                    return new Respuesta(
                            false,
                            CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró el cliente a modificar.",
                            "guardarCustomer NoResultException");
                }
                customer.actualizar(customerDto);
            }
            em.flush();
            return new Respuesta(
                    true, CodigoRespuesta.CORRECTO, "", "", "Customer", new CustomerDTO(customer));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al guardar el cliente.", ex);
            return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al guardar el cliente.",
                    "guardarCustomer " + ex.getMessage());
        }
    }

    public Respuesta eliminarCustomer(Long id) {
        try {
            Customer customer;
            if (id != null && id > 0) {
                customer = em.find(Customer.class, id);
                if (customer == null) {
                    return new Respuesta(
                            false,
                            CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró el cliente a eliminar.",
                            "eliminarCustomer NoResultException");
                }
                em.remove(customer);
            } else {
                return new Respuesta(
                        false,
                        CodigoRespuesta.ERROR_NOENCONTRADO,
                        "Debe cargar el cliente a eliminar.",
                        "eliminarCustomer NoResultException");
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
        } catch (Exception ex) {
            if (ex.getCause() != null
                    && ex.getCause().getCause().getClass()
                            == SQLIntegrityConstraintViolationException.class) {
                return new Respuesta(
                        false,
                        CodigoRespuesta.ERROR_INTERNO,
                        "No se puede eliminar el cliente porque tiene relaciones con otros registros.",
                        "eliminarCustomer " + ex.getMessage());
            }
            LOG.log(Level.SEVERE, "Ocurrió un error al eliminar el cliente.", ex);
            return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al eliminar el cliente.",
                    "eliminarCustomer " + ex.getMessage());
        }
    }
}
