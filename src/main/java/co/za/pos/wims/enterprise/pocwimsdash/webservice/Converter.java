package co.za.pos.wims.enterprise.pocwimsdash.webservice;


import co.za.pos.wims.enterprise.pocwimsdash.webservice.error.ConversionException;

public interface Converter {
    <T> T deserialize(String payload, Class<T> targetType) throws ConversionException;
    String serialize(Object payload) throws ConversionException;
}

