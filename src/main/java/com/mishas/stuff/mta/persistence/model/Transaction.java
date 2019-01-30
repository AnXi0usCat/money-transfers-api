package com.mishas.stuff.mta.persistence.model;

import com.mishas.stuff.common.persistence.IEntity;
import com.mishas.stuff.common.persistence.IValidDto;

public class Transaction implements IEntity, IValidDto {

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public void setId(Long id) {

    }

    @Override
    public boolean isValid() {
        return false;
    }
}
