package com.mishas.stuff.mta.service.impl;

import com.mishas.stuff.common.utils.exceptions.MyMissingResourceException;
import com.mishas.stuff.mta.persistence.dao.AccountRepository;
import com.mishas.stuff.mta.persistence.model.Account;
import com.mishas.stuff.mta.persistence.model.Transfer;
import com.mishas.stuff.mta.service.IAccountService;
import com.mishas.stuff.mta.web.dto.TransferResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AccountService implements IAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository = new AccountRepository();

    @Override
    public Account get(long id) {
        LOGGER.info("getting resource with ID: " + id);
        return accountRepository.get(id);
    }

    @Override
    public void create(Account resource) {
        LOGGER.info("creating a new resource" + resource.toString());
        accountRepository.create(resource);
    }

    @Override
    public Account update(long id, Account resource) {
        LOGGER.info("updating a resource with Id: " + id);
        return accountRepository.update(id, resource);
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
    public TransferResultDto transferFundsBetweenAccounts(Transfer resource) {
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
