package com.mishas.stuff.mta.service.impl;

import com.mishas.stuff.mta.persistence.dao.TransferRepository;
import com.mishas.stuff.mta.persistence.model.Transfer;
import com.mishas.stuff.mta.service.IAccountService;
import com.mishas.stuff.mta.service.ITransferService;
import com.mishas.stuff.mta.persistence.model.TransferResult;
import com.mishas.stuff.mta.web.dto.TransferDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TransferService implements ITransferService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferService.class);

    private  TransferRepository transferRepository;
    private IAccountService accountService;

    public TransferService(TransferRepository transferRepository,  IAccountService accountService) {
        this.transferRepository = transferRepository;
        this.accountService = accountService;
    }

    @Override
    public TransferDto get(long id) {
        LOGGER.info("getting resource with ID: " + id);
        if (transferExists(id)){
            return new TransferDto(transferRepository.get(id));
        }
        return new TransferDto();
    }

    @Override
    public void create(TransferDto resource) {
        LOGGER.info("creating a new resource: " + resource.toString());
        Transfer entity = new Transfer(resource);
        TransferResult transferResult = accountService.transferFundsBetweenAccounts(entity);
        entity.setTransferResult(transferResult);
        transferRepository.create(entity);
    }

    @Override
    public boolean transferExists(long id) {
        return transferRepository.transferExists(id);
    }
}
