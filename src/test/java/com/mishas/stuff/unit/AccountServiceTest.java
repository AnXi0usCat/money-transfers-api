package com.mishas.stuff.unit;

import com.mishas.stuff.common.utils.exceptions.MyMissingResourceException;
import com.mishas.stuff.mta.persistence.dao.AccountRepository;
import com.mishas.stuff.mta.persistence.model.Account;
import com.mishas.stuff.mta.persistence.model.Transfer;
import com.mishas.stuff.mta.persistence.model.TransferResult;
import com.mishas.stuff.mta.service.impl.AccountService;
import com.mishas.stuff.mta.web.dto.AccountDto;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountServiceTest {

    private AccountService accountService;
    private AccountRepository accountRepository;
    private AccountDto accountDto;
    private AccountDto accountDtoEmpty;

    @Before
    public void before() {
        accountDto = new AccountDto(1L, "GBP", new BigDecimal(100));
        accountDtoEmpty = new AccountDto();
        accountRepository = mock(AccountRepository.class);
        accountService = new AccountService(accountRepository);
    }

    /*
        returns Empty DTO when account doesn't exist
     */
    @Test
    public void testGetMethodWhenAccountDoesntExist_retuensEmptyDto() {
       assertEquals(accountDtoEmpty, accountService.get(1000L));
    }

    /*
        return the correct dto when
     */
    @Test
    public void testReturnsAccountDtoWhenItExists_returnsResult() {
        when(accountRepository.accountExists(1L)).thenReturn(true);
        when(accountRepository.get(1L)).thenAnswer(
                invocation -> new Account(1L, "GBP", new BigDecimal(100))
        );
        assertEquals(accountDto, accountService.get(1L));
    }

    /*
        returns Empty DTO when account doesn't exist
     */
    @Test
    public void testUpdateMethodWhenAccountDoesntExist_returnEmptyDto() {
        assertEquals(
                accountDtoEmpty,
                accountService.update(
                        1000,
                        new AccountDto(1000L, "GBP", new BigDecimal(100)))
        );
    }

    /*
        returns updated object when it exist
     */
    @Test
    public void testUpdateMethodWhenAccounExists_returnUpdatedObject() {
        when(accountRepository.accountExists(1L)).thenReturn(true);
        when(accountRepository.update(1L, new Account(accountDto))).thenAnswer(
                invocation -> new Account());
        assertEquals(new AccountDto(), accountService.update(1L, accountDto));

    }

    /*
        throws exception when neither account exists
     */
    @Test(expected = MyMissingResourceException.class)
    public void testTransferFundsBetweenAccountsNeitherAccountExists_throwsException() {
        when(accountRepository.accountExists(1L)).thenReturn(false);
        when(accountRepository.accountExists(2L)).thenReturn(false);
        Transfer transfer = new Transfer(1L, 2L, "GBP", new BigDecimal(100));
        accountService.transferFundsBetweenAccounts(transfer);
    }

    /*
    throws exception when one account doesnt exists
    */
    @Test(expected = MyMissingResourceException.class)
    public void testTransferFundsBetweenAccountsOneAccountDoesntExist_throwsException() {
        when(accountRepository.accountExists(1L)).thenReturn(true);
        when(accountRepository.accountExists(2L)).thenReturn(false);
        Transfer transfer = new Transfer(1L, 2L, "GBP", new BigDecimal(100));
        accountService.transferFundsBetweenAccounts(transfer);
    }

    @Test
    public void testTransferFundsBetweenAccountsBothAccountsExist_returnsTransferResult() {
        when(accountRepository.accountExists(1L)).thenReturn(true);
        when(accountRepository.accountExists(2L)).thenReturn(true);
        Transfer transfer = new Transfer(1L, 2L, "GBP", new BigDecimal(100));
        when(accountRepository.transferFundsBetweenAccounts(transfer)).thenAnswer(invocation-> new TransferResult());
        accountService.transferFundsBetweenAccounts(transfer);
    }
}
