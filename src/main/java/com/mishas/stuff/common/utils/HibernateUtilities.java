package com.mishas.stuff.common.utils;


import com.mishas.stuff.common.utils.ConfigPropertiesLoader;
import com.mishas.stuff.mta.persistence.model.Account;
import com.mishas.stuff.mta.persistence.model.Transfer;
import com.mishas.stuff.mta.persistence.model.TransferResult;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;


/**
 * Hibenrate Configuration class, returns SessionFactory to create sessions
 */
public class HibernateUtilities {

    private static SessionFactory sessionFactory;
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                // Hibernate settings equivalent to hibernate.cfg.xml's properties
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, ConfigPropertiesLoader.getProperty("h2_driver"));
                settings.put(Environment.URL, ConfigPropertiesLoader.getProperty("h2_connection_url"));
                settings.put(Environment.USER, ConfigPropertiesLoader.getProperty("h2_user"));
                settings.put(Environment.PASS, ConfigPropertiesLoader.getProperty("h2_password"));
                settings.put(Environment.DIALECT, ConfigPropertiesLoader.getProperty("hibernate_dialect"));
                settings.put(Environment.SHOW_SQL, ConfigPropertiesLoader.getProperty("hibernate_show_sql"));
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, ConfigPropertiesLoader.getProperty("hibernate_current_session_context_class"));
                settings.put(Environment.HBM2DDL_AUTO, ConfigPropertiesLoader.getProperty("hibernate_hbm2ddl_auto"));
                settings.put(Environment.AUTOCOMMIT, ConfigPropertiesLoader.getProperty("hibernate_auto_commit"));
//                // pool size
//                settings.put(Environment.C3P0_MIN_SIZE, ConfigPropertiesLoader.getProperty("c3p0_min_size"));
//                settings.put(Environment.C3P0_MAX_SIZE, ConfigPropertiesLoader.getProperty("c3p0_max_size"));
//                settings.put(Environment.C3P0_TIMEOUT, ConfigPropertiesLoader.getProperty("c3p0_timeout"));
//                settings.put(Environment.C3P0_MAX_STATEMENTS, ConfigPropertiesLoader.getProperty("c3p0_max_statements"));

                configuration.setProperties(settings);
                // entities
                configuration.addAnnotatedClass(Account.class);
                configuration.addAnnotatedClass(Transfer.class);
                configuration.addAnnotatedClass(TransferResult.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
