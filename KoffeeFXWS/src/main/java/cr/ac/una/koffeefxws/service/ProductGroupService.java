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

import cr.ac.una.koffeefxws.model.ProductGroup;
import cr.ac.una.koffeefxws.model.ProductGroupDTO;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;

/**
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

      return new Respuesta(
          true,
          CodigoRespuesta.CORRECTO,
          "",
          "",
          "ProductGroup",
          new ProductGroupDTO((ProductGroup) qryProductGroup.getSingleResult()));
    } catch (NoResultException ex) {
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_NOENCONTRADO,
          "No product group found with the provided ID.",
          "getProductGroup NoResultException");
    } catch (NonUniqueResultException ex) {
      LOG.log(Level.SEVERE, "An error occurred while querying the product group.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while querying the product group.",
          "getProductGroup NonUniqueResultException");
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "An error occurred while querying the product group.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while querying the product group.",
          "getProductGroup " + ex.getMessage());
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

      return new Respuesta(
          true, CodigoRespuesta.CORRECTO, "", "", "ProductGroups", productGroupsDto);
    } catch (NoResultException ex) {
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_NOENCONTRADO,
          "No product groups found.",
          "getProductGroups NoResultException");
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "An error occurred while querying product groups.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while querying product groups.",
          "getProductGroups " + ex.getMessage());
    }
  }

  public Respuesta guardarProductGroup(ProductGroupDTO productGroupDto) {
    try {
      ProductGroup productGroup;
      if (productGroupDto.getId() != null && productGroupDto.getId() > 0) {
        productGroup = em.find(ProductGroup.class, productGroupDto.getId());
        if (productGroup == null) {
          return new Respuesta(
              false,
              CodigoRespuesta.ERROR_NOENCONTRADO,
              "No product group found to modify.",
              "guardarProductGroup NoResultException");
        }
        productGroup.actualizar(productGroupDto);
        productGroup = em.merge(productGroup);
      } else {
        productGroup = new ProductGroup(productGroupDto);
        em.persist(productGroup);
      }
      em.flush();
      return new Respuesta(
          true,
          CodigoRespuesta.CORRECTO,
          "",
          "",
          "ProductGroup",
          new ProductGroupDTO(productGroup));
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "An error occurred while saving the product group.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while saving the product group.",
          "guardarProductGroup " + ex.getMessage());
    }
  }

  public Respuesta eliminarProductGroup(Long id) {
    try {
      ProductGroup productGroup;
      if (id != null && id > 0) {
        productGroup = em.find(ProductGroup.class, id);
        if (productGroup == null) {
          return new Respuesta(
              false,
              CodigoRespuesta.ERROR_NOENCONTRADO,
              "No product group found to delete.",
              "eliminarProductGroup NoResultException");
        }
        em.remove(productGroup);
      } else {
        return new Respuesta(
            false,
            CodigoRespuesta.ERROR_NOENCONTRADO,
            "You must provide the product group to delete.",
            "eliminarProductGroup NoResultException");
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
            "Cannot delete the product group because it has related records.",
            "eliminarProductGroup " + ex.getMessage());
      }
      LOG.log(Level.SEVERE, "An error occurred while deleting the product group.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while deleting the product group.",
          "eliminarProductGroup " + ex.getMessage());
    }
  }
}
