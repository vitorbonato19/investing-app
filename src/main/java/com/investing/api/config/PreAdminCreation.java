package com.investing.api.config;

import com.investing.api.repository.AccountRepository;
import com.investing.api.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PreAdminCreation implements CommandLineRunner {

    private final AccountService accountService;

    public PreAdminCreation(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void run(String... args) throws Exception {

        var admin = accountService.find




    }
}
