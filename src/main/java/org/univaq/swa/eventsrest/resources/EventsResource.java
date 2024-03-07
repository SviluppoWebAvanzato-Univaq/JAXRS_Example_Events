package org.univaq.swa.eventsrest.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.univaq.swa.eventsrest.NotFoundException;
import org.univaq.swa.eventsrest.business.EventsService;
import org.univaq.swa.eventsrest.business.EventsServiceFactory;
import org.univaq.swa.eventsrest.model.Event;
import org.univaq.swa.eventsrest.security.Logged;

/**
 *
 * @author Didattica
 */
//risponde ai path .../rest/fatture
@Path("/events")
public class EventsResource {

    private final EventsService delegate;
    private final DateTimeFormatter temporalformat= DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public EventsResource() {
        this.delegate = EventsServiceFactory.getEventsService();
        
    }

    private List mapEvents(List<Event> events) {
        return events.stream()
                .map(e -> Map.of("start", e.getStart(), "end", e.getEnd(), "summary", e.getSummary(), "uid", e.getUid()))
                .toList();
    }

    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @Logged
    public Response addEvent(Event event, @Context SecurityContext securityContext, @Context UriInfo uriinfo) {
        String uid = delegate.addEvent(event);
        URI uri = uriinfo.getBaseUriBuilder()
                .path(getClass())
                .path(getClass(), "getEvent")
                .build(uid);
        return Response.created(uri).entity(uri).build();

    }

    @GET
    @Path("/current")
    @Produces({"application/json"})
    public Response getCurrentEvents(@QueryParam("cat") List<String> cat) {
        return Response.ok(mapEvents(delegate.getCurrentEvents(cat))).build();
    }

    @GET
    @Produces({"application/json"})
    public Response getEvents(@QueryParam("from") String from, @QueryParam("to") String to, @QueryParam("cat") List<String> cat, @Context SecurityContext securityContext) {
        if (from != null && !from.isBlank() && to != null && !to.isBlank()) {
            ZonedDateTime dfrom =null;// ZonedDateTime.parse(from,temporalformat);
            ZonedDateTime dto =null;// ZonedDateTime.parse(to,temporalformat);
            return Response.ok(mapEvents(delegate.getEvents(dfrom, dto, cat))).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity("Both from and to query parameters must be specified").build();
        }
    }

    @GET
    @Path("/count")
    @Produces({"text/plain"})
    public Response getNumberOfEvents(@QueryParam("from") String from, @QueryParam("to") String to, @QueryParam("cat") List<String> cat, @Context SecurityContext securityContext) {
        if (from != null && !from.isBlank() && to != null && !to.isBlank()) {
            ZonedDateTime dfrom =null;// ZonedDateTime.parse(from,temporalformat);
            ZonedDateTime dto =null;// ZonedDateTime.parse(to,temporalformat);
            return Response.ok(delegate.getNumberOfEvents(dfrom, dto, cat)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity("Both from and to query parameters must be specified").build();
        }
    }

    @Path("/{uid}")
    public EventResource getEvent(@PathParam("uid") String uid) {
        try {
            return new EventResource(delegate.getEvent(uid));
        } catch (NotFoundException ex) {
            return null; //ritornare null da un metodo che restituisce una sotto-risorsa equivale a un 404
        }
    }
}
