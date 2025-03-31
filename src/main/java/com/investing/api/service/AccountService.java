package com.investing.api.service;

import com.investing.api.entity.Stock;
import com.investing.api.entity.dto.AccountRequestDto;
import com.investing.api.entity.dto.AccountResponseDto;
import com.investing.api.exceptions.RegisterNotFoundException;
import com.investing.api.mapper.AccountMapper;
import com.investing.api.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
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

    public AccountResponseDto getTotalStocks(String uuid) {

        var entity = accountRepository.findByUUID(UUID.fromString(uuid));

        if (entity != null) {

            var stocks = entity.getStocks().stream()
                    .map(stock -> new );

        }

    }

}
