package com.investing.api.service;

import com.investing.api.entity.dto.AccountRequestDto;
import com.investing.api.entity.dto.AccountResponseDto;
import com.investing.api.mapper.AccountMapper;
import com.investing.api.repository.AccountRepository;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
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

    public AccountResponseDto create(@NotNull @NotBlank AccountRequestDto request) {

        var entity = accountMapper.requestToEntity(request);

        accountRepository.save(entity);

        return accountMapper.entityToResponse(entity);
    }

    public void update(@NotNull @NotBlank UUID uuid, @NotNull @NotBlank AccountRequestDto newUSer) {

        var exists = accountRepository.findByUUID(UUID.fromString(String.valueOf(uuid)));

        if (exists.getUuid() != null) {
            
        }

    }
}
