package cr.ac.una.koffeefxws.service;

import java.util.logging.Logger;

import cr.ac.una.koffeefxws.model.AppUser;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.PersistenceContext;

/**
 *
 * @author quesadx
 */
@Stateless
@LocalBean
public class AppUserService {

     // Instancia de log que vamos a usar para el log de errores en Payara
    private static final Logger LOG = Logger.getLogger(AppUserService.class.getName());

    // Vamos a inyectar una instancia con la anotación
    @PersistenceContext(unitName = "KoffeeFXWSPU")
    private EntityManager em;

    public Respuesta getAppUser(Long id) {
        try {
            Query qryAppUser = em.createNamedQuery("AppUser.findById", AppUser.class);
            qryAppUser.setParameter("id", id);
            
            AppUser appUser = (AppUser) qryAppUser.getSingleResult();
            
            // TODO: Implement AppUserDTO conversion when DTO mappers are ready
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "AppUser", appUser);
        } catch (Exception ex) {
            LOG.severe("Error getting AppUser by id: " + ex.getMessage());
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Error getting user", "getAppUser " + ex.getMessage());
        }
    }
}
