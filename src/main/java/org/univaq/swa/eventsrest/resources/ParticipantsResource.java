package org.univaq.swa.eventsrest.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import org.univaq.swa.eventsrest.DatabaseException;
import org.univaq.swa.eventsrest.NotFoundException;
import org.univaq.swa.eventsrest.business.EventsService;
import org.univaq.swa.eventsrest.business.EventsServiceFactory;
import org.univaq.swa.eventsrest.model.Event;
import org.univaq.swa.eventsrest.model.Participant;
import org.univaq.swa.eventsrest.security.Logged;

/**
 *
 * @author Giuseppe Della Penna
 */
//essendo solo una sotto-risorsa, non ha un'annotazione @Path e non
//viene registrata nella RESTApp. I path da cui potrà essere attivata
//dipendono quindi solo dalle risorse che la restituiranno 
//(in questo caso solo EventRespource)
public class ParticipantsResource {

    private final EventsService business;
    private final Event event;

    public ParticipantsResource(Event e) {
        this.business = EventsServiceFactory.getEventsService();
        this.event = e;
    }

    @GET
    @Produces({"application/json"})
    public Response listParticipants() {
        return Response.ok(event.getParticipants()).build();
    }

    @Logged
    @POST
    @Consumes({"application/json"})
    @Produces({"text/plain"})
    public Response addParticipant(Participant participant, @Context SecurityContext securityContext, @Context UriInfo uriinfo) {
        try {
            String partid = business.addParticipant(event.getUid(), participant);
            URI uri = uriinfo.getBaseUriBuilder()
                    .path(EventsResource.class)
                    .path(EventsResource.class, "getEvent")
                    .path(EventResource.class, "getParticipants")
                    .build(event.getUid());
            return Response.created(uri).entity(uri.toString()).build();
        } catch (NotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("Event not found").build();
        }
    }

    @DELETE
    @Path("{partid}")
    @Logged
    public Response deleteParticipant(@PathParam("partid") String partid, @Context SecurityContext securityContext) {
        try {
            business.deleteParticipant(event.getUid(), partid);
            return Response.noContent().build();
        } catch (NotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("Event not found").build();
        } catch (DatabaseException ex) {
            return Response.serverError()
                    .entity(ex.getMessage()) //NEVER IN PRODUCTION!
                    .build();
        }
    }
}
