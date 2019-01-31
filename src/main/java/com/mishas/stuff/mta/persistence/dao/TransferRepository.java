package com.mishas.stuff.mta.persistence.dao;

import com.mishas.stuff.mta.persistence.model.Transfer;

public class TransferRepository extends GenericCrudRepository<Transfer> {

    public TransferRepository() {
        super(Transfer.class);
    }

    public boolean transferExists(long id) {
        return super.get(id) != null;
    }
}
