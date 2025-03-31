package com.investing.api.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @UuidGenerator
    @Column(unique = true, nullable = false, updatable = false)
    private UUID uuid;
    @NotNull
    private String name;
    @Column(unique = true)
    @NotNull
    private String document;
    @NotNull
    @NotBlank
    private String password;
    @Column(unique = true)  
    private String email;
    @Column(precision = 20, scale = 2)
    private BigDecimal equity;
    @OneToMany(mappedBy = "account")
    private List<Stock> stocks;

    public Account() {

    }
    public Account(Long id, UUID uuid, String name, String document, String password, String email, BigDecimal equity, List<Stock> stocks) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.document = document;
        this.password = password;
        this.email = email;
        this.equity = equity;
        this.stocks = stocks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getEquity() {
        return equity;
    }

    public void setEquity(BigDecimal equity) {
        this.equity = equity;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }
}
