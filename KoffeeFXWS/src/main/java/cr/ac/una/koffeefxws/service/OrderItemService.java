package cr.ac.una.koffeefxws.service;

import cr.ac.una.koffeefxws.model.CustomerOrder;
import cr.ac.una.koffeefxws.model.OrderItem;
import cr.ac.una.koffeefxws.model.OrderItemDTO;
import cr.ac.una.koffeefxws.model.Product;
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

/**
 *
 * @author quesadx
 */
@Stateless
@LocalBean
public class OrderItemService {

    private static final Logger LOG = Logger.getLogger(
        OrderItemService.class.getName()
    );

    @PersistenceContext(unitName = "KoffeeFXWSPU")
    private EntityManager em;

    public Respuesta getOrderItem(Long id) {
        try {
            Query qryOrderItem = em.createNamedQuery(
                "OrderItem.findById",
                OrderItem.class
            );
            qryOrderItem.setParameter("id", id);

            return new Respuesta(
                true,
                CodigoRespuesta.CORRECTO,
                "",
                "",
                "OrderItem",
                new OrderItemDTO((OrderItem) qryOrderItem.getSingleResult())
            );
        } catch (NoResultException ex) {
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_NOENCONTRADO,
                "No existe un item con el código ingresado.",
                "getOrderItem NoResultException"
            );
        } catch (NonUniqueResultException ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar el item.", ex);
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_INTERNO,
                "Ocurrió un error al consultar el item.",
                "getOrderItem NonUniqueResultException"
            );
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar el item.", ex);
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_INTERNO,
                "Ocurrió un error al consultar el item.",
                "getOrderItem " + ex.getMessage()
            );
        }
    }

    public Respuesta getOrderItems() {
        try {
            Query query = em.createNamedQuery(
                "OrderItem.findAll",
                OrderItem.class
            );
            List<OrderItem> orderItems = (List<
                OrderItem
            >) query.getResultList();
            List<OrderItemDTO> orderItemsDto = new ArrayList<>();
            for (OrderItem item : orderItems) {
                orderItemsDto.add(new OrderItemDTO(item));
            }
            return new Respuesta(
                true,
                CodigoRespuesta.CORRECTO,
                "",
                "",
                "OrderItems",
                orderItemsDto
            );
        } catch (NoResultException ex) {
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_NOENCONTRADO,
                "No existen items con los criterios ingresados.",
                "getOrderItems NoResultException"
            );
        } catch (Exception ex) {
            LOG.log(
                Level.SEVERE,
                "Ocurrió un error al consultar los items.",
                ex
            );
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_INTERNO,
                "Ocurrió un error al consultar los items.",
                "getOrderItems " + ex.getMessage()
            );
        }
    }

    public Respuesta guardarOrderItem(OrderItemDTO itemDto) {
        try {
            OrderItem item;
            if (itemDto.getId() != null && itemDto.getId() > 0) {
                item = em.find(OrderItem.class, itemDto.getId());
                if (item == null) {
                    return new Respuesta(
                        false,
                        CodigoRespuesta.ERROR_NOENCONTRADO,
                        "No se encontró el item a modificar.",
                        "guardarOrderItem NoResultException"
                    );
                }
                item.actualizar(itemDto);

                // Handle FK relationships
                if (itemDto.getCustomerOrderId() != null) {
                    CustomerOrder order = em.find(
                        CustomerOrder.class,
                        itemDto.getCustomerOrderId()
                    );
                    if (order != null) {
                        item.setCustomerOrderId(order);
                    }
                }

                if (itemDto.getProductId() != null) {
                    Product product = em.find(
                        Product.class,
                        itemDto.getProductId()
                    );
                    if (product != null) {
                        item.setProductId(product);
                    }
                }

                item = em.merge(item);
            } else {
                item = new OrderItem(itemDto);

                // Handle FK relationships
                if (itemDto.getCustomerOrderId() != null) {
                    CustomerOrder order = em.find(
                        CustomerOrder.class,
                        itemDto.getCustomerOrderId()
                    );
                    if (order != null) {
                        item.setCustomerOrderId(order);
                    }
                }

                if (itemDto.getProductId() != null) {
                    Product product = em.find(
                        Product.class,
                        itemDto.getProductId()
                    );
                    if (product != null) {
                        item.setProductId(product);
                    }
                }

                em.persist(item);
            }
            em.flush();
            return new Respuesta(
                true,
                CodigoRespuesta.CORRECTO,
                "",
                "",
                "OrderItem",
                new OrderItemDTO(item)
            );
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al guardar el item.", ex);
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_INTERNO,
                "Ocurrió un error al guardar el item.",
                "guardarOrderItem " + ex.getMessage()
            );
        }
    }

    public Respuesta eliminarOrderItem(Long id) {
        try {
            OrderItem item;
            if (id != null && id > 0) {
                item = em.find(OrderItem.class, id);
                if (item == null) {
                    return new Respuesta(
                        false,
                        CodigoRespuesta.ERROR_NOENCONTRADO,
                        "No se encontró el item a eliminar.",
                        "eliminarOrderItem NoResultException"
                    );
                }
                em.remove(item);
            } else {
                return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_NOENCONTRADO,
                    "Debe cargar el item a eliminar.",
                    "eliminarOrderItem NoResultException"
                );
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al eliminar el item.", ex);
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_INTERNO,
                "Ocurrió un error al eliminar el item.",
                "eliminarOrderItem " + ex.getMessage()
            );
        }
    }
}
