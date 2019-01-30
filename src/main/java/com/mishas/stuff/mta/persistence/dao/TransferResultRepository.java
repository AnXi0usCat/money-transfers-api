package com.mishas.stuff.mta.persistence.dao;


import com.mishas.stuff.mta.persistence.model.TransferResult;

public class TransferResultRepository extends GenericCrudRepository<TransferResult> {

    public TransferResultRepository() {
        super(TransferResult.class);
    }
}
