package com.mishas.stuff.mta.web.dto;

import com.mishas.stuff.common.interfaces.IValidDto;

import java.math.BigDecimal;

public class TransferResultDto implements IValidDto {

    private Long sourceAccountId;
    private BigDecimal balance;
    private BigDecimal previousBalance;
    private BigDecimal transactionAmmount;
    private String currency;

    // constructor

    public TransferResultDto(){
        super();
    }

    public TransferResultDto(Long sourceAccountId, BigDecimal balance, BigDecimal previousBalance, BigDecimal transactionAmmount, String currency) {
        this.sourceAccountId = sourceAccountId;
        this.balance = balance;
        this.previousBalance = previousBalance;
        this.transactionAmmount = transactionAmmount;
        this.currency = currency;
    }

    // api

    public Long getSourceAccountId() {
        return sourceAccountId;
    }

    public void setSourceAccountId(Long sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public BigDecimal getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(BigDecimal previousBalance) {
        this.previousBalance = previousBalance;
    }

    public BigDecimal getTransactionAmmount() {
        return transactionAmmount;
    }

    public void setTransactionAmmount(BigDecimal transactionAmmount) {
        this.transactionAmmount = transactionAmmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

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

    @Override
    public String toString() {
        return "TransferResultDto{" +
                "sourceAccountId=" + sourceAccountId +
                ", balance=" + balance +
                ", previousBalance=" + previousBalance +
                ", transactionAmmount=" + transactionAmmount +
                ", currency='" + currency + '\'' +
                '}';
    }
}
