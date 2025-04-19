package com.investing.api.service;

import com.investing.api.entity.Access;
import com.investing.api.entity.Account;
import com.investing.api.entity.Stock;
import com.investing.api.entity.dto.*;
import com.investing.api.exceptions.InvalidRequestException;
import com.investing.api.exceptions.InvalidAccessChangeException;
import com.investing.api.exceptions.RegisterNotFoundException;
import com.investing.api.exceptions.StockNotFoundException;
import com.investing.api.feign.BrapiExternalApi;
import com.investing.api.mapper.AccountMapper;
import com.investing.api.repository.AccessRepository;
import com.investing.api.repository.AccountRepository;
import com.investing.api.repository.StockRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class AccountService {

    @Value("${api.key}")
    private String BRAPI_TOKEN;

    private final AccountRepository accountRepository;

    private final AccessRepository accessRepository;

    private final AccountMapper accountMapper;

    private final BrapiExternalApi externalApi;

    private final StockRepository stockRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AccountService(AccountRepository accountRepository, AccessRepository accessRepository, AccountMapper accountMapper, BrapiExternalApi externalApi, StockRepository stockRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.accountRepository = accountRepository;
        this.accessRepository = accessRepository;
        this.accountMapper = accountMapper;
        this.externalApi = externalApi;
        this.stockRepository = stockRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<AccountResponseDto> findAll()    {
        return accountMapper.entityToResponse(accountRepository.findAll());
    }

    public Account findByDocument(String document) {
        return accountRepository.findByDocument(document).orElseThrow(
                () -> new RegisterNotFoundException("User not found with document " + document, HttpStatus.NOT_FOUND)
        );
    }

    public Account findByEmail(@Email String email) {
        return accountRepository.findByEmail(email).orElseThrow(
                () -> new RegisterNotFoundException("user not found with email " + email, HttpStatus.NOT_FOUND)
        );
    }

    public Account findByUuid(String accountid) {
        return accountRepository.findByUUID(UUID.fromString(accountid))
                .orElseThrow(
                        () -> new RegisterNotFoundException("user not found with id " + accountid, HttpStatus.NOT_FOUND)
                );
    }

    @Transactional
    public AccountResponseDto create(@NotNull @NotBlank @NotEmpty AccountRequestDto request) {

        Account entity = accountMapper.requestToEntity(request);
        Access basic = accessRepository.findByName("BASIC");
        List<Access> accesses = new ArrayList<>();
        accesses.add(basic);
        entity.setPassword(bCryptPasswordEncoder.encode(request.password()));
        entity.setPerms(accesses);

        Account response = accountRepository.save(entity);

        return accountMapper.entityToResponse(response);
    }

    public void updateEmailByUuid(String accountId, String email) {

        Account entity = findByUuid(accountId);
        entity.setEmail(email);
        accountRepository.save(entity);

    }

    public void updatePasswordByUUID(String accountId, String password) {

        Account entity = findByUuid(accountId);
        entity.setPassword(bCryptPasswordEncoder.encode(password));
        accountRepository.save(entity);

    }

    public void deleteByUuid(String accountId) {

        Account entity = findByUuid(accountId);
        accountRepository.deleteById(entity.getId());

    }

    public Boolean verifyAccountLoginCredentials(LoginRequestDto request) {
        Account entity = accountRepository.findByDocument(request.document()).orElseThrow(
                () ->
                        new RegisterNotFoundException(
                                "User not found with document " + request.document() + ", please verifiy your request.",
                                HttpStatus.NOT_FOUND)
        );
        return bCryptPasswordEncoder.matches(request.password(), entity.getPassword());
    }

    public StockAccountResponseDto getStockInfoByTicker(String accountId, String ticker) {

        if (accountId.isBlank() || ticker.isBlank()) {
            throw new InvalidRequestException("Request was invalidated.", HttpStatus.BAD_REQUEST);
        }

        Account entity = findByUuid(accountId);

        Stock stock = stockRepository.findByTicker(
                findByUuid(accountId).getId()
                , ticker);

        if (stock == null) {
            throw new StockNotFoundException("Stock " + ticker + " not found for account " + accountId, HttpStatus.NOT_FOUND);
        }

        if (!stock.getRegularMarketPrice().equals(BigDecimal.valueOf(quoteEntity(ticker).results().getFirst().regularMarketPrice()))) {

            stock.setRegularMarketPrice(BigDecimal.valueOf(quoteEntity(ticker).results().getFirst().regularMarketPrice()));

            stockRepository.save(stock);

            return new StockAccountResponseDto(
                    UUID.fromString(accountId),
                    ticker,
                    getCurrency(ticker),
                    quoteEntity(ticker).results().getFirst().shortName(),
                    stock.getQuantity(),
                    BigDecimal.valueOf(quoteEntity(ticker).results().getFirst().regularMarketPrice()),
                    BigDecimal.valueOf(mathTotalExternalApi(entity.getStocks().getFirst().getQuantity(), ticker))
            );
        }
        return new StockAccountResponseDto(
                UUID.fromString(accountId),
                ticker,
                getCurrency(ticker),
                quoteEntity(ticker).results().getFirst().shortName(),
                stock.getQuantity(),
                stock.getRegularMarketPrice(),
                stock.getRegularMarketPrice().multiply(BigDecimal.valueOf(stock.getQuantity()))
        );
    }

    public Double mathTotalExternalApi(Long quantity, String ticker) {

        return externalApi.quote(BRAPI_TOKEN, ticker).results().getFirst().regularMarketPrice() * quantity;
    }

    public String getCurrency(String ticker) {
        return externalApi.quote(BRAPI_TOKEN, ticker).results().getFirst().currency();
    }

    public BrapiQuoteDto quoteEntity(String ticker) {
        return externalApi.quote(BRAPI_TOKEN, ticker);
    }

    public void updateAccountAccessToMedium(String accountId) {

        Account entity = findByUuid(accountId);

        Access medium = accessRepository.findByName("MEDIUM");
        Access basic = accessRepository.findByName("BASIC");

        List<Access> accesses = entity.getPerms();

        List<Access> containsHighAccess =
                accesses.stream()
                        .filter(
                                a -> a.getName().contentEquals("HIGH") || a.getName().contentEquals("ADMIN")).toList();

        if (!containsHighAccess.isEmpty()) {
             throw new InvalidAccessChangeException("invalid access change request, owner have a access higher than the update request", HttpStatus.NOT_ACCEPTABLE);
        }

        accesses.clear();
        accesses.add(basic);
        accesses.add(medium);
        entity.setPerms(accesses);
        accountRepository.save(entity);
    }

    public void updateAccountAccessToHigh(String accountId) {

        Account entity = findByUuid(accountId);

        Access basic = accessRepository.findByName("BASIC");
        Access medium = accessRepository.findByName("MEDIUM");
        Access high = accessRepository.findByName("HIGH");

        List<Access> accesses = entity.getPerms();


        List<Access> containsHighAccess =
                accesses.stream()
                        .filter(
                                a -> a.getName().contentEquals("ADMIN")).toList();

        if (!containsHighAccess.isEmpty()) {
            throw new InvalidAccessChangeException("invalid access change request, owner have a access higher than the update request", HttpStatus.NOT_ACCEPTABLE);
        }

        accesses.clear();
        accesses.add(basic);
        accesses.add(medium);
        accesses.add(high);
        entity.setPerms(accesses);
        accountRepository.save(entity);
    }

}
