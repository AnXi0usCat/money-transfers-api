package com.mishas.stuff.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigPropertiesLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigPropertiesLoader.class);
    private static Properties prop = new Properties();

    static {
        run();
    }

    /**
     *  Get the configuration properties from the application.properties file
     */
    private static void run() {
        InputStream input = null;

        try {
            input = new FileInputStream("src/main/resources/application.properties");
            prop.load(input);


        } catch (IOException ex) {
            LOGGER.error("Cannot load application.properties file: " + ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOGGER.error("Cannot close the file input stream: " + e);
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getProperty(String property, String defaultProperty) {
            return prop.getProperty(property, defaultProperty);
    }
}
