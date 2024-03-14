package org.univaq.swa.eventsrest.resources;

import java.io.OutputStream;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.StreamingOutput;
import jakarta.ws.rs.core.UriInfo;
import java.io.InputStream;
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

    private final EventsService business;
    private final Event event;

    public EventResource(Event e) {
        this.business = EventsServiceFactory.getEventsService();
        this.event = e;
    }

    @GET
    @Produces({"application/json"})
    public Response getEvent() {
        return Response.ok(event).build();
    }

    //@PATCH non funziona :)
    @Logged
    @PUT
    @Consumes({"application/json"})
    public Response updateEvent(Event body, @Context SecurityContext securityContext) {
        try {
            business.updateEvent(event.getUid(), body);
            return Response.noContent().build();
        } catch (NotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("Event not found").build();
        } catch (DatabaseException ex) {
            return Response.serverError()
                    .entity(ex.getMessage()) //NEVER IN PRODUCTION!
                    .build();
        }
    }

    @Logged
    @DELETE
    public Response deleteEvent(@Context SecurityContext securityContext) {
        try {
            business.deleteEvent(event.getUid());
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
    //binario generico, nel caso l'attachment non abbia tipi specifici
    @Produces({"application/octet-stream"})
    public Response getAttachment() {
        final byte[] attachment = event.getAttachment();
        StreamingOutput out = (OutputStream output) -> {
            output.write(attachment); //esempio banale
            //ad esempio, potrei copiare su output un altro file
        };
        return Response
                .ok(out)
                //in output inseriremo il nome effettivo del file restituito
                .header("content-disposition", "attachment; filename=attachment.txt")
                //in oputput, possiamo specificare il tipo dell'attachment effettivo che stiamo restituendo
                //che ovviamente deve essere compatibile con l'@Produces
                .type("text/plain")
                .build();
    }

    @Logged
    @POST
    @Path("/participants")
    @Consumes({"application/json"})
    @Produces({"text/plain"})
    public Response addParticipant(Participant participant, @Context SecurityContext securityContext, @Context UriInfo uriinfo) {
        try {
            String partid = business.addParticipant(event.getUid(), participant);
            URI uri = uriinfo.getBaseUriBuilder()
                    .path(EventsResource.class)
                    .path(EventsResource.class, "getEvent")
                    .path(getClass(), "listParticipants")
                    .build(event.getUid());
            return Response.created(uri).entity(uri.toString()).build();
        } catch (NotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("Event not found").build();
        }
    }

    @DELETE
    @Path("/participants/{partid}")
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

    @GET
    @Path("/participants")
    @Produces({"application/json"})
    public Response listParticipants() {
        return Response.ok(event.getParticipants()).build();
    }

    @Logged
    @PUT
    @Path("/recurrence")
    @Consumes({"application/json"})
    public Response updateRecurrence(Recurrence body, @Context SecurityContext securityContext) {
        try {
            business.updateRecurrence(event.getUid(), body);
            return Response.noContent().build();
        } catch (NotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("Event not found").build();
        } catch (DatabaseException ex) {
            return Response.serverError()
                    .entity(ex.getMessage()) //NEVER IN PRODUCTION!
                    .build();
        }
    }

    @Logged
    @PUT
    @Path("/attachment")
    @Consumes(MediaType.WILDCARD)
    public Response updateAttachment(
            @Context UriInfo c,
            // Possiamo anche (per POST e PUT) dire a JAX-RS
            // di fornirci il payload sotto forma ti uno stream.
            // (utile spprattutto per payload lunghi o binari)
            InputStream data) {

        try {
            business.updateAttachment(event.getUid(), data);
        } catch (NotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("Event not found").build();
        } catch (DatabaseException ex) {
            return Response.serverError()
                    .entity(ex.getMessage()) //NEVER IN PRODUCTION!
                    .build();
        }

        return Response.noContent().build();
    }
}
