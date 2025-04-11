package com.investing.api.service;

import com.investing.api.entity.Access;
import com.investing.api.entity.Account;
import com.investing.api.entity.Stock;
import com.investing.api.entity.dto.*;
import com.investing.api.exceptions.InvalidRequestException;
import com.investing.api.exceptions.RegisterNotFoundException;
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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

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

    public void updateEmailByUuid(String uuid, String email) {

        var entity = accountRepository.findByUUID(UUID.fromString(uuid));

        if (entity != null) {
            entity.setEmail(email);
            accountRepository.save(entity);
        } else {
            throw new RegisterNotFoundException("Account not found with UUID " + uuid, HttpStatus.NOT_FOUND);
        }
    }

    public void updatePasswordByUUID(String uuid, String password) {

        var entity = accountRepository.findByUUID(UUID.fromString(uuid));

        if (entity != null) {
            entity.setPassword(password);
            accountRepository.save(entity);
        } else {
            throw new RegisterNotFoundException("Account not found with UUID " + uuid, HttpStatus.NOT_FOUND);
        }
    }

    public void deleteByUuid(String uuid) {

        var entity = accountRepository.findByUUID(UUID.fromString(uuid));

        if (entity != null) {
            accountRepository.deleteByUuid(UUID.fromString(uuid));
        } else {
            throw new RegisterNotFoundException("Account not found with UUID " + uuid, HttpStatus.NOT_FOUND);
        }
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

    public StockAccountResponseDto getStockInfoByTicker(String uuid, String ticker) {

        if (uuid.isBlank() && ticker.isBlank()) {
            throw new InvalidRequestException("Request was invalidated.", HttpStatus.BAD_REQUEST);
        }

        Account entity = accountRepository.findByUUID(UUID.fromString(uuid));

        Stock stock = stockRepository.findByTicker(
                accountRepository.findByUUID(UUID.fromString(uuid)).getId()
                , ticker);

        if (entity == null || stock == null) {
                throw new RegisterNotFoundException("Account or Stock not found, verify the request -> Account ID " + uuid + " Stock Ticker " + ticker, HttpStatus.NOT_FOUND);
            }

        if (!stock.getRegularMarketPrice().equals(BigDecimal.valueOf(quoteEntity(ticker).results().getFirst().regularMarketPrice()))) {

            stock.setRegularMarketPrice(BigDecimal.valueOf(quoteEntity(ticker).results().getFirst().regularMarketPrice()));

            stockRepository.save(stock);

            return new StockAccountResponseDto(
                    UUID.fromString(uuid),
                    ticker,
                    getCurrency(ticker),
                    quoteEntity(ticker).results().getFirst().shortName(),
                    stock.getQuantity(),
                    BigDecimal.valueOf(quoteEntity(ticker).results().getFirst().regularMarketPrice()),
                    BigDecimal.valueOf(mathTotalExternalApi(entity.getStocks().getFirst().getQuantity(), ticker))
            );
        }

        return new StockAccountResponseDto(
                UUID.fromString(uuid),
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

    public void udpateAccountAccessToMedium(String accountId) {

        Account entity = accountRepository.findByUUID(UUID.fromString(accountId));

        Access medium = accessRepository.findByName("MEDIUM");

        List<Access> accesses = entity.getPerms();
        accesses.add(medium);
        entity.setPerms(accesses);
        accountRepository.save(entity);
    }

    public void udpateAccountAccessToHigh(String accountId) {

        Account entity = accountRepository.findByUUID(UUID.fromString(accountId));

        Access high = accessRepository.findByName("HIGH");

        List<Access> accesses = entity.getPerms();
        accesses.add(high);
        entity.setPerms(accesses);
        accountRepository.save(entity);
    }

}
