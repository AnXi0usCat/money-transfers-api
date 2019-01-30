package com.mishas.stuff.mta.persistence.dao;

import com.mishas.stuff.common.utils.exceptions.MyFundsTransferException;
import com.mishas.stuff.common.utils.exceptions.MyPersistenceException;
import com.mishas.stuff.mta.persistence.HibernateUtilities;
import com.mishas.stuff.mta.persistence.model.Account;
import com.mishas.stuff.mta.persistence.model.Transfer;
import com.mishas.stuff.mta.web.dto.TransferResultDto;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AccountRepository extends GenericCrudRepository<Account> {

        public AccountRepository() {
            super(Account.class);
        }

        public boolean accountExists(long id) {
            return super.get(id) != null ? true : false;
        }

        public TransferResultDto transferFundsBetweenAccounts(Transfer resource) {

            Transaction transaction = null;
            Session session = null;
            Account sourceAccount = null;
            Account destinationAccount = null;
            TransferResultDto transferResultDto;

            try  {
                session = HibernateUtilities.getSessionFactory().openSession();
                // start a transaction
                transaction = session.beginTransaction();

                // get both accounts and acquire a pessimistic write lock on both od thm
                sourceAccount = session.load(Account.class, resource.getSourceAccount(), LockMode.PESSIMISTIC_WRITE);
                destinationAccount = session.load(Account.class, resource.getDestinationAccount(), LockMode.PESSIMISTIC_WRITE);

                // check that both accounts have the same ccy and that source account ccy is the same as transfer ccy
                if ( !(sourceAccount.getCurrency().equals(destinationAccount.getCurrency())) || !(sourceAccount.getCurrency().equals(resource.getCurrency())) ){
                    LOGGER.error("Currency mismatch. Source: "
                            + sourceAccount.getCurrency() + " Destination: "
                            + destinationAccount.getCurrency() + " Transfer: " + resource.getCurrency());
                    throw new MyFundsTransferException("Mismatch in currencies between Accounts or the transferred amount, " +
                            "cannot transfer funds. Check logs for details");

                }

                BigDecimal sourceBalanceAfter = sourceAccount.getBalance().subtract(resource.getBalance());
                BigDecimal destinationBalanceAfter = destinationAccount.getBalance().add(resource.getBalance());

                // check that source account has sufficient balance to complete this transfer
                if (sourceBalanceAfter.compareTo(new BigDecimal(0).setScale(4, RoundingMode.HALF_EVEN)) < 0) {
                    LOGGER.error("Insufficient Balance on the Source Account, cannot proceed with the transfer");
                    throw new MyFundsTransferException("Insufficient Balance on the Source Account, cannot proceed with the transfer");
                }

                // update the balances of account objects
                sourceAccount.setBalance(sourceBalanceAfter);
                destinationAccount.setBalance(destinationBalanceAfter);

                // update the records in the database
                session.update(sourceAccount);
                session.update(destinationAccount);

                // commit and done =)
                transaction.commit();

                // create out result object
                transferResultDto = new TransferResultDto(
                        sourceAccount.getId(),
                        sourceBalanceAfter,
                        sourceBalanceAfter.add(resource.getBalance()),
                        resource.getBalance(),
                        sourceAccount.getCurrency()
                );
            } catch (HibernateException e) {
                super.LOGGER.error("Error occurred in the update method, rolling back the transaction: " + e.getLocalizedMessage());
                // roll back the transaction
                if (transaction != null) {
                    transaction.rollback();
                }
                // re throw custom exception here
                throw new MyPersistenceException(e);
            } finally {
                session.close();
            }
            return transferResultDto;
        }
}
