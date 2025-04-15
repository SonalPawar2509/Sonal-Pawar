package com.petstore.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApiConfig {
    private static final String CONFIG_FILE = "config/api-config.properties";
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ApiConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + CONFIG_FILE);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading configuration file", e);
        }
    }

    public static String getBaseUri() {
        return properties.getProperty("api.base.uri");
    }

    public static int getDefaultTimeout() {
        return Integer.parseInt(properties.getProperty("api.timeout", "5000"));
    }

    public static String getApiVersion() {
        return properties.getProperty("api.version", "v2");
    }
} 