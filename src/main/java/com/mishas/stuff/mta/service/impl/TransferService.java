package com.mishas.stuff.mta.service.impl;

import com.mishas.stuff.mta.persistence.dao.TransferRepository;
import com.mishas.stuff.mta.persistence.dao.TransferResultRepository;
import com.mishas.stuff.mta.persistence.model.Transfer;
import com.mishas.stuff.mta.service.IAccountService;
import com.mishas.stuff.mta.service.ITransferService;
import com.mishas.stuff.mta.persistence.model.TransferResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;


public class TransferService implements ITransferService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferService.class);

    private final TransferRepository transferRepository = new TransferRepository();
    private final TransferResultRepository transferResultRepository = new TransferResultRepository();
    private final IAccountService accountService = new AccountService();

    @Override
    public Transfer get(long id) {
        LOGGER.info("getting resource with ID: " + id);
        return transferRepository.get(id);
    }

    @Override
    public void create(Transfer resource) {
        TransferResult transferResult = accountService.transferFundsBetweenAccounts(resource);
        resource.setTransferResult(transferResult);
        transferRepository.create(resource);

        // proceed with the money transfer
    }
}
