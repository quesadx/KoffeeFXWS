/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.koffeefxws.service;

import cr.ac.una.koffeefxws.model.ProductGroup;
import cr.ac.una.koffeefxws.model.ProductGroupDTO;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.sql.SQLIntegrityConstraintViolationException;
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
public class ProductGroupService {
    
    private static final Logger LOG = Logger.getLogger(ProductGroupService.class.getName());
    
    @PersistenceContext(unitName = "KoffeeFXWSPU")
    private EntityManager em;
    
    public Respuesta getProductGroup(Long id) {
        try {
            Query qryProductGroup = em.createNamedQuery("ProductGroup.findById", ProductGroup.class);
            qryProductGroup.setParameter("id", id);

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "ProductGroup", new ProductGroupDTO((ProductGroup) qryProductGroup.getSingleResult()));

        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existe un grupo de productos con el código ingresado.", "getProductGroup NoResultException");
        } catch (NonUniqueResultException ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar el grupo de productos.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar el grupo de productos.", "getProductGroup NonUniqueResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar el grupo de productos.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar el grupo de productos.", "getProductGroup " + ex.getMessage());
        }
    }

    public Respuesta getProductGroups() {
        try {
            Query query = em.createNamedQuery("ProductGroup.findAll", ProductGroup.class);
            List<ProductGroup> productGroups = (List<ProductGroup>) query.getResultList();
            List<ProductGroupDTO> productGroupsDto = new ArrayList<>();
            for (ProductGroup productGroup : productGroups) {
                productGroupsDto.add(new ProductGroupDTO(productGroup));
            }

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "ProductGroups", productGroupsDto);

        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existen grupos de productos registrados.", "getProductGroups NoResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar los grupos de productos.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar los grupos de productos.", "getProductGroups " + ex.getMessage());
        }
    }

    public Respuesta guardarProductGroup(ProductGroupDTO productGroupDto) {
        try {
            ProductGroup productGroup;
            if (productGroupDto.getId() != null && productGroupDto.getId() > 0) {
                productGroup = em.find(ProductGroup.class, productGroupDto.getId());
                if (productGroup == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el grupo de productos a modificar.", "guardarProductGroup NoResultException");
                }
                productGroup.actualizar(productGroupDto);
                productGroup = em.merge(productGroup);
            } else {
                productGroup = new ProductGroup(productGroupDto);
                em.persist(productGroup);
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "ProductGroup", new ProductGroupDTO(productGroup));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al guardar el grupo de productos.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al guardar el grupo de productos.", "guardarProductGroup " + ex.getMessage());
        }
    }

    public Respuesta eliminarProductGroup(Long id) {
        try {
            ProductGroup productGroup;
            if (id != null && id > 0) {
                productGroup = em.find(ProductGroup.class, id);
                if (productGroup == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el grupo de productos a eliminar.", "eliminarProductGroup NoResultException");
                }
                em.remove(productGroup);
            } else {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "Debe cargar el grupo de productos a eliminar.", "eliminarProductGroup NoResultException");
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
        } catch (Exception ex) {
            if (ex.getCause() != null && ex.getCause().getCause().getClass() == SQLIntegrityConstraintViolationException.class) {
                return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "No se puede eliminar el grupo de productos porque tiene relaciones con otros registros.", "eliminarProductGroup " + ex.getMessage());
            }
            LOG.log(Level.SEVERE, "Ocurrió un error al eliminar el grupo de productos.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al eliminar el grupo de productos.", "eliminarProductGroup " + ex.getMessage());
        }
    }
}
