package com.investing.api.service;

import com.investing.api.entity.dto.AccountRequestDto;
import com.investing.api.entity.dto.AccountResponseDto;
import com.investing.api.entity.dto.StockAccountResponseDto;
import com.investing.api.exceptions.RegisterNotFoundException;
import com.investing.api.feign.BrapiExternalApi;
import com.investing.api.mapper.AccountMapper;
import com.investing.api.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {


    @Value("xoh96BQ4QMfLr6Ue5U8GYN")
    private String BRAPI_TOKEN;

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    private final BrapiExternalApi externalApi;

    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper, BrapiExternalApi externalApi) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.externalApi = externalApi;
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

    public List<StockAccountResponseDto> getTotalStocks(String uuid, String ticker) {

        var entity = accountRepository.findByUUID(UUID.fromString(uuid));

        var brapi = externalApi.quote(BRAPI_TOKEN, ticker);

        List<StockAccountResponseDto> stocks = new ArrayList<>();

        return entity.getStocks().stream().map(
                sts -> new StockAccountResponseDto(
                        entity.getUuid(),
                        entity.getName(),
                        brapi.results().getFirst().symbol(),
                        brapi.results().getFirst().currency(),
                        brapi.results().getFirst().shortName(),
                        entity.getStocks().getFirst().getQuantity(),
                        brapi.results().getFirst().regularMarketPrice(),
                        brapi.results().getFirst().regularMarketPrice() * entity.getStocks().getFirst().getQuantity())).toList() ;
    }

}
