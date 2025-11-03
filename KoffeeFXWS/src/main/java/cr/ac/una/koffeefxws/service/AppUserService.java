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

import cr.ac.una.koffeefxws.model.AppUser;
import cr.ac.una.koffeefxws.model.AppUserDTO;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;

/**
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

      if (user.getPassword().equals(password)) {
        return new Respuesta(
            true, CodigoRespuesta.CORRECTO, "", "", "AppUser", new AppUserDTO(user));
      } else {
        return new Respuesta(
            false,
            CodigoRespuesta.ERROR_ACCESO,
            "Incorrect credentials.",
            "validateUser password mismatch");
      }
    } catch (NoResultException ex) {
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_NOENCONTRADO,
          "No user exists with the provided credentials.",
          "validateUser NoResultException");
    } catch (NonUniqueResultException ex) {
      LOG.log(Level.SEVERE, "An error occurred while querying the user.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while querying the user.",
          "validateUser NonUniqueResultException");
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "An error occurred while querying the user.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while querying the user.",
          "validateUser " + ex.getMessage());
    }
  }

  public Respuesta getAppUser(Long id) {
    try {
      Query qryAppUser = em.createNamedQuery("AppUser.findById", AppUser.class);
      qryAppUser.setParameter("id", id);

      return new Respuesta(
          true,
          CodigoRespuesta.CORRECTO,
          "",
          "",
          "AppUser",
          new AppUserDTO((AppUser) qryAppUser.getSingleResult()));
    } catch (NoResultException ex) {
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_NOENCONTRADO,
          "No user exists with the provided ID.",
          "getAppUser NoResultException");
    } catch (NonUniqueResultException ex) {
      LOG.log(Level.SEVERE, "An error occurred while querying the user.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while querying the user.",
          "getAppUser NonUniqueResultException");
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "An error occurred while querying the user.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while querying the user.",
          "getAppUser " + ex.getMessage());
    }
  }

  public Respuesta getAppUserByUsername(String username) {
    try {
      Query qryAppUser = em.createNamedQuery("AppUser.findByUsername", AppUser.class);
      qryAppUser.setParameter("username", username);

      return new Respuesta(
          true,
          CodigoRespuesta.CORRECTO,
          "",
          "",
          "AppUser",
          new AppUserDTO((AppUser) qryAppUser.getSingleResult()));
    } catch (NoResultException ex) {
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_NOENCONTRADO,
          "No user exists with the provided username.",
          "getAppUserByUsername NoResultException");
    } catch (NonUniqueResultException ex) {
      LOG.log(Level.SEVERE, "An error occurred while querying the user.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while querying the user.",
          "getAppUserByUsername NonUniqueResultException");
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "An error occurred while querying the user.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while querying the user.",
          "getAppUserByUsername " + ex.getMessage());
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
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_NOENCONTRADO,
          "No users registered.",
          "getAppUsers NoResultException");
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "An error occurred while querying users.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while querying users.",
          "getAppUsers " + ex.getMessage());
    }
  }

  public Respuesta guardarAppUser(AppUserDTO userDto) {
    try {
      AppUser user;
      if (userDto.getId() != null && userDto.getId() > 0) {
        user = em.find(AppUser.class, userDto.getId());
        if (user == null) {
          return new Respuesta(
              false,
              CodigoRespuesta.ERROR_NOENCONTRADO,
              "User to modify was not found.",
              "guardarAppUser NoResultException");
        }
        user.actualizar(userDto);
        user.setUserRole(userDto.getUserRole());

        user = em.merge(user);
      } else {
        user = new AppUser(userDto);
        user.setCreationDate(LocalDate.now());

        em.persist(user);
      }
      em.flush();
      return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "AppUser", new AppUserDTO(user));
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "An error occurred while saving the user.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while saving the user.",
          "guardarAppUser " + ex.getMessage());
    }
  }

  public Respuesta eliminarAppUser(Long id) {
    try {
      AppUser user;
      if (id != null && id > 0) {
        user = em.find(AppUser.class, id);
        if (user == null) {
          return new Respuesta(
              false,
              CodigoRespuesta.ERROR_NOENCONTRADO,
              "User to delete was not found.",
              "eliminarAppUser NoResultException");
        }
        em.remove(user);
      } else {
        return new Respuesta(
            false,
            CodigoRespuesta.ERROR_NOENCONTRADO,
            "You must provide the user to delete.",
            "eliminarAppUser NoResultException");
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
            "Cannot delete the user because it has relations with other records.",
            "eliminarAppUser " + ex.getMessage());
      }
      LOG.log(Level.SEVERE, "An error occurred while deleting the user.", ex);
      return new Respuesta(
          false,
          CodigoRespuesta.ERROR_INTERNO,
          "An error occurred while deleting the user.",
          "eliminarAppUser " + ex.getMessage());
    }
  }
}
