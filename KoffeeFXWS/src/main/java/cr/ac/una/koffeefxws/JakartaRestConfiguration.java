package cr.ac.una.koffeefxws;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;
/**
 * Configures Jakarta RESTful Web Services for the application.
 * @author Juneau
 */
@ApplicationPath("ws")
public class JakartaRestConfiguration extends Application {
    public JakartaRestConfiguration(){
            //super();
            //packages("cr.ac.una.unaplanillaws.controller",
                    //"io.swagger.v3.jaxrs2.integration.resources");
        }
}
