package org.univaq.swa.eventsrest.security;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;
import jakarta.ws.rs.core.UriInfo;
import java.util.Map;

/**
 *
 * @author Giuseppe Della Penna
 */
@Path("auth")
public class AuthenticationRes {

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@Context UriInfo uriinfo, Map<String, String> credentials) {
        try {

            if (AuthHelpers.getInstance().authenticateUser(credentials.getOrDefault("username", ""), credentials.getOrDefault("password", ""))) {
                String authToken = AuthHelpers.getInstance().issueToken(uriinfo, credentials.get("username"));
                return Response.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken).build();
                //Possiamo restituire il token in varie modalità, giusto per fare un esempio...
                //.ok(authToken)
                //.cookie(new NewCookie.Builder("token").value(authToken).build())
            }
        } catch (Exception e) {
            //logging dell'errore 
        }
        return Response.status(UNAUTHORIZED).build();
    }

    //un altro modo per ricevere e iniettare i parametri con JAX-RS...
    @POST
    @Path("login2")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(@Context UriInfo uriinfo,
            @FormParam("username") String username,
            @FormParam("password") String password) {
        try {

            if (AuthHelpers.getInstance().authenticateUser(username, password)) {
                String authToken = AuthHelpers.getInstance().issueToken(uriinfo, username);

                return Response.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken).build();
                //Possiamo restituire il token in varie modalità, giusto per fare un esempio...
                //.ok(authToken)
                //.cookie(new NewCookie.Builder("token").value(authToken).build())

            }
        } catch (Exception e) {
            //logging dell'errore 
        }
        return Response.status(UNAUTHORIZED).build();
    }

    @DELETE
    @Path("logout")
    @Logged
    public Response logout(@Context ContainerRequestContext req) {
        //proprietà estratta dall'authorization header 
        //e iniettata nella request dal filtro di autenticazione
        String token = (String) req.getProperty("token");
        AuthHelpers.getInstance().revokeToken(token);
        return Response.noContent()
                //eliminaimo anche il cookie con il token
                .cookie(new NewCookie.Builder("token").value("").maxAge(0).build())
                .build();
    }

    //Metodo per fare "refresh" del token senza ritrasmettere le credenziali
    @GET
    @Path("refresh")
    @Logged
    public Response refresh(@Context ContainerRequestContext req, @Context UriInfo uriinfo) {
        //proprietà iniettata nella request dal filtro di autenticazione
        String username = (String) req.getProperty("user");
        String newtoken = AuthHelpers.getInstance().issueToken(uriinfo, username);
        return Response.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + newtoken).build();
        //Possiamo restituire il token in varie modalità, giusto per fare un esempio...
        //.ok(newtoken)
        //.cookie(new NewCookie.Builder("token").value(newtoken).build())

    }
}
