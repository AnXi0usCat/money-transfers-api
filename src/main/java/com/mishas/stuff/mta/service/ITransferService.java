package com.mishas.stuff.mta.service;

import com.mishas.stuff.common.interfaces.ICrudService;
import com.mishas.stuff.mta.persistence.model.Transfer;

public interface ITransferService extends ICrudService<Transfer> {

    // we don't need these methods so default implementation will do

    default Transfer update(long id, Transfer resource) {
        return new Transfer();
    }

    default void delete(long id) {}
}
