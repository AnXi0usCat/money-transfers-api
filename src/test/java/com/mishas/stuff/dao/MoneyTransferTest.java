package com.mishas.stuff.dao;

import com.mishas.stuff.common.utils.exceptions.MyFundsTransferException;
import com.mishas.stuff.mta.persistence.dao.AccountRepository;
import com.mishas.stuff.mta.persistence.model.Account;
import com.mishas.stuff.mta.persistence.model.Transfer;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;


public class MoneyTransferTest {

    private AccountRepository accountRepository;
    private final static int THREAD_COUNT = 2;


    private long serilizableToLong(Serializable input) {
        return Long.parseLong(input.toString());
    }

    @Before
    public void setup() {
        accountRepository = new AccountRepository();
    }

    /*
      source currency must be the same as destination currency
    */
    @Test(expected = MyFundsTransferException.class)
    public void testTransferMoneyWhenSourceCurrencyAndDestinationCurrencyAreDifferent_shoudFail() {
        // given

        Account accountCreateOne = new Account("EUR", new BigDecimal(100.00));
        Account accountCreateTwo = new Account("GBP", new BigDecimal(120.00));
        // when
        Serializable keySource = accountRepository.create(accountCreateOne);
        Serializable keyDest = accountRepository.create(accountCreateTwo);
        Transfer transferCreate = new Transfer(serilizableToLong(keySource),serilizableToLong(keyDest),"EUR", new BigDecimal(10));
        accountRepository.transferFundsBetweenAccounts(transferCreate); // throws MyFundsTransferException
    }

    /*
       transfer currency must be the same as source and dest currency
     */
    @Test(expected = MyFundsTransferException.class)
    public void testTransferMoneyWhenSourceCurrencyAnTransferCurrencyAreDifferent_shoudFail() {
        // given
        Account accountCreateOne = new Account("GBP", new BigDecimal(100.00));
        Account accountCreateTwo = new Account("GBP", new BigDecimal(120.00));
        // when
        Serializable keySource = accountRepository.create(accountCreateOne);
        Serializable keyDest = accountRepository.create(accountCreateTwo);
        Transfer transferCreate = new Transfer(serilizableToLong(keySource), serilizableToLong(keyDest),"EUR", new BigDecimal(10));
        accountRepository.transferFundsBetweenAccounts(transferCreate); // throws MyFundsTransferException
    }

    /*
        One account doesnt exist in the Database - should fail
     */
    @Test(expected = Exception.class)
    public void testOneAccountDoesntExist_ShouldFail() {
        // given
        Transfer transferCreate = new Transfer(1L,3000L,"GBP", new BigDecimal(10));
        Account accountCreateOne = new Account("GBP", new BigDecimal(100.00));
        Account accountCreateTwo = new Account("GBP", new BigDecimal(120.00));
        // when
        accountRepository.create(accountCreateOne);
        accountRepository.create(accountCreateTwo);
        accountRepository.transferFundsBetweenAccounts(transferCreate); // throws Excpetion
    }

    /*
        transfer money between accounts successfully
     */
    @Test
    public void testTransferMoneyBetweenAccounts() {
        // given
        Account accountCreateOne = new Account("GBP", new BigDecimal(100));
        Account accountCreateTwo = new Account("GBP", new BigDecimal(0));
        Long keySource = Long.parseLong(accountRepository.create(accountCreateOne).toString());
        Long keyDest = Long.parseLong(accountRepository.create(accountCreateTwo).toString());

        Transfer transferCreate = new Transfer(keySource, keyDest,"GBP", new BigDecimal(100));
        accountRepository.transferFundsBetweenAccounts(transferCreate);
        // then money was successfully transferred between accounts
        assertEquals(accountRepository.get(keySource).getBalance().compareTo(new BigDecimal(0).setScale(4, RoundingMode.HALF_EVEN)) ,0);
        assertEquals(accountRepository.get(keyDest).getBalance().compareTo(new BigDecimal(100).setScale(4, RoundingMode.HALF_EVEN)),0);
    }

    /*
        transfer more than balance
    */
    @Test
    public void testTransferMoreThanInBalanceShouldFail() {
        // given
        Account accountCreateOne = new Account("GBP", new BigDecimal(100));
        Account accountCreateTwo = new Account("GBP", new BigDecimal(0));
        // when
        Long keySource = Long.parseLong(accountRepository.create(accountCreateOne).toString());
        Long keyDest = Long.parseLong(accountRepository.create(accountCreateTwo).toString());

        Transfer transferCreate = new Transfer(keySource,keyDest,"GBP", new BigDecimal(110));
        try {
            accountRepository.transferFundsBetweenAccounts(transferCreate);
        } catch(MyFundsTransferException mf) {

        }
        // get the balance from both of these accounts
        Account accountSource = accountRepository.get(keySource);
        Account AccountDest = accountRepository.get(keyDest);

        // confirm that balance is the same
        assertEquals(accountSource.getBalance().compareTo(new BigDecimal(100.00)), 0);
        assertEquals(AccountDest.getBalance().compareTo(new BigDecimal(0).setScale(4, RoundingMode.HALF_EVEN)), 0);
    }

    /*
        Two threads trying to make a transfer at the same time, one transaction should fail, one should succeed
     */
    @Test
    public void testOneTransferShouldFailOnMultiThreadTransfer() throws InterruptedException {

        Account accountCreateOne = new Account("GBP", new BigDecimal(100));
        Account accountCreateTwo = new Account("GBP", new BigDecimal(0));

        Long keySource = Long.parseLong(accountRepository.create(accountCreateOne).toString());
        Long keyDest = Long.parseLong(accountRepository.create(accountCreateTwo).toString());

        Transfer transferCreate = new Transfer(keySource, keyDest,"GBP", new BigDecimal(100));

        // transfer a total of 200USD from 100USD balance in multi-threaded
        // mode, expect half of the transaction fail
        final CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(() -> {
                try {
                    accountRepository.transferFundsBetweenAccounts(transferCreate);
                } catch(MyFundsTransferException mf) {
                    // handle error
                } finally {
                    latch.countDown();
                }
            }).start();
        }
        // release the hounds =)
        latch.await();

        // get the balance from both of these accounts
        Account accountSource = accountRepository.get(keySource);
        Account AccountDest = accountRepository.get(keyDest);

        // destination account should only have 100 GBP on it, NOT more
        assertEquals(accountSource.getBalance().compareTo(new BigDecimal(0.00)), 0);
        assertEquals(AccountDest.getBalance().compareTo(new BigDecimal(100.00)), 0);
    }
}

