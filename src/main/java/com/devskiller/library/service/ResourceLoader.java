package com.devskiller.library.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.Properties;

public class ResourceLoader {

    private static final String FILE_NAME = "/application.properties";

    public static Optional<String> getProperty(String name) {
        Properties properties = new ResourceLoader().loadProperties();
        return Optional.ofNullable(properties.getProperty(name));
    }

    private Properties loadProperties() {
        Properties properties = new Properties();
        try( BufferedReader reader = new BufferedReader(
            new InputStreamReader(this.getClass().getResourceAsStream(FILE_NAME), "UTF-8"));
        ){
            properties.load(reader);
        } catch (IOException ioException) {
            System.out.println("exception occured while reading application.properties file");
        }
        return properties;
    }
}
