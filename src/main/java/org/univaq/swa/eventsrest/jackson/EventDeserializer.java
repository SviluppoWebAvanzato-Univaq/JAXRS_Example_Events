package org.univaq.swa.eventsrest.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.univaq.swa.eventsrest.model.Event;

public class EventDeserializer extends JsonDeserializer<Event> {

    @Override
    public Event deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        Event f = new Event();

        JsonNode node = jp.getCodec().readTree(jp);

        if (node.has("uid")) {
            f.setUid(node.get("uid").asText());
        }

        if (node.has("start")) {
            f.setStart(jp.getCodec().treeToValue(node.get("start"), ZonedDateTime.class));
        }

        if (node.has("end")) {
            f.setStart(jp.getCodec().treeToValue(node.get("end"), ZonedDateTime.class));
        }

        //omettiamo volontariamente dalla deserializzazione moltissimi dettagli
        
        return f;
    }
}
