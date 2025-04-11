package com.investing.api.controller;

import com.investing.api.entity.dto.AccountRequestDto;
import com.investing.api.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AccountService accountService;

    public AdminController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> updateAccountAccessToMedium(String accountId) {
        accountService.udpateAccountAccessToMedium(accountId);
        return ResponseEntity.status(204).build();
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> updateAccountAccessToHigh(String accountId) {
        accountService.udpateAccountAccessToMedium(accountId);
        return ResponseEntity.status(204).build();
    }

}
