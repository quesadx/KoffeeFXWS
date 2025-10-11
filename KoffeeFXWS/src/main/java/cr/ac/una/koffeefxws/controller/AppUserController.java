package cr.ac.una.koffeefxws.controller;

import java.util.logging.Logger;

import cr.ac.una.koffeefxws.model.AppUserDTO;
import cr.ac.una.koffeefxws.service.AppUserService;
import cr.ac.una.koffeefxws.util.CodigoRespuesta;
import cr.ac.una.koffeefxws.util.Respuesta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Path;

@Path("AppUserController")
@Tag(name = "AppUserController", description = "Endpoints para gestionar usuarios de la aplicación")
public class AppUserController {

    @EJB
    AppUserService appUserService;

    @GET
    @Path("/AppUser/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene un usuario de la aplicación por su ID")
    public Response getAppUser(@PathParam("id") Long id) {
        try {
            Respuesta r = appUserService.getAppUser(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r).build();
            }
            return Response.ok((AppUserDTO) r.getResultado("AppUser")).build();
        } catch (Exception ex) {
            Logger.getLogger(AppUserController.class.getName()).log(java.util.logging.Level.SEVERE, "Ocurrió un error al ejecutar el servicio REST en getAppUser {id}", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Ocurrió un error al ejecutar el servicio REST en getAppUser " + id).build();
        }
    } 
}
