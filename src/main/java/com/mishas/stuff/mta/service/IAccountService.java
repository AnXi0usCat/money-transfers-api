package com.mishas.stuff.mta.service;

import com.mishas.stuff.common.interfaces.ICrudService;
import com.mishas.stuff.mta.persistence.model.Account;
import com.mishas.stuff.mta.persistence.model.Transfer;
import com.mishas.stuff.mta.web.dto.TransferResultDto;

public interface IAccountService extends ICrudService<Account> {

    public boolean accountExists(long id);

    public TransferResultDto transferFundsBetweenAccounts(Transfer resource);
}
