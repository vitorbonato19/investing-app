package com.investing.api.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.security.Permissions;
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
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "accountaccess", joinColumns = @JoinColumn(name = "accountId"), inverseJoinColumns = @JoinColumn(name = "accessId"))
    private List<Access> perms;

    public Account() {

    }

    public Account(Long id, UUID uuid, String name, String document, String password, String email, BigDecimal equity, List<Stock> stocks, List<Access> perms) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.document = document;
        this.password = password;
        this.email = email;
        this.equity = equity;
        this.stocks = stocks;
        this.perms = perms;
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

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public @NotNull String getDocument() {
        return document;
    }

    public void setDocument(@NotNull String document) {
        this.document = document;
    }

    public @NotNull @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotNull @NotBlank String password) {
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

    public List<Access> getPerms() {
        return perms;
    }

    public void setPerms(List<Access> perms) {
        this.perms = perms;
    }
}
