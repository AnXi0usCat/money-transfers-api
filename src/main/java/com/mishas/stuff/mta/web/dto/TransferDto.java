package com.mishas.stuff.mta.web.dto;

import com.mishas.stuff.common.interfaces.IValidDto;
import com.mishas.stuff.mta.persistence.model.Transfer;
import com.mishas.stuff.mta.persistence.model.TransferResult;

import java.math.BigDecimal;
import java.util.Objects;

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

    public TransferDto(Long sourceAccount, Long destinationAccount, String currency, BigDecimal balance) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.currency = currency;
        this.balance = balance;
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

    @Override
    public String toString() {
        return "TransferDto{" +
                "id=" + id +
                ", sourceAccount=" + sourceAccount +
                ", destinationAccount=" + destinationAccount +
                ", currency='" + currency + '\'' +
                ", balance=" + balance +
                ", transferResult=" + transferResult +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransferDto)) return false;
        TransferDto that = (TransferDto) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getSourceAccount(), that.getSourceAccount()) &&
                Objects.equals(getDestinationAccount(), that.getDestinationAccount()) &&
                Objects.equals(getCurrency(), that.getCurrency()) &&
                Objects.equals(getBalance(), that.getBalance()) &&
                Objects.equals(getTransferResult(), that.getTransferResult());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSourceAccount(), getDestinationAccount(), getCurrency(), getBalance(), getTransferResult());
    }
}
