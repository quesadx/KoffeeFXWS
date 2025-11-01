/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.koffeefxws.service;

import cr.ac.una.koffeefxws.model.SystemParameter;
import cr.ac.una.koffeefxws.model.SystemParameterDTO;
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
public class SystemParameterService {

    private static final Logger LOG = Logger.getLogger(
        SystemParameterService.class.getName()
    );

    @PersistenceContext(unitName = "KoffeeFXWSPU")
    private EntityManager em;

    public Respuesta getSystemParameter(Long id) {
        try {
            Query qryParam = em.createNamedQuery(
                "SystemParameter.findById",
                SystemParameter.class
            );
            qryParam.setParameter("id", id);

            return new Respuesta(
                true,
                CodigoRespuesta.CORRECTO,
                "",
                "",
                "SystemParameter",
                new SystemParameterDTO(
                    (SystemParameter) qryParam.getSingleResult()
                )
            );
        } catch (NoResultException ex) {
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_NOENCONTRADO,
                "No existe un parámetro con el código ingresado.",
                "getSystemParameter NoResultException"
            );
        } catch (NonUniqueResultException ex) {
            LOG.log(
                Level.SEVERE,
                "Ocurrió un error al consultar el parámetro del sistema.",
                ex
            );
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_INTERNO,
                "Ocurrió un error al consultar el parámetro del sistema.",
                "getSystemParameter NonUniqueResultException"
            );
        } catch (Exception ex) {
            LOG.log(
                Level.SEVERE,
                "Ocurrió un error al consultar el parámetro del sistema.",
                ex
            );
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_INTERNO,
                "Ocurrió un error al consultar el parámetro del sistema.",
                "getSystemParameter " + ex.getMessage()
            );
        }
    }

    public Respuesta getSystemParameterByName(String paramName) {
        try {
            Query qryParam = em.createNamedQuery(
                "SystemParameter.findByParamName",
                SystemParameter.class
            );
            qryParam.setParameter("paramName", paramName);

            return new Respuesta(
                true,
                CodigoRespuesta.CORRECTO,
                "",
                "",
                "SystemParameter",
                new SystemParameterDTO(
                    (SystemParameter) qryParam.getSingleResult()
                )
            );
        } catch (NoResultException ex) {
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_NOENCONTRADO,
                "No existe un parámetro con el nombre ingresado.",
                "getSystemParameterByName NoResultException"
            );
        } catch (NonUniqueResultException ex) {
            LOG.log(
                Level.SEVERE,
                "Ocurrió un error al consultar el parámetro del sistema.",
                ex
            );
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_INTERNO,
                "Ocurrió un error al consultar el parámetro del sistema.",
                "getSystemParameterByName NonUniqueResultException"
            );
        } catch (Exception ex) {
            LOG.log(
                Level.SEVERE,
                "Ocurrió un error al consultar el parámetro del sistema.",
                ex
            );
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_INTERNO,
                "Ocurrió un error al consultar el parámetro del sistema.",
                "getSystemParameterByName " + ex.getMessage()
            );
        }
    }

    public Respuesta getSystemParameters() {
        try {
            Query query = em.createNamedQuery(
                "SystemParameter.findAll",
                SystemParameter.class
            );
            List<SystemParameter> parameters = (List<
                SystemParameter
            >) query.getResultList();
            List<SystemParameterDTO> parametersDto = new ArrayList<>();
            for (SystemParameter parameter : parameters) {
                parametersDto.add(new SystemParameterDTO(parameter));
            }

            return new Respuesta(
                true,
                CodigoRespuesta.CORRECTO,
                "",
                "",
                "SystemParameters",
                parametersDto
            );
        } catch (NoResultException ex) {
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_NOENCONTRADO,
                "No existen parámetros del sistema registrados.",
                "getSystemParameters NoResultException"
            );
        } catch (Exception ex) {
            LOG.log(
                Level.SEVERE,
                "Ocurrió un error al consultar los parámetros del sistema.",
                ex
            );
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_INTERNO,
                "Ocurrió un error al consultar los parámetros del sistema.",
                "getSystemParameters " + ex.getMessage()
            );
        }
    }

    public Respuesta guardarSystemParameter(SystemParameterDTO parameterDto) {
        try {
            SystemParameter parameter;
            if (parameterDto.getId() != null && parameterDto.getId() > 0) {
                parameter = em.find(
                    SystemParameter.class,
                    parameterDto.getId()
                );
                if (parameter == null) {
                    return new Respuesta(
                        false,
                        CodigoRespuesta.ERROR_NOENCONTRADO,
                        "No se encontró el parámetro del sistema a modificar.",
                        "guardarSystemParameter NoResultException"
                    );
                }
                parameter.actualizar(parameterDto);
                parameter = em.merge(parameter);
            } else {
                parameter = new SystemParameter(parameterDto);
                em.persist(parameter);
            }
            em.flush();
            return new Respuesta(
                true,
                CodigoRespuesta.CORRECTO,
                "",
                "",
                "SystemParameter",
                new SystemParameterDTO(parameter)
            );
        } catch (Exception ex) {
            LOG.log(
                Level.SEVERE,
                "Ocurrió un error al guardar el parámetro del sistema.",
                ex
            );
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_INTERNO,
                "Ocurrió un error al guardar el parámetro del sistema.",
                "guardarSystemParameter " + ex.getMessage()
            );
        }
    }

    public Respuesta eliminarSystemParameter(Long id) {
        try {
            SystemParameter parameter;
            if (id != null && id > 0) {
                parameter = em.find(SystemParameter.class, id);
                if (parameter == null) {
                    return new Respuesta(
                        false,
                        CodigoRespuesta.ERROR_NOENCONTRADO,
                        "No se encontró el parámetro del sistema a eliminar.",
                        "eliminarSystemParameter NoResultException"
                    );
                }
                em.remove(parameter);
            } else {
                return new Respuesta(
                    false,
                    CodigoRespuesta.ERROR_NOENCONTRADO,
                    "Debe cargar el parámetro del sistema a eliminar.",
                    "eliminarSystemParameter NoResultException"
                );
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
        } catch (Exception ex) {
            LOG.log(
                Level.SEVERE,
                "Ocurrió un error al eliminar el parámetro del sistema.",
                ex
            );
            return new Respuesta(
                false,
                CodigoRespuesta.ERROR_INTERNO,
                "Ocurrió un error al eliminar el parámetro del sistema.",
                "eliminarSystemParameter " + ex.getMessage()
            );
        }
    }
}
