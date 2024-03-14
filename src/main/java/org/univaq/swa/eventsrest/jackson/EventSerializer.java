package org.univaq.swa.eventsrest.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.time.temporal.ChronoUnit;
import org.univaq.swa.eventsrest.model.Event;

public class EventSerializer extends JsonSerializer<Event> {

    @Override
    public void serialize(Event item, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeStringField("uid", item.getUid());
        jgen.writeObjectField("start", item.getStart());
        jgen.writeObjectField("end", item.getEnd());
        jgen.writeObjectField("summary", item.getSummary());
        jgen.writeObjectField("categories", item.getCategories());
        jgen.writeObjectFieldStart("details");
        jgen.writeNumberField("duration", ChronoUnit.MINUTES.between(item.getStart(), item.getEnd()));
        jgen.writeEndObject();
        jgen.writeEndObject();
    }
}
