package com.mishas.stuff.mta.web.dto;

import com.mishas.stuff.common.interfaces.IValidDto;
import com.mishas.stuff.mta.persistence.model.Account;

import java.math.BigDecimal;
import java.util.Objects;

public class AccountDto implements IValidDto {

    private Long id;
    private String currency;
    private BigDecimal balance;

    // constructor

    public AccountDto(){
        super();
    }

    public AccountDto(Long id, String currency, BigDecimal balance) {
        this.id = id;
        this.currency = currency;
        this.balance = balance;
    }

    public AccountDto(Account entity) {
        this.id = entity.getId();
        this.currency = entity.getCurrency();
        this.balance = entity.getBalance();
    }

    // api

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean isValid() {
        return this.currency != null && this.balance != null;
    }

    @Override
    public String toString() {
        return "AccountDto{" +
                "id=" + id +
                ", currency='" + currency + '\'' +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountDto)) return false;
        AccountDto that = (AccountDto) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getCurrency(), that.getCurrency()) &&
                Objects.equals(getBalance(), that.getBalance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCurrency(), getBalance());
    }
}
