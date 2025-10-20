/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.koffeefxws.service;

import cr.ac.una.koffeefxws.model.AppUser;
import cr.ac.una.koffeefxws.model.CashOpening;
import cr.ac.una.koffeefxws.model.CashOpeningDTO;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.time.LocalDate;
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
public class CashOpeningService {
    
    private static final Logger LOG = Logger.getLogger(CashOpeningService.class.getName());
    
    @PersistenceContext(unitName = "KoffeeFXWSPU")
    private EntityManager em;
    
    public Respuesta getCashOpening(Long id) {
        try {
            Query qryCashOpening = em.createNamedQuery("CashOpening.findById", CashOpening.class);
            qryCashOpening.setParameter("id", id);

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "CashOpening", new CashOpeningDTO((CashOpening) qryCashOpening.getSingleResult()));

        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existe una apertura de caja con el código ingresado.", "getCashOpening NoResultException");
        } catch (NonUniqueResultException ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar la apertura de caja.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar la apertura de caja.", "getCashOpening NonUniqueResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar la apertura de caja.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar la apertura de caja.", "getCashOpening " + ex.getMessage());
        }
    }

    public Respuesta getCashOpenings() {
        try {
            Query query = em.createNamedQuery("CashOpening.findAll", CashOpening.class);
            List<CashOpening> cashOpenings = (List<CashOpening>) query.getResultList();
            List<CashOpeningDTO> cashOpeningsDto = new ArrayList<>();
            for (CashOpening cashOpening : cashOpenings) {
                cashOpeningsDto.add(new CashOpeningDTO(cashOpening));
            }

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "CashOpenings", cashOpeningsDto);

        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existen aperturas de caja registradas.", "getCashOpenings NoResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar las aperturas de caja.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar las aperturas de caja.", "getCashOpenings " + ex.getMessage());
        }
    }

    public Respuesta getActiveCashOpening(Long userId) {
        try {
            Query query = em.createQuery("SELECT c FROM CashOpening c WHERE c.userId.id = :userId AND c.isClosed = 'N' ORDER BY c.openingDate DESC", CashOpening.class);
            query.setParameter("userId", userId);
            query.setMaxResults(1);
            
            List<CashOpening> results = query.getResultList();
            if (results.isEmpty()) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No hay una caja abierta para este usuario.", "getActiveCashOpening NoResultException");
            }

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "CashOpening", new CashOpeningDTO(results.get(0)));

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar la caja activa.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar la caja activa.", "getActiveCashOpening " + ex.getMessage());
        }
    }

    public Respuesta guardarCashOpening(CashOpeningDTO cashOpeningDto) {
        try {
            if (cashOpeningDto.getUserId() == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "El ID del usuario es obligatorio.", "guardarCashOpening Validation");
            }

            AppUser user = em.find(AppUser.class, cashOpeningDto.getUserId());
            if (user == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existe un usuario con el ID indicado.", "guardarCashOpening Validation");
            }

            CashOpening cashOpening;
            if (cashOpeningDto.getId() != null && cashOpeningDto.getId() > 0) {
                cashOpening = em.find(CashOpening.class, cashOpeningDto.getId());
                if (cashOpening == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró la apertura de caja a modificar.", "guardarCashOpening NoResultException");
                }
                cashOpening.actualizar(cashOpeningDto);
                cashOpening = em.merge(cashOpening);
            } else {
                cashOpening = new CashOpening(cashOpeningDto);
                if (cashOpening.getOpeningDate() == null) {
                    cashOpening.setOpeningDate(LocalDate.now());
                }
                if (cashOpening.getIsClosed() == null) {
                    cashOpening.setIsClosed('N');
                }
                cashOpening.setUserId(user);
                em.persist(cashOpening);
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "CashOpening", new CashOpeningDTO(cashOpening));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al guardar la apertura de caja.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al guardar la apertura de caja.", "guardarCashOpening " + ex.getMessage());
        }
    }

    public Respuesta closeCashOpening(Long id, Long closingAmount, String notes) {
        try {
            CashOpening cashOpening = em.find(CashOpening.class, id);
            if (cashOpening == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró la apertura de caja.", "closeCashOpening NoResultException");
            }

            if (cashOpening.getIsClosed() != null && cashOpening.getIsClosed().equals('Y')) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "Esta caja ya está cerrada.", "closeCashOpening Validation");
            }

            cashOpening.setIsClosed('Y');
            cashOpening.setClosingDate(LocalDate.now());
            cashOpening.setClosingAmount(closingAmount);
            if (notes != null) {
                cashOpening.setNotes(notes);
            }
            
            cashOpening = em.merge(cashOpening);
            em.flush();
            
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "CashOpening", new CashOpeningDTO(cashOpening));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al cerrar la caja.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al cerrar la caja.", "closeCashOpening " + ex.getMessage());
        }
    }

    public Respuesta eliminarCashOpening(Long id) {
        try {
            CashOpening cashOpening;
            if (id != null && id > 0) {
                cashOpening = em.find(CashOpening.class, id);
                if (cashOpening == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró la apertura de caja a eliminar.", "eliminarCashOpening NoResultException");
                }
                em.remove(cashOpening);
            } else {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "Debe cargar la apertura de caja a eliminar.", "eliminarCashOpening NoResultException");
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al eliminar la apertura de caja.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al eliminar la apertura de caja.", "eliminarCashOpening " + ex.getMessage());
        }
    }
}
