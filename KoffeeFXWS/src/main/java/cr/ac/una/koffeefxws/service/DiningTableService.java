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

import cr.ac.una.koffeefxws.model.DiningArea;
import cr.ac.una.koffeefxws.model.DiningTable;
import cr.ac.una.koffeefxws.model.DiningTableDTO;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;

/**
 * @author quesadx
 */
@Stateless
@LocalBean
public class DiningTableService {

  private static final Logger LOG = Logger.getLogger(DiningTableService.class.getName());

  @PersistenceContext(unitName = "KoffeeFXWSPU")
  private EntityManager em;

  public Respuesta getDiningTable(Long id) {
    try {
      Query qryDiningTable = em.createNamedQuery("DiningTable.findById", DiningTable.class);
      qryDiningTable.setParameter("id", id);

      return new Respuesta(
          true,
          CodigoRespuesta.CORRECTO,
          "",
          "",
          "DiningTable",
          new DiningTableDTO((DiningTable) qryDiningTable.getSingleResult()));
    } catch (NoResultException ex) {
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_NOENCONTRADO,
          "No table found with the provided ID.",
          "getDiningTable NoResultException");
    } catch (NonUniqueResultException ex) {
      LOG.log(Level.SEVERE, "An error occurred while querying the dining table.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while querying the dining table.",
          "getDiningTable NonUniqueResultException");
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "An error occurred while querying the dining table.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while querying the dining table.",
          "getDiningTable " + ex.getMessage());
    }
  }

  public Respuesta getDiningTables() {
    try {
      Query query = em.createNamedQuery("DiningTable.findAll", DiningTable.class);
      List<DiningTable> diningTables = (List<DiningTable>) query.getResultList();
      List<DiningTableDTO> diningTablesDto = new ArrayList<>();
      for (DiningTable diningTable : diningTables) {
        diningTablesDto.add(new DiningTableDTO(diningTable));
      }

      return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "DiningTables", diningTablesDto);
    } catch (NoResultException ex) {
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_NOENCONTRADO,
          "No dining tables found.",
          "getDiningTables NoResultException");
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "An error occurred while querying dining tables.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while querying dining tables.",
          "getDiningTables " + ex.getMessage());
    }
  }

  public Respuesta guardarDiningTable(DiningTableDTO diningTableDto) {
    try {
      // Validate required FK before touching the DB to avoid ORA-01400
      if (diningTableDto.getDiningAreaId() == null) {
        return new Respuesta(
            false,
            CodigoRespuesta.ERROR_CLIENTE,
            "Dining area ID (diningAreaId) is required.",
            "guardarDiningTable Validation");
      }

      DiningArea diningArea = em.find(DiningArea.class, diningTableDto.getDiningAreaId());
      if (diningArea == null) {
        return new Respuesta(
            false,
            CodigoRespuesta.ERROR_NOENCONTRADO,
            "No dining area found with the specified ID.",
            "guardarDiningTable Validation");
      }

      DiningTable diningTable;
      if (diningTableDto.getId() != null && diningTableDto.getId() > 0) {
        diningTable = em.find(DiningTable.class, diningTableDto.getId());
        if (diningTable == null) {
          return new Respuesta(
              false,
              CodigoRespuesta.ERROR_NOENCONTRADO,
              "No table found to modify.",
              "guardarDiningTable NoResultException");
        }
        diningTable.actualizar(diningTableDto);
        diningTable.setDiningAreaId(diningArea);
        diningTable = em.merge(diningTable);
      } else {
        diningTable = new DiningTable(diningTableDto);
        if (diningTable.getStatus() == null) {
          diningTable.setStatus("FREE");
        }
        diningTable.setDiningAreaId(diningArea);
        em.persist(diningTable);
      }
      em.flush();
      return new Respuesta(
          true, CodigoRespuesta.CORRECTO, "", "", "DiningTable", new DiningTableDTO(diningTable));
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "An error occurred while saving the dining table.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while saving the dining table.",
          "guardarDiningTable " + ex.getMessage());
    }
  }

  public Respuesta eliminarDiningTable(Long id) {
    try {
      DiningTable diningTable;
      if (id != null && id > 0) {
        diningTable = em.find(DiningTable.class, id);
        if (diningTable == null) {
          return new Respuesta(
              false,
              CodigoRespuesta.ERROR_NOENCONTRADO,
              "No table found to delete.",
              "eliminarDiningTable NoResultException");
        }
        em.remove(diningTable);
      } else {
        return new Respuesta(
            false,
            CodigoRespuesta.ERROR_NOENCONTRADO,
            "You must provide the table to delete.",
            "eliminarDiningTable NoResultException");
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
            "Cannot delete the table because it has related records.",
            "eliminarDiningTable " + ex.getMessage());
      }
      LOG.log(Level.SEVERE, "An error occurred while deleting the dining table.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while deleting the dining table.",
          "eliminarDiningTable " + ex.getMessage());
    }
  }
}
