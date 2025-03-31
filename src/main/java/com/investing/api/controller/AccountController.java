package com.investing.api.controller;

import com.investing.api.entity.dto.AccountRequestDto;
import com.investing.api.entity.dto.AccountResponseDto;
import com.investing.api.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDto>> findAll() {
        return ResponseEntity.status(200).body(accountService.findAll());
    }

    @PostMapping
    public ResponseEntity<AccountResponseDto> create(@RequestBody @Valid AccountRequestDto request) {
        return ResponseEntity.status(201).body(accountService.create(request));
    }

    public ResponseEntity<Void> updateByEmail(@RequestParam String uuid, @RequestParam String email) {
        accountService.updateEmailByUuid(uuid, email);
        return ResponseEntity.status(204).build();
    }
}
