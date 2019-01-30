package com.mishas.stuff.mta.persistence.dao;

import com.mishas.stuff.common.interfaces.IEntity;
import com.mishas.stuff.common.utils.exceptions.MyPersistenceException;
import com.mishas.stuff.mta.persistence.HibernateUtilities;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GenericCrudRepository<T extends IEntity> {


    private Class<T> clazz;
    static final Logger LOGGER = LoggerFactory.getLogger("CrudRepository");

    public GenericCrudRepository (Class<T> clazz) {
        this.clazz = clazz;
    }

    public void create(T resource) {

        Transaction transaction = null;
        Session session = null;
        try  {
            session = HibernateUtilities.getSessionFactory().openSession();
            // start a transaction
            transaction = session.beginTransaction();
            session.save(resource);
            transaction.commit();
        } catch (Exception e) {
            LOGGER.error("Error occurred in the create method, rolling back the transaction: " + e.getLocalizedMessage());
            // roll back the transaction
            if (transaction != null) {
                transaction.rollback();
            }
            // re throw custom exception here
            throw new MyPersistenceException(e);
        } finally {
            session.close();
        }

    }

    // could return null if account doesnt exist
    public T get(long id) {

        T resource = null;
        Transaction transaction = null;
        Session session = null;
        try  {
            session = HibernateUtilities.getSessionFactory().openSession();
            // start a transaction
            transaction = session.beginTransaction();
            resource = session.get(clazz, id);
            transaction.commit();
        } catch (Exception e) {
            LOGGER.error("Error occurred in the get method: " + e.getLocalizedMessage());
            if (transaction != null) {
                transaction.rollback();
            }
            // re throw custom exception here
            throw new MyPersistenceException(e);
        } finally {
            session.close();
        }
        return resource;
    }

    public void delete(long id) {

        Transaction transaction = null;
        Session session = null;
        try  {
            session = HibernateUtilities.getSessionFactory().openSession();
            // start a transaction
            transaction = session.beginTransaction();
            T resource = session.load(clazz, id);
            session.delete(resource);
            transaction.commit();
        } catch (Exception e) {
            LOGGER.error("Error occurred in the delete method, rolling back the transaction" + e.getLocalizedMessage());
            // roll back the transaction
            if (transaction != null) {
                transaction.rollback();
            }
            // re throw custom exception here
            throw new MyPersistenceException(e);
        } finally {
            session.close();
        }
    }

    public T update(long id, T update) {

        Transaction transaction = null;
        Session session = null;
        T toBeUpdated = null;
        try  {
            session = HibernateUtilities.getSessionFactory().openSession();
            // start a transaction
            transaction = session.beginTransaction();
            // use pessimistic lock to avoid missing update
            toBeUpdated = session.load(clazz, id, LockMode.PESSIMISTIC_WRITE);
            // update accounts balance
            toBeUpdated.setBalance(update.getBalance());
            // update the database record
            session.update(toBeUpdated);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            LOGGER.error("Error occurred in the update method, rolling back the transaction: " + e.getLocalizedMessage());
            // roll back the transaction
            if (transaction != null) {
                transaction.rollback();
            }
            // re throw custom exception here
            throw new MyPersistenceException(e);
        } finally {
            session.close();
        }
        return toBeUpdated;
    }
}
