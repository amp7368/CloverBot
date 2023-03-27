package apple.discord.clover.api.base.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.Instant;

public class InstantJacksonSerializer extends JsonSerializer<Instant> {

    @Override
    public void serialize(Instant src, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(src.toString());
    }
}
