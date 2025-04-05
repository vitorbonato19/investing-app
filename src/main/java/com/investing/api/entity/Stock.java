package com.investing.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "stocks")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ticker;
    @Column(nullable = true)
    private String currency;
    @Column(nullable = true)
    private String shortName;
    @Column(nullable = true)
    private String longName;
    private Long quantity;
    @Column(precision = 20, scale = 2)
    private BigDecimal regularMarketPrice;
    @ManyToOne
    private Account account;

    public Stock() {

    }

    public Stock(Long id, String ticker, String currency, String shortName, String longName, Long quantity, BigDecimal regularMarketPrice, Account account) {
        this.id = id;
        this.ticker = ticker;
        this.currency = currency;
        this.shortName = shortName;
        this.longName = longName;
        this.quantity = quantity;
        this.regularMarketPrice = regularMarketPrice;
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getRegularMarketPrice() {
        return regularMarketPrice;
    }

    public void setRegularMarketPrice(BigDecimal regularMarketPrice) {
        this.regularMarketPrice = regularMarketPrice;
    }

    @JsonIgnore
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
