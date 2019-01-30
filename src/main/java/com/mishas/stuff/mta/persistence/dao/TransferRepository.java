package com.mishas.stuff.mta.persistence.dao;

import com.mishas.stuff.mta.persistence.model.Transfer;

public class TransferRepository extends GenericCrudRepository<Transfer> {

    public TransferRepository() {
        super(Transfer.class);
    }
}
