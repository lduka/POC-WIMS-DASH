package co.za.pos.wims.enterprise.pocwimsdash.webservice.convert;

import co.za.pos.wims.enterprise.pocwimsdash.webservice.Converter;
import co.za.pos.wims.enterprise.pocwimsdash.webservice.error.ConversionException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonConverter implements Converter {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T deserialize(String payload, Class<T> targetType)
            throws ConversionException {
        try {
            return mapper.readValue(payload, targetType);
        } catch (Exception e) {
            throw new ConversionException("Deserialization failed", e);
        }
    }

    @Override
    public String serialize(Object payload) throws ConversionException {
        try {
            return mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new ConversionException("Serialization failed", e);
        }
    }
}
