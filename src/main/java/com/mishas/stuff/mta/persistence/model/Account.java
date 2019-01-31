package com.mishas.stuff.mta.persistence.model;

import com.mishas.stuff.common.interfaces.IEntity;
import com.mishas.stuff.mta.web.dto.AccountDto;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;


@Entity
@Table(name = "account")
public class Account implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;


    // constructor

    public Account() {
        super();
    }

    public Account(String currency) {
        this.currency = currency;
        this.balance = new BigDecimal(0);

    }

    public Account(String currency, BigDecimal balance) {
        this.currency = currency;
        this.balance = balance;

    }

    public Account(Long id, String currency, BigDecimal balance) {
        this.id = id;
        this.currency = currency;
        this.balance = balance;
    }

    // Controller: convert from Dto to Entity

    public Account(AccountDto accountDto) {
        this.id = accountDto.getId();
        this.currency = accountDto.getCurrency();
        this.balance = accountDto.getBalance();
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

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", currency='" + currency + '\'' +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return Objects.equals(getId(), account.getId()) &&
                Objects.equals(getCurrency(), account.getCurrency()) &&
                Objects.equals(getBalance(), account.getBalance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCurrency(), getBalance());
    }
}
