package com.mishas.stuff.unit;

import com.mishas.stuff.mta.persistence.dao.TransferRepository;
import com.mishas.stuff.mta.persistence.model.Transfer;
import com.mishas.stuff.mta.persistence.model.TransferResult;
import com.mishas.stuff.mta.service.impl.AccountService;
import com.mishas.stuff.mta.service.impl.TransferService;
import com.mishas.stuff.mta.web.dto.TransferDto;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransferServiceTest {

    private TransferRepository transferRepository;
    private AccountService accountService;
    private TransferService transferService;
    private TransferDto transferDto;
    private TransferResult transferResult;

    @Before
    public void before(){
        transferDto = new TransferDto( 1L, 2L, "GBP", new BigDecimal(1));
        transferResult = new TransferResult();
        transferRepository = mock(TransferRepository.class);
        accountService = mock(AccountService.class);
        transferService = new TransferService(transferRepository, accountService);
    }

    /*
        Transfer doesn't exist return an empty Dto
     */
    @Test
    public void testGetMethodWhenAccountDoesntExist_returnsEmptyTransactionDto() {
        when(transferRepository.transferExists(1L)).thenReturn(false);
        assertEquals(transferService.get(1L), new TransferDto());
    }

    /*
        Transfer exists, return new Transfer
     */
    @Test
    public void testGetMethodWhenAccountExist_Tansfer() {
        when(transferRepository.transferExists(1L)).thenReturn(true);
        when(transferRepository.get(1L)).thenAnswer(invocation -> new Transfer( 1L, 2L, "GBP", new BigDecimal(1)));
        assertEquals(transferService.get(1L), transferDto);
    }


    /*
        created method works
     */
    @Test
    public void testCreateMethod() {
        when(accountService.transferFundsBetweenAccounts(
                new Transfer(
                        1L,
                        2L,
                        "GBP",
                        new BigDecimal(1)))
        ).thenAnswer(invocation -> new TransferResult());
        transferService.create(transferDto);
    }

}
