package com.investing.api.controller;

import com.investing.api.entity.dto.AccountRequestDto;
import com.investing.api.entity.dto.AccountResponseDto;
import com.investing.api.entity.dto.BrapiQuoteDto;
import com.investing.api.entity.dto.StockAccountResponseDto;
import com.investing.api.service.AccountService;
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

    @PatchMapping("/email")
    public ResponseEntity<Void> updateEmail(@RequestParam String uuid, @RequestParam("email") String email) {
        accountService.updateEmailByUuid(uuid, email);
        return ResponseEntity.status(204).build();
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(@RequestParam String uuid, @RequestParam("password") String password) {
        accountService.updatePasswordByUUID(uuid, password);
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable String uuid) {
        accountService.deleteByUuid(uuid);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/{ticker}")
    public ResponseEntity<StockAccountResponseDto> getStocksInfo(@PathVariable("ticker") String ticker, @RequestParam String id) {
	        return ResponseEntity.status(200).body(accountService.getStockInfoByTicker(id, ticker));
    }
}
