package com.mishas.stuff.mta.service.impl;

import com.mishas.stuff.mta.persistence.dao.AccountRepository;
import com.mishas.stuff.mta.persistence.model.Account;
import com.mishas.stuff.mta.service.IAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AccountService implements IAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository = new AccountRepository();

    @Override
    public Account get(long id) {
        LOGGER.info("getting Account with ID: " + id);
        return accountRepository.get(id);
    }

    @Override
    public void create(Account resource) {
        LOGGER.info("creating a new resource" + resource.toString());
        accountRepository.create(resource);
    }

    @Override
    public Account update(long id, Account resource) {

        return accountRepository.update(id, resource);
    }

    @Override
    public void delete(long id) {
        accountRepository.delete(id);
    }
}
