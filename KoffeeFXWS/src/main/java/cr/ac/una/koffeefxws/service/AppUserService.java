package cr.ac.una.koffeefxws.service;

import cr.ac.una.koffeefxws.model.AppUser;
import cr.ac.una.koffeefxws.model.AppUserDTO;
import cr.ac.una.koffeefxws.model.Role;
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
public class AppUserService {

    private static final Logger LOG = Logger.getLogger(AppUserService.class.getName());

    @PersistenceContext(unitName = "KoffeeFXWSPU")
    private EntityManager em;

    public Respuesta validateUser(String username, String password) {
        try {
            Query qryUser = em.createNamedQuery("AppUser.findByUsername", AppUser.class);
            qryUser.setParameter("username", username);

            AppUser user = (AppUser) qryUser.getSingleResult();
            
            // TODO: Implement proper password hashing verification
            if (user.getPassword().equals(password)) {
                return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "AppUser", new AppUserDTO(user));
            } else {
                return new Respuesta(false, CodigoRespuesta.ERROR_ACCESO, "Credenciales incorrectas.", "validateUser password mismatch");
            }

        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existe un usuario con las credenciales ingresadas.", "validateUser NoResultException");
        } catch (NonUniqueResultException ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar el usuario.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar el usuario.", "validateUser NonUniqueResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar el usuario.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar el usuario.", "validateUser " + ex.getMessage());
        }
    }

    public Respuesta getAppUser(Long id) {
        try {
            Query qryAppUser = em.createNamedQuery("AppUser.findById", AppUser.class);
            qryAppUser.setParameter("id", id);

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "AppUser", new AppUserDTO((AppUser) qryAppUser.getSingleResult()));

        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existe un usuario con el código ingresado.", "getAppUser NoResultException");
        } catch (NonUniqueResultException ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar el usuario.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar el usuario.", "getAppUser NonUniqueResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar el usuario.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar el usuario.", "getAppUser " + ex.getMessage());
        }
    }

    public Respuesta getAppUserByUsername(String username) {
        try {
            Query qryAppUser = em.createNamedQuery("AppUser.findByUsername", AppUser.class);
            qryAppUser.setParameter("username", username);

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "AppUser", new AppUserDTO((AppUser) qryAppUser.getSingleResult()));

        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existe un usuario con el username ingresado.", "getAppUserByUsername NoResultException");
        } catch (NonUniqueResultException ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar el usuario.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar el usuario.", "getAppUserByUsername NonUniqueResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar el usuario.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar el usuario.", "getAppUserByUsername " + ex.getMessage());
        }
    }

    public Respuesta getAppUsers() {
        try {
            Query query = em.createNamedQuery("AppUser.findAll", AppUser.class);
            List<AppUser> users = (List<AppUser>) query.getResultList();
            List<AppUserDTO> usersDto = new ArrayList<>();
            for (AppUser user : users) {
                usersDto.add(new AppUserDTO(user));
            }

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "AppUsers", usersDto);

        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existen usuarios registrados.", "getAppUsers NoResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar los usuarios.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar los usuarios.", "getAppUsers " + ex.getMessage());
        }
    }

    public Respuesta guardarAppUser(AppUserDTO userDto) {
        try {
            AppUser user;
            if (userDto.getId() != null && userDto.getId() > 0) {
                user = em.find(AppUser.class, userDto.getId());
                if (user == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el usuario a modificar.", "guardarAppUser NoResultException");
                }
                user.setFirstName(userDto.getFirstName());
                user.setLastName(userDto.getLastName());
                user.setUsername(userDto.getUsername());
                user.setEmail(userDto.getEmail());
                user.setIsActive(userDto.getIsActive() ? 'Y' : 'N');
                // TODO: Implement proper password hashing
                if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
                    user.setPassword(userDto.getPassword());
                }
                
                if (userDto.getRoleId() != null) {
                    Role role = em.find(Role.class, userDto.getRoleId());
                    if (role != null) {
                        user.setRoleId(role);
                    }
                }
                
                user = em.merge(user);
            } else {
                user = new AppUser();
                // ID is auto-generated, don't set it manually
                user.setFirstName(userDto.getFirstName());
                user.setLastName(userDto.getLastName());
                user.setUsername(userDto.getUsername());
                user.setPassword(userDto.getPassword()); // TODO: Hash password
                user.setEmail(userDto.getEmail());
                user.setIsActive(userDto.getIsActive() != null && userDto.getIsActive() ? 'Y' : 'N');
                user.setCreationDate(LocalDate.now());
                
                if (userDto.getRoleId() != null) {
                    Role role = em.find(Role.class, userDto.getRoleId());
                    if (role != null) {
                        user.setRoleId(role);
                    }
                }
                
                em.persist(user);
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "AppUser", new AppUserDTO(user));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al guardar el usuario.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al guardar el usuario.", "guardarAppUser " + ex.getMessage());
        }
    }

    public Respuesta eliminarAppUser(Long id) {
        try {
            AppUser user;
            if (id != null && id > 0) {
                user = em.find(AppUser.class, id);
                if (user == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el usuario a eliminar.", "eliminarAppUser NoResultException");
                }
                em.remove(user);
            } else {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "Debe cargar el usuario a eliminar.", "eliminarAppUser NoResultException");
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
        } catch (Exception ex) {
            if (ex.getCause() != null && ex.getCause().getCause().getClass() == SQLIntegrityConstraintViolationException.class) {
                return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "No se puede eliminar el usuario porque tiene relaciones con otros registros.", "eliminarAppUser " + ex.getMessage());
            }
            LOG.log(Level.SEVERE, "Ocurrió un error al eliminar el usuario.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al eliminar el usuario.", "eliminarAppUser " + ex.getMessage());
        }
    }
}
