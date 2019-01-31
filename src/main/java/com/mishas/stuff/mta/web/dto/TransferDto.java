package com.mishas.stuff.mta.web.dto;

import com.mishas.stuff.common.interfaces.IValidDto;
import com.mishas.stuff.mta.persistence.model.Transfer;
import com.mishas.stuff.mta.persistence.model.TransferResult;

import java.math.BigDecimal;

public class TransferDto implements IValidDto {

    private Long id;

    private Long sourceAccount;

    private Long destinationAccount;

    private String currency;

    private BigDecimal balance;

    private TransferResult transferResult;

    public TransferDto() {
        super();
    }

    public TransferDto(Long sourceAccount, Long destinationAccount, String currency, BigDecimal balance, TransferResult transferResult) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.currency = currency;
        this.balance = balance;
        this.transferResult = transferResult;
    }

    public TransferDto(Transfer entity) {
        this.id = entity.getId();
        this.sourceAccount = entity.getSourceAccount();
        this.destinationAccount = entity.getDestinationAccount();
        this.currency = entity.getCurrency();
        this.balance = entity.getBalance();
        this.transferResult = entity.getTransferResult();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Long sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public Long getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(Long destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public TransferResult getTransferResult() {
        return transferResult;
    }

    public void setTransferResult(TransferResult transferResult) {
        this.transferResult = transferResult;
    }

    @Override
    public boolean isValid() {
        return this.sourceAccount != null && this.destinationAccount != null && this.currency != null && this.balance != null;
    }
}
