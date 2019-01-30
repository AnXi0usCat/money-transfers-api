package com.mishas.stuff.mta.persistence;


import com.mishas.stuff.mta.persistence.model.Account;
import com.mishas.stuff.mta.persistence.model.Transfer;
import com.mishas.stuff.mta.persistence.model.TransferResult;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;


public class HibernateUtilities {

    private static SessionFactory sessionFactory;
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                // Hibernate settings equivalent to hibernate.cfg.xml's properties
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "org.h2.Driver");
                settings.put(Environment.URL, "jdbc:h2:mem:test?useSSL=false");
                settings.put(Environment.USER, "sa");
                settings.put(Environment.PASS, "");
                settings.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                settings.put(Environment.HBM2DDL_AUTO, "create-drop");
                settings.put(Environment.AUTOCOMMIT, false);
                // pool size
                settings.put(Environment.C3P0_MIN_SIZE, 10);
                settings.put(Environment.C3P0_MAX_SIZE, 40);
                settings.put(Environment.C3P0_TIMEOUT, 1800);
                settings.put(Environment.C3P0_MAX_STATEMENTS, 80);

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
