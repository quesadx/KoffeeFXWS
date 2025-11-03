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
import cr.ac.una.koffeefxws.model.DiningAreaDTO;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;

/**
 * @author quesadx
 */
@Stateless
@LocalBean
public class DiningAreaService {

    private static final Logger LOG = Logger.getLogger(DiningAreaService.class.getName());

    @PersistenceContext(unitName = "KoffeeFXWSPU")
    private EntityManager em;

    public Respuesta getDiningArea(Long id) {
        try {
            Query qryDiningArea = em.createNamedQuery("DiningArea.findById", DiningArea.class);
            qryDiningArea.setParameter("id", id);

            return new Respuesta(
                    true,
                    CodigoRespuesta.CORRECTO,
                    "",
                    "",
                    "DiningArea",
                    new DiningAreaDTO((DiningArea) qryDiningArea.getSingleResult()));
        } catch (NoResultException ex) {
            return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No existe un área de comedor con el código ingresado.",
                    "getDiningArea NoResultException");
        } catch (NonUniqueResultException ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar el área de comedor.", ex);
            return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al consultar el área de comedor.",
                    "getDiningArea NonUniqueResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar el área de comedor.", ex);
            return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al consultar el área de comedor.",
                    "getDiningArea " + ex.getMessage());
        }
    }

    public Respuesta getDiningAreas() {
        try {
            Query query = em.createNamedQuery("DiningArea.findAll", DiningArea.class);
            List<DiningArea> diningAreas = (List<DiningArea>) query.getResultList();
            List<DiningAreaDTO> diningAreasDto = new ArrayList<>();
            for (DiningArea diningArea : diningAreas) {
                diningAreasDto.add(new DiningAreaDTO(diningArea));
            }

            return new Respuesta(
                    true, CodigoRespuesta.CORRECTO, "", "", "DiningAreas", diningAreasDto);
        } catch (NoResultException ex) {
            return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No existen áreas de comedor registradas.",
                    "getDiningAreas NoResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar las áreas de comedor.", ex);
            return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al consultar las áreas de comedor.",
                    "getDiningAreas " + ex.getMessage());
        }
    }

    public Respuesta getActiveDiningAreas() {
        try {
            Query query = em.createNamedQuery("DiningArea.findByIsActive", DiningArea.class);
            query.setParameter("isActive", 'Y');
            List<DiningArea> diningAreas = (List<DiningArea>) query.getResultList();
            List<DiningAreaDTO> diningAreasDto = new ArrayList<>();
            for (DiningArea diningArea : diningAreas) {
                diningAreasDto.add(new DiningAreaDTO(diningArea));
            }

            return new Respuesta(
                    true, CodigoRespuesta.CORRECTO, "", "", "DiningAreas", diningAreasDto);
        } catch (NoResultException ex) {
            return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No existen áreas de comedor activas.",
                    "getActiveDiningAreas NoResultException");
        } catch (Exception ex) {
            LOG.log(
                    Level.SEVERE,
                    "Ocurrió un error al consultar las áreas de comedor activas.",
                    ex);
            return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al consultar las áreas de comedor activas.",
                    "getActiveDiningAreas " + ex.getMessage());
        }
    }

    public Respuesta guardarDiningArea(DiningAreaDTO diningAreaDto) {
        try {
            DiningArea diningArea;
            if (diningAreaDto.getId() != null && diningAreaDto.getId() > 0) {
                diningArea = em.find(DiningArea.class, diningAreaDto.getId());
                if (diningArea == null) {
                    return new Respuesta(
                            false,
                            CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró el área de comedor a modificar.",
                            "guardarDiningArea NoResultException");
                }
                diningArea.actualizar(diningAreaDto);
                diningArea = em.merge(diningArea);
            } else {
                diningArea = new DiningArea(diningAreaDto);
                em.persist(diningArea);
            }
            em.flush();
            return new Respuesta(
                    true,
                    CodigoRespuesta.CORRECTO,
                    "",
                    "",
                    "DiningArea",
                    new DiningAreaDTO(diningArea));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al guardar el área de comedor.", ex);
            return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al guardar el área de comedor.",
                    "guardarDiningArea " + ex.getMessage());
        }
    }

    public Respuesta eliminarDiningArea(Long id) {
        try {
            DiningArea diningArea;
            if (id != null && id > 0) {
                diningArea = em.find(DiningArea.class, id);
                if (diningArea == null) {
                    return new Respuesta(
                            false,
                            CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró el área de comedor a eliminar.",
                            "eliminarDiningArea NoResultException");
                }
                em.remove(diningArea);
            } else {
                return new Respuesta(
                        false,
                        CodigoRespuesta.ERROR_NOENCONTRADO,
                        "Debe cargar el área de comedor a eliminar.",
                        "eliminarDiningArea NoResultException");
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
                        "No se puede eliminar el área de comedor porque tiene relaciones con otros registros.",
                        "eliminarDiningArea " + ex.getMessage());
            }
            LOG.log(Level.SEVERE, "Ocurrió un error al eliminar el área de comedor.", ex);
            return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al eliminar el área de comedor.",
                    "eliminarDiningArea " + ex.getMessage());
        }
    }
}
