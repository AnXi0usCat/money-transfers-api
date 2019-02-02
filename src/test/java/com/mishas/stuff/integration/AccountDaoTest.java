package com.mishas.stuff.integration;

import com.mishas.stuff.common.utils.exceptions.MyPersistenceException;
import com.mishas.stuff.mta.persistence.dao.AccountRepository;
import com.mishas.stuff.mta.persistence.model.Account;

import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountDaoTest {

    private AccountRepository accountRepository;
    private final static int THREAD_COUNT = 2;

    @Before
    public void setup() {
        accountRepository = new AccountRepository();
    }

    /*
        retrieve saved account are the same
     */
    @Test
    public void testCreateNewAccount() {
        Account accountCreate = new Account("EUR", new BigDecimal(100.00));
        Serializable key = accountRepository.create(accountCreate);
        Account accountGet = accountRepository.get(Long.parseLong(key.toString()));
        assertEquals(accountGet.getBalance().compareTo(accountCreate.getBalance().setScale(4, RoundingMode.HALF_EVEN)), 0);
        assertEquals(accountGet.getCurrency(), accountCreate.getCurrency());
    }

    /*
        currency cant be null
     */
    @Test(expected = MyPersistenceException.class)
    public void testCreateNewAccount_currencyNotNull() {
        Account accountCreate = new Account(null, new BigDecimal(100.00));
        Serializable key = accountRepository.create(accountCreate);
        Account accountGet = accountRepository.get(Long.parseLong(key.toString()));
    }

    /*
        balance cant be null
     */
    @Test(expected = MyPersistenceException.class)
    public void testCreateNewAccount_balanceNotNull() {
        Account accountCreate = new Account("EUR", null);
        Serializable key = accountRepository.create(accountCreate);
        Account accountGet = accountRepository.get(Long.parseLong(key.toString()));
    }

    /*
        after updating the account only balance changes
     */
    @Test
    public void testCreateAccountThenUpdate_changesOnlyBalance() {
        // given
        Account accountCreate = new Account("EUR", new BigDecimal(100.00));
        Account accountUpdate = new Account("GBP", new BigDecimal(120.00));
        // when
        Serializable key = accountRepository.create(accountCreate);
        accountRepository.update(Long.parseLong(key.toString()),accountUpdate);
        Account accountGet = accountRepository.get(Long.parseLong(key.toString()));
        // then
        assertEquals(accountGet.getBalance().compareTo(accountUpdate.getBalance().setScale(4, RoundingMode.HALF_EVEN)), 0);
        assertTrue(!accountGet.getCurrency().equals(accountUpdate.getCurrency()));
        assertEquals("currency of the initial account and account after update are not the same",
                accountGet.getCurrency(), accountCreate.getCurrency());
    }

    /*
        deleted account disappears from the database
     */
    @Test
    public void testDeletingAccount_removesItFromDatabase() {
        // given
        Account accountCreate = new Account("EUR", new BigDecimal(100.00));
        // when
        Serializable key = accountRepository.create(accountCreate);
        accountRepository.delete(Long.parseLong(key.toString()));
        Account accountGet = accountRepository.get(Long.parseLong(key.toString()));
        assertEquals(accountGet, null);
    }
}
