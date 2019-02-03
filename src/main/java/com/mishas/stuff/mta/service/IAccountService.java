package com.mishas.stuff.mta.service;

import com.mishas.stuff.common.interfaces.ICrudService;
import com.mishas.stuff.mta.persistence.model.Transfer;
import com.mishas.stuff.mta.persistence.model.TransferResult;
import com.mishas.stuff.mta.web.dto.AccountDto;

public interface IAccountService extends ICrudService<AccountDto> {

    public boolean accountExists(long id);

    public TransferResult transferFundsBetweenAccounts(Transfer resource);
}
