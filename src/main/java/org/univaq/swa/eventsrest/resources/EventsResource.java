package org.univaq.swa.eventsrest.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
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
//risponde ai path .../rest/events
@Path("/events")
public class EventsResource {

    private final EventsService business;
    private final DateTimeFormatter INPUT_DATE_PARSING_FORMAT = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
            .parseDefaulting(ChronoField.OFFSET_SECONDS, 0)
            .toFormatter();

    public EventsResource() {
        this.business = EventsServiceFactory.getEventsService();

    }

    //crea "al volo" una sorta di DTO (qui realizzato semplicemente con una mappa) per gli eventi
    //che permette di esportarne tramite la API "lista" solo i dettagli che vogliamo mostrare all'esterno
    private List mapSimpleEvents(List<Event> events, UriInfo uriinfo) {
        return events.stream()
                .map(e -> {
                    URI uri = uriinfo.getBaseUriBuilder()
                            .path(getClass())
                            .path(getClass(), "getEvent")
                            .build(e.getUid());
                    return Map.of("url", uri, "start", e.getStart(), "end", e.getEnd(), "summary", e.getSummary(), "uid", e.getUid());
                })
                .toList();
    }

    @POST
    @Consumes({"application/json"})
    @Produces({"text/plain"})
    @Logged
    public Response addEvent(Event event, @Context SecurityContext securityContext, @Context ContainerRequestContext requestcontext, @Context UriInfo uriinfo) {

        //essendo Logged, possiamo estrarre le informazioni
        //impostate dal LoggedFilter iniettando le opportune
        //classi tra i parametri del metodo:     
        //solo per debug...
        System.out.println(securityContext.getUserPrincipal().getName());
        System.out.println(requestcontext.getProperty("token"));

        String uid = business.addEvent(event);
        URI uri = uriinfo.getBaseUriBuilder()
                .path(getClass())
                .path(getClass(), "getEvent")
                .build(uid);
        return Response.created(uri).entity(uri.toString()).build();

    }

    @GET
    @Path("/current")
    @Produces({"application/json"})
    public Response getCurrentEvents(@QueryParam("cat") List<String> cat, @Context UriInfo uriinfo) {
        //cat sarà nulla se il parametro di query non è presente
        return Response.ok(mapSimpleEvents(business.getCurrentEvents(cat), uriinfo)).build();
    }

    @GET
    @Produces({"application/json"})
    public Response getEvents(@QueryParam("from") String from, @QueryParam("to") String to, @QueryParam("cat") List<String> cat, @Context UriInfo uriinfo) {
        if (from != null && !from.isBlank() && to != null && !to.isBlank()) {
            try {
                ZonedDateTime dfrom = ZonedDateTime.parse(from, INPUT_DATE_PARSING_FORMAT);
                ZonedDateTime dto = ZonedDateTime.parse(to, INPUT_DATE_PARSING_FORMAT);
                return Response.ok(mapSimpleEvents(business.getEvents(dfrom, dto, cat), uriinfo)).build();
            } catch (DateTimeParseException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity("Invalid date format").build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity("Both from and to query parameters must be specified").build();
        }
    }

    @GET
    @Path("/count")
    @Produces({"text/plain"})
    public Response getNumberOfEvents(@QueryParam("from") String from, @QueryParam("to") String to, @QueryParam("cat") List<String> cat) {
        if (from != null && !from.isBlank() && to != null && !to.isBlank()) {
            try {
                ZonedDateTime dfrom = ZonedDateTime.parse(from, INPUT_DATE_PARSING_FORMAT);
                ZonedDateTime dto = ZonedDateTime.parse(to, INPUT_DATE_PARSING_FORMAT);
                return Response.ok(business.getNumberOfEvents(dfrom, dto, cat)).build();
            } catch (DateTimeParseException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity("Invalid date format").build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity("Both from and to query parameters must be specified").build();
        }
    }

    //non ci sono metodi specificati, quindi per JAX-RS si tratta
    //della restituzione di una sotto-risorsa
    @Path("/{uid: ID[a-z]+}")
    public EventResource getEvent(@PathParam("uid") String uid) {
        try {
            return new EventResource(business.getEvent(uid));
        } catch (NotFoundException ex) {
            return null; //ritornare null da un metodo che restituisce una sotto-risorsa equivale a un 404
        }
    }
}
