package com.mishas.stuff.mta.service;

import com.mishas.stuff.common.interfaces.ICrudService;
import com.mishas.stuff.mta.persistence.model.Account;
import com.mishas.stuff.mta.persistence.model.Transfer;
import com.mishas.stuff.mta.persistence.model.TransferResult;

public interface IAccountService extends ICrudService<Account> {

    public boolean accountExists(long id);

    public TransferResult transferFundsBetweenAccounts(Transfer resource);
}
