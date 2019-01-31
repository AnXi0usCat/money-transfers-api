package com.mishas.stuff.mta.service;

import com.mishas.stuff.common.interfaces.ICrudService;
import com.mishas.stuff.mta.web.dto.TransferDto;

public interface ITransferService extends ICrudService<TransferDto> {

    // we don't need these methods so default implementation will do

    default TransferDto update(long id, TransferDto resource) {
        return new TransferDto();
    }

    default void delete(long id) {}
}
