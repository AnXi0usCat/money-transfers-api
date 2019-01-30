package com.mishas.stuff.mta.persistence.model;

import com.mishas.stuff.common.persistence.IEntity;
import com.mishas.stuff.common.persistence.IValidDto;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "account")
public class Account implements IEntity, IValidDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Column(name = "reserved_balance", nullable = false)
    private BigDecimal reservedBalance;

    // constructor

    public Account() {
        super();
    }

    public Account(String currency, BigDecimal balance, BigDecimal reservedBalance) {
        this.currency = currency;
        this.balance = balance;
        this.reservedBalance = reservedBalance;
    }

    public Account(Long id, String currency, BigDecimal balance, BigDecimal reservedBalance) {
        this.id = id;
        this.currency = currency;
        this.balance = balance;
        this.reservedBalance = reservedBalance;
    }

    // api

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getReservedBalance() {
        return reservedBalance;
    }

    public void setReservedBalance(BigDecimal reservedBalance) {
        this.reservedBalance = reservedBalance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", currency='" + currency + '\'' +
                ", balance=" + balance +
                ", reservedBalance=" + reservedBalance +
                '}';
    }

    // api - other

    /**
     * checks if json payload was valid
     * @return bolean
     */
    @Override
    public boolean isValid() {
        return this.currency != null && this.balance != null && this.reservedBalance != null;
    }
}
