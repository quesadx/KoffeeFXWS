/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.koffeefxws.service;

import cr.ac.una.koffeefxws.model.Role;
import cr.ac.una.koffeefxws.model.RoleDTO;
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
public class RoleService {
    
    private static final Logger LOG = Logger.getLogger(RoleService.class.getName());
    
    @PersistenceContext(unitName = "KoffeeFXWSPU")
    private EntityManager em;
    
    public Respuesta getRole(Long id) {
        try {
            Query qryRole = em.createNamedQuery("Role.findById", Role.class);
            qryRole.setParameter("id", id);

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Role", new RoleDTO((Role) qryRole.getSingleResult()));

        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existe un rol con el código ingresado.", "getRole NoResultException");
        } catch (NonUniqueResultException ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar el rol.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar el rol.", "getRole NonUniqueResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar el rol.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar el rol.", "getRole " + ex.getMessage());
        }
    }

    public Respuesta getRoleByCode(String code) {
        try {
            Query qryRole = em.createNamedQuery("Role.findByCode", Role.class);
            qryRole.setParameter("code", code);

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Role", new RoleDTO((Role) qryRole.getSingleResult()));

        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existe un rol con el código ingresado.", "getRoleByCode NoResultException");
        } catch (NonUniqueResultException ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar el rol.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar el rol.", "getRoleByCode NonUniqueResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar el rol.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar el rol.", "getRoleByCode " + ex.getMessage());
        }
    }

    public Respuesta getRoles() {
        try {
            Query query = em.createNamedQuery("Role.findAll", Role.class);
            List<Role> roles = (List<Role>) query.getResultList();
            List<RoleDTO> rolesDto = new ArrayList<>();
            for (Role role : roles) {
                rolesDto.add(new RoleDTO(role));
            }

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Roles", rolesDto);

        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existen roles registrados.", "getRoles NoResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar los roles.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar los roles.", "getRoles " + ex.getMessage());
        }
    }

    public Respuesta guardarRole(RoleDTO roleDto) {
        try {
            Role role;
            if (roleDto.getId() != null && roleDto.getId() > 0) {
                role = em.find(Role.class, roleDto.getId());
                if (role == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el rol a modificar.", "guardarRole NoResultException");
                }
                role.actualizar(roleDto);
                role = em.merge(role);
            } else {
                role = new Role(roleDto);
                em.persist(role);
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Role", new RoleDTO(role));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al guardar el rol.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al guardar el rol.", "guardarRole " + ex.getMessage());
        }
    }

    public Respuesta eliminarRole(Long id) {
        try {
            Role role;
            if (id != null && id > 0) {
                role = em.find(Role.class, id);
                if (role == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el rol a eliminar.", "eliminarRole NoResultException");
                }
                em.remove(role);
            } else {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "Debe cargar el rol a eliminar.", "eliminarRole NoResultException");
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
        } catch (Exception ex) {
            if (ex.getCause() != null && ex.getCause().getCause().getClass() == SQLIntegrityConstraintViolationException.class) {
                return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "No se puede eliminar el rol porque tiene relaciones con otros registros.", "eliminarRole " + ex.getMessage());
            }
            LOG.log(Level.SEVERE, "Ocurrió un error al eliminar el rol.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al eliminar el rol.", "eliminarRole " + ex.getMessage());
        }
    }
}
