package com.mishas.stuff.mta.persistence.dao;

import com.mishas.stuff.common.persistence.ICrudRepository;
import com.mishas.stuff.common.service.ICrudService;
import com.mishas.stuff.common.utils.exception.MyPersistenceException;
import com.mishas.stuff.mta.persistence.HibernateUtilities;
import com.mishas.stuff.mta.persistence.model.Account;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountRepository implements ICrudRepository{

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountRepository.class);

    public void create(Account resource) {

        Transaction transaction = null;
        Session session = null;
        try  {
            session = HibernateUtilities.getSessionFactory().openSession();
            // start a transaction
            transaction = session.beginTransaction();
            session.save(resource);
            transaction.commit();
        } catch (Exception e) {
            LOGGER.error("Error occurred in the createAccount method, rolling back the transaction: " + e.getLocalizedMessage());
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
    public Account get(long id) {

        Account resource = null;
        try (Session session = HibernateUtilities.getSessionFactory().openSession()) {
            resource = session.get(Account.class, id);
        } catch (Exception e) {
            LOGGER.error("Error occurred in the getAccount method: " + e.getLocalizedMessage());
            // re throw custom exception here
            throw new MyPersistenceException(e);
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
            Account resource = session.load(Account.class, id);
            session.delete(resource);
            transaction.commit();

        } catch (Exception e) {
            LOGGER.error("Error occurred in the deleteAccount method, rolling back the transaction" + e.getLocalizedMessage());
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

    public Account update(long id, Account update) {

        Transaction transaction = null;
        Session session = null;
        Account updated = null;
        try  {
            session = HibernateUtilities.getSessionFactory().openSession();
            // start a transaction
            transaction = session.beginTransaction();
            session.update(update);
            updated = session.load(Account.class, id);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            LOGGER.error("Error occurred in the updateAccount method, rolling back the transaction: " + e.getLocalizedMessage());
            // roll back the transaction
            if (transaction != null) {
                transaction.rollback();
            }
            // re throw custom exception here
            throw new MyPersistenceException(e);
        } finally {
            session.close();
        }
        return updated;
    }
}
