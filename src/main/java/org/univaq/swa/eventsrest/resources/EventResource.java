package org.univaq.swa.eventsrest.resources;

import java.io.OutputStream;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.StreamingOutput;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import org.univaq.swa.eventsrest.DatabaseException;
import org.univaq.swa.eventsrest.NotFoundException;
import org.univaq.swa.eventsrest.business.EventsService;
import org.univaq.swa.eventsrest.business.EventsServiceFactory;
import org.univaq.swa.eventsrest.model.Event;
import org.univaq.swa.eventsrest.model.Participant;
import org.univaq.swa.eventsrest.model.Recurrence;
import org.univaq.swa.eventsrest.security.Logged;

/**
 *
 * @author Giuseppe Della Penna
 */
//essendo solo una sotto-risorsa, non ha un'annotazione @Path e non
//viene registrata nella RESTApp. I path da cui potrà essere attivata
//dipendono quindi solo dalle risorse che la restituiranno 
//(in questo caso solo EventsRespource)
public class EventResource {

    private final EventsService delegate;
    private final Event event;

    public EventResource(Event e) {
        this.delegate = EventsServiceFactory.getEventsService();
        this.event = e;
    }

    @GET
    @Produces({"application/json"})
    public Response getEvent() {
        return Response.ok(event).build();
    }

    //@PATCH
    @PUT
    @Consumes({"application/json"})
    @Logged
    public Response updateEvent(Event body, @Context SecurityContext securityContext) {
        try {
            delegate.updateEvent(event.getUid(), body);
            return Response.noContent().build();
        } catch (NotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("Event not found").build();
        } catch (DatabaseException ex) {
            return Response.serverError()
                    .entity(ex.getMessage()) //NEVER IN PRODUCTION!
                    .build();
        }
    }

    @DELETE
    @Logged
    public Response deleteEvent(@Context SecurityContext securityContext) {
        try {
            delegate.deleteEvent(event.getUid());
            return Response.noContent().build();
        } catch (NotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("Event not found").build();
        } catch (DatabaseException ex) {
            return Response.serverError()
                    .entity(ex.getMessage()) //NEVER IN PRODUCTION!
                    .build();
        }
    }

    /////
    @GET
    @Path("/attachment")
    @Produces({"application/octet-stream"})
    public Response getAttachment() {
        final byte[] attachment = event.getAttachment();
        StreamingOutput out = (OutputStream output) -> {
            output.write(attachment); //esempio banale
            //ad esempio, potrei copiare su output un altro file
        };
        return Response
                .ok(out)
                .header("content-disposition", "attachment; filename=attachment.bin")
                .build();
    }

    @POST
    @Path("/participants")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @Logged
    public Response addParticipant(@PathParam("uid") String uid, Participant participant, @Context SecurityContext securityContext, @Context UriInfo uriinfo) {
        try {
            String partid = delegate.addParticipant(event.getUid(), participant);
            URI uri = uriinfo.getBaseUriBuilder()
                    .path(getClass())
                    .path(getClass(), "getEvent")
                    .build(partid); //DA RIVEDERE!!!!!!
            return Response.created(uri).entity(uri).build();
        } catch (NotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("Event not found").build();
        }
    }

    @DELETE
    @Path("/participants/{partid}")
    @Logged
    public Response deleteParticipant(@PathParam("partid") String partid, @Context SecurityContext securityContext) {
        try {
            delegate.deleteParticipant(event.getUid(), partid);
            return Response.noContent().build();
        } catch (NotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("Event not found").build();
        } catch (DatabaseException ex) {
            return Response.serverError()
                    .entity(ex.getMessage()) //NEVER IN PRODUCTION!
                    .build();
        }
    }

    @GET
    @Path("/participants")
    @Produces({"application/json"})
    public Response listParticipants() {
        return Response.ok(event.getParticipants()).build();
    }

    @PUT
    @Path("/recurrence")
    @Consumes({"application/json"})
    @Logged
    public Response updateRecurrence(Recurrence body, @Context SecurityContext securityContext) {
        try {
            delegate.updateRecurrence(event.getUid(), body);
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
