/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.koffeefxws.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
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

import cr.ac.una.koffeefxws.model.Product;
import cr.ac.una.koffeefxws.model.ProductDTO;
import cr.ac.una.koffeefxws.model.ProductGroup;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;

/**
 * @author quesadx
 */
@Stateless
@LocalBean
public class ProductService {

  private static final Logger LOG = Logger.getLogger(ProductService.class.getName());

  @PersistenceContext(unitName = "KoffeeFXWSPU")
  private EntityManager em;

  public Respuesta getProduct(Long id) {
    try {
      Query qryProduct = em.createNamedQuery("Product.findById", Product.class);
      qryProduct.setParameter("id", id);

      return new Respuesta(
          true,
          CodigoRespuesta.CORRECTO,
          "",
          "",
          "Product",
          new ProductDTO((Product) qryProduct.getSingleResult()));
    } catch (NoResultException ex) {
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_NOENCONTRADO,
          "No product found with the provided ID.",
          "getProduct NoResultException");
    } catch (NonUniqueResultException ex) {
      LOG.log(Level.SEVERE, "An error occurred while querying the product.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while querying the product.",
          "getProduct NonUniqueResultException");
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "An error occurred while querying the product.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while querying the product.",
          "getProduct " + ex.getMessage());
    }
  }

  public Respuesta getProducts() {
    try {
      Query query = em.createNamedQuery("Product.findAll", Product.class);
      List<Product> products = (List<Product>) query.getResultList();
      List<ProductDTO> productsDto = new ArrayList<>();
      for (Product product : products) {
        productsDto.add(new ProductDTO(product));
      }

      return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Products", productsDto);
    } catch (NoResultException ex) {
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_NOENCONTRADO,
          "No products found.",
          "getProducts NoResultException");
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "An error occurred while querying products.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while querying products.",
          "getProducts " + ex.getMessage());
    }
  }

  public Respuesta getActiveProducts() {
    try {
      Query query = em.createNamedQuery("Product.findByIsActive", Product.class);
      query.setParameter("isActive", 'Y');
      List<Product> products = (List<Product>) query.getResultList();
      List<ProductDTO> productsDto = new ArrayList<>();
      for (Product product : products) {
        productsDto.add(new ProductDTO(product));
      }

      return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Products", productsDto);
    } catch (NoResultException ex) {
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_NOENCONTRADO,
          "No active products found.",
          "getActiveProducts NoResultException");
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "An error occurred while querying active products.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while querying active products.",
          "getActiveProducts " + ex.getMessage());
    }
  }

  public Respuesta guardarProduct(ProductDTO productDto) {
    try {
      Product product;
      if (productDto.getId() != null && productDto.getId() > 0) {
        product = em.find(Product.class, productDto.getId());
        if (product == null) {
          return new Respuesta(
              false,
              CodigoRespuesta.ERROR_NOENCONTRADO,
              "No product found to modify.",
              "guardarProduct NoResultException");
        }
        product.actualizar(productDto);

        if (productDto.getProductGroupId() != null) {
          ProductGroup productGroup = em.find(ProductGroup.class, productDto.getProductGroupId());
          if (productGroup != null) {
            product.setProductGroupId(productGroup);
          }
        }

        product = em.merge(product);
      } else {
        product = new Product(productDto);
        product.setCreatedAt(LocalDate.now());

        if (productDto.getProductGroupId() != null) {
          ProductGroup productGroup = em.find(ProductGroup.class, productDto.getProductGroupId());
          if (productGroup != null) {
            product.setProductGroupId(productGroup);
          }
        }

        em.persist(product);
      }
      em.flush();
      return new Respuesta(
          true, CodigoRespuesta.CORRECTO, "", "", "Product", new ProductDTO(product));
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "An error occurred while saving the product.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while saving the product.",
          "guardarProduct " + ex.getMessage());
    }
  }

  public Respuesta eliminarProduct(Long id) {
    try {
      Product product;
      if (id != null && id > 0) {
        product = em.find(Product.class, id);
        if (product == null) {
          return new Respuesta(
              false,
              CodigoRespuesta.ERROR_NOENCONTRADO,
              "No product found to delete.",
              "eliminarProduct NoResultException");
        }
        em.remove(product);
      } else {
        return new Respuesta(
            false,
            CodigoRespuesta.ERROR_NOENCONTRADO,
            "You must provide the product to delete.",
            "eliminarProduct NoResultException");
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
            "Cannot delete the product because it has related records.",
            "eliminarProduct " + ex.getMessage());
      }
      LOG.log(Level.SEVERE, "An error occurred while deleting the product.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while deleting the product.",
          "eliminarProduct " + ex.getMessage());
    }
  }
}
