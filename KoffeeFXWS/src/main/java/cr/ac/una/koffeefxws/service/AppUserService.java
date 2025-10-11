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
            Query qryEmpleado = em.createNamedQuery("AppUser.findById", AppUser.class);
            qryEmpleado.setParameter("id", id);
            // TODO: return new Respuesta(true, CodigoRespuesta.CORRECTO, "" "", "AppUser", new AppUserDTO
        }
    }
}
