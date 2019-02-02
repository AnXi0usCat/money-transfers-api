package com.mishas.stuff.integration;

import com.mishas.stuff.common.utils.exceptions.MyPersistenceException;
import com.mishas.stuff.mta.persistence.dao.TransferRepository;
import com.mishas.stuff.mta.persistence.model.Transfer;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.assertEquals;

public class TransferDaoTest {

    private TransferRepository transferRepository;

    @Before
    public void setup() {
        transferRepository = new TransferRepository();
    }

    @Test
    public void testCreateNewTransfer() {
        // given
        Transfer transferCreate = new Transfer(1L,2L,"EUR", new BigDecimal(10));
        // when
        Serializable key = transferRepository.create(transferCreate);
        transferCreate.setId(Long.parseLong(key.toString()));
        Transfer transferGet = transferRepository.get(Long.parseLong(key.toString()));
        // then
        assertEquals(transferGet.getBalance().compareTo(transferCreate.getBalance().setScale(4, RoundingMode.HALF_EVEN)), 0);
        assertEquals(transferGet.getCurrency(),  transferCreate.getCurrency());
        assertEquals(transferGet.getSourceAccount(),  transferCreate.getSourceAccount());
        assertEquals(transferGet.getDestinationAccount(),  transferCreate.getDestinationAccount());
    }
    /*
        source account cant be null
     */
    @Test(expected = MyPersistenceException.class)
    public void testSourceAccountCantBeNull() {
        // given
        Transfer transferCreate = new Transfer(null,2L,"EUR", new BigDecimal(10));
        // when
        Serializable key = transferRepository.create(transferCreate);
    }

    /*
        destination account cant be null
     */
    @Test(expected = MyPersistenceException.class)
    public void testDestinationAccountCantBeNull() {
        // given
        Transfer transferCreate = new Transfer(1L,null,"EUR", new BigDecimal(10));
        // when
        Serializable key = transferRepository.create(transferCreate);
    }

    /*
        currency cant be null
     */
    @Test(expected = MyPersistenceException.class)
    public void testCurrencyCantBeNull() {
        // given
        Transfer transferCreate = new Transfer(1L,2L,null, new BigDecimal(10));
        // when
        Serializable key = transferRepository.create(transferCreate);
    }

    /*
        Balance Cant be null
     */
    @Test(expected = MyPersistenceException.class)
    public void testBalanceCantBeNull() {
        // given
        Transfer transferCreate = new Transfer(1L,2L,"EUR", null);
        // when
        Serializable key = transferRepository.create(transferCreate);
    }
}
