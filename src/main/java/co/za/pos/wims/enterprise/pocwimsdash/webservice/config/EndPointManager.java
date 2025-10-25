package co.za.pos.wims.enterprise.pocwimsdash.webservice.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EndPointManager {
    private static final String ENV_PROPERTY = "app.env";
    private static final String DEFAULT_ENV  = "dev";
    private static final Properties PROPS     = new Properties();

    static {
        String env = System.getProperty(ENV_PROPERTY, DEFAULT_ENV).toLowerCase();
        String file = String.format("endpoints-%s.properties", env);
        try (InputStream in = EndPointManager.class
                .getClassLoader()
                .getResourceAsStream(file)) {
            if (in == null) {
                throw new IllegalStateException("Missing " + file);
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + file, e);
        }
    }

    public static String getEndpoint(String key) {
        String url = PROPS.getProperty(key);
        if (url == null) {
            throw new IllegalArgumentException("No endpoint for key: " + key);
        }
        return url;
    }
}
