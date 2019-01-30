package com.mishas.stuff.mta.web.dto;

import com.mishas.stuff.common.interfaces.IValidDto;

import java.math.BigDecimal;

public class TransferDto implements IValidDto {
    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public BigDecimal getBalance() {
        return null;
    }

    @Override
    public void setBalance(BigDecimal balance) {

    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public void setId(Long id) {

    }
}
