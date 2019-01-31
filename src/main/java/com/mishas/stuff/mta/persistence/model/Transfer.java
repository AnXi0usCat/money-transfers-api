package com.mishas.stuff.mta.persistence.model;

import com.mishas.stuff.common.interfaces.IEntity;
import com.mishas.stuff.mta.web.dto.TransferDto;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "transfer")
public class Transfer implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "source_account", nullable = false)
    private Long sourceAccount;

    @Column(name = "destination_account", nullable = false)
    private Long destinationAccount;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "transfered_amount", nullable = false)
    private BigDecimal balance;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transfer_result_id", referencedColumnName = "id")
    private TransferResult transferResult;

    // controller

    public Transfer() {
        super();
    }

    public Transfer(Long sourceAccount, Long destinationAccount, String currency, BigDecimal balance) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.currency = currency;
        this.balance = balance;
    }

    public Transfer(Long id, Long sourceAccount, Long destinationAccount, String currency, BigDecimal balance) {
        this.id = id;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.currency = currency;
        this.balance = balance;
    }

    // dto to entity
    public Transfer(TransferDto resource) {
        this.sourceAccount = resource.getSourceAccount();
        this.destinationAccount = resource.getDestinationAccount();
        this.currency = resource.getCurrency();
        this.balance = resource.getBalance();
        this.transferResult = resource.getTransferResult();
    }

    // api


    public TransferResult getTransferResult() {
        return transferResult;
    }

    public void setTransferResult(TransferResult transferResult) {
        this.transferResult = transferResult;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", sourceAccount=" + sourceAccount +
                ", destinationAccount=" + destinationAccount +
                ", currency='" + currency + '\'' +
                ", balance=" + balance +
                ", transferResult=" + transferResult +
                '}';
    }
}
