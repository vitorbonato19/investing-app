package com.investing.api.service;

import com.investing.api.entity.Account;
import com.investing.api.entity.Stock;
import com.investing.api.entity.dto.AccountRequestDto;
import com.investing.api.entity.dto.AccountResponseDto;
import com.investing.api.entity.dto.BrapiQuoteDto;
import com.investing.api.entity.dto.StockAccountResponseDto;
import com.investing.api.exceptions.InvalidRequestException;
import com.investing.api.exceptions.RegisterNotFoundException;
import com.investing.api.feign.BrapiExternalApi;
import com.investing.api.mapper.AccountMapper;
import com.investing.api.repository.AccountRepository;
import com.investing.api.repository.StockRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {


    @Value("${api.key}")
    private String BRAPI_TOKEN;

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    private final BrapiExternalApi externalApi;

    private final StockRepository stockRepository;

    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper, BrapiExternalApi externalApi, StockRepository stockRepository) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.externalApi = externalApi;
        this.stockRepository = stockRepository;
    }

    public List<AccountResponseDto> findAll() {
        return accountMapper.entityToResponse(accountRepository.findAll());
    }

    @Transactional
    public AccountResponseDto create(@NotNull @NotBlank @NotEmpty AccountRequestDto request) {

        var entity = accountMapper.requestToEntity(request);

        accountRepository.save(entity);

        return accountMapper.entityToResponse(entity);
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

    public StockAccountResponseDto getStockInfoByTicker(String uuid, String ticker) {

        if (uuid != null
                && !uuid.isEmpty()
                && !uuid.isBlank()
                && ticker != null
                && !ticker.isBlank()
                && !ticker.isEmpty()
        ) {
            Account entity = accountRepository.findByUUID(UUID.fromString(uuid));

            Stock stock = stockRepository.findByTicker(
                    accountRepository.findByUUID(UUID.fromString(uuid)).getId()
                    ,ticker);

            if (entity != null && stock != null) {

                if (!stock.getRegularMarketPrice().equals(BigDecimal.valueOf(quoteEntity(ticker).results().getFirst().regularMarketPrice()))) {

                    stock.setRegularMarketPrice(BigDecimal.valueOf(quoteEntity(ticker).results().getFirst().regularMarketPrice()));

                    return new StockAccountResponseDto(
                            UUID.fromString(uuid),
                            entity.getName(),
                            ticker,
                            getCurrency(ticker),
                            quoteEntity(ticker).results().getFirst().shortName(),
                            stock.getQuantity(),
                            BigDecimal.valueOf(quoteEntity(ticker).results().getFirst().regularMarketPrice()),
                            BigDecimal.valueOf(mathTotalExternalApi(entity.getStocks().getFirst().getQuantity(), ticker))
                    );
                } else {

                    return new StockAccountResponseDto(
                            UUID.fromString(uuid),
                            entity.getName(),
                            ticker,
                            getCurrency(ticker),
                            quoteEntity(ticker).results().getFirst().shortName(),
                            stock.getQuantity(),
                            stock.getRegularMarketPrice(),
                            stock.getRegularMarketPrice().multiply(BigDecimal.valueOf(stock.getQuantity()))
                    );

                }
            } else {
                throw new RegisterNotFoundException("Account or stock not found for id " + uuid + " and ticker " + ticker + ".", HttpStatus.NOT_FOUND);
            }
        }
        throw new InvalidRequestException("Request was invalidated.", HttpStatus.BAD_REQUEST);
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

}
