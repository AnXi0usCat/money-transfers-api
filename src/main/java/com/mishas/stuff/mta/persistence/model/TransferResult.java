package com.mishas.stuff.mta.persistence.model;

import com.google.gson.annotations.Expose;
import com.mishas.stuff.common.interfaces.IEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "transfer_result")
public class TransferResult implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Column(name = "previous_balance", nullable = false)
    private BigDecimal previousBalance;

    @Column(name = "transaction_amount", nullable = false)
    private BigDecimal transactionAmount;

    @Column(name = "source_account_currency", nullable = false)
    private String sourceAccountCurrency;

    @Column(name = "transfer_currency", nullable = false)
    private String transferCurrency;

    @OneToOne(mappedBy = "transferResult", fetch = FetchType.EAGER)
    private transient Transfer transfer;

    // constructor

    public TransferResult(){
        super();
    }

    public TransferResult(
            BigDecimal balance,
            BigDecimal previousBalance,
            BigDecimal transactionAmount,
            String sourceAccountCurrency,
            String transferCurrency) {
        this.balance = balance;
        this.previousBalance = previousBalance;
        this.transactionAmount = transactionAmount;
        this.sourceAccountCurrency = sourceAccountCurrency;
        this.transferCurrency = transferCurrency;
    }

    // api

    public BigDecimal getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(BigDecimal previousBalance) {
        this.previousBalance = previousBalance;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getSourceAccountCurrency() {
        return sourceAccountCurrency;
    }

    public void setSourceAccountCurrency(String sourceAccountCurrency) {
        this.sourceAccountCurrency = sourceAccountCurrency;
    }

    public String getTransferCurrency() {
        return transferCurrency;
    }

    public void setTransferCurrency(String transferCurrency) {
        this.transferCurrency = transferCurrency;
    }

    public Transfer getTransfer() {
        return transfer;
    }

    public void setTransfer(Transfer transfer) {
        this.transfer = transfer;
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
        return "TransferResult{" +
                "id=" + id +
                ", balance=" + balance +
                ", previousBalance=" + previousBalance +
                ", transactionAmount=" + transactionAmount +
                ", sourceAccountCurrency='" + sourceAccountCurrency + '\'' +
                ", transferCurrency='" + transferCurrency + '\'' +
                '}';
    }
}
