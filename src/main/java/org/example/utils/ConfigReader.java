package org.example.utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * Loads configuration values from {@code config.properties} with support for
 * JVM system-property overrides (e.g., {@code -Dbrowser=edge -Dheadless=true}).
 */
public final class ConfigReader {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) {
                throw new IllegalStateException("config.properties not found on classpath");
            }
            PROPS.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private ConfigReader() {
    }

    /**
     * Returns the trimmed value for {@code key}, checking {@link System#getProperty(String)} first.
     */
    public static String get(String key) {
        String sysValue = System.getProperty(key);
        if (sysValue != null && !sysValue.isBlank()) {
            return sysValue.trim();
        }
        String fileValue = PROPS.getProperty(key);
        return fileValue != null ? fileValue.trim() : null;
    }

    /**
     * Returns the trimmed value for {@code key}, or {@code defaultValue} if missing or blank.
     */
    public static String get(String key, String defaultValue) {
        String value = get(key);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    /**
     * Returns an integer configuration value, or {@code defaultValue} if missing.
     */
    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        return (value == null || value.isBlank()) ? defaultValue : Integer.parseInt(value);
    }

    /**
     * Returns a boolean configuration value, or {@code defaultValue} if missing.
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        return (value == null || value.isBlank()) ? defaultValue : Boolean.parseBoolean(value);
    }
}
