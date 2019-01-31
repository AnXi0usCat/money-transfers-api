package com.mishas.stuff.mta.service.impl;

import com.mishas.stuff.common.utils.exceptions.MyMissingResourceException;
import com.mishas.stuff.mta.persistence.dao.AccountRepository;
import com.mishas.stuff.mta.persistence.model.Account;
import com.mishas.stuff.mta.persistence.model.Transfer;
import com.mishas.stuff.mta.service.IAccountService;
import com.mishas.stuff.mta.persistence.model.TransferResult;
import com.mishas.stuff.mta.web.dto.AccountDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AccountService implements IAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository = new AccountRepository();

    @Override
    public AccountDto get(long id) {
        LOGGER.info("getting resource with ID: " + id);
        if (accountExists(id)){
            return new AccountDto(accountRepository.get(id));
        }
        return new AccountDto();
    }

    @Override
    public void create(AccountDto resource) {
        LOGGER.info("creating a new resource" + resource.toString());
        accountRepository.create(new Account(resource));
    }

    @Override
    public AccountDto update(long id, AccountDto resource) {
        LOGGER.info("updating a resource with Id: " + id);
        Account entity = accountRepository.update(id, new Account(resource));;
        return new AccountDto(entity);
    }

    @Override
    public void delete(long id) {
        LOGGER.info("deleting a resource with Id: " + id);
        accountRepository.delete(id);
    }

    public boolean accountExists(long id) {
        return accountRepository.accountExists(id);
    }

    @Override
    public TransferResult transferFundsBetweenAccounts(Transfer resource) {
        long sourceAccount = resource.getSourceAccount();
        long destinationAccount = resource.getDestinationAccount();

        // check if the accounts exist
        if (!accountRepository.accountExists(sourceAccount) || !accountRepository.accountExists(destinationAccount)) {
            LOGGER.error("One or more Bank Accounts do not exist");
            throw new MyMissingResourceException("One or more Bank Accounts do not exist, cannot transfer funds. Source Account ID: "
                    + sourceAccount + "Destination Account ID: " + destinationAccount);
        }
        return accountRepository.transferFundsBetweenAccounts(resource);
    }
}
