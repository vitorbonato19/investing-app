package com.investing.api.service;

import com.investing.api.entity.Access;
import com.investing.api.entity.Account;
import com.investing.api.entity.dto.LoginRequestDto;
import com.investing.api.entity.dto.LoginResponseDto;
import com.investing.api.exceptions.BadLoginRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AccountService accountService;

    private final JwtEncoder jwtEncoder;

    public AuthService(AccountService accountService, JwtEncoder jwtEncoder) {
        this.accountService = accountService;
        this.jwtEncoder = jwtEncoder;
    }

    public LoginResponseDto login(LoginRequestDto request) {

        Account entity = accountService.findByDocument(request.document());

        if (!accountService.verifyAccountLoginCredentials(request)) {
            throw new BadLoginRequestException("Document or password do not matches, please verify your request.", HttpStatus.BAD_REQUEST);
        }

        var perms = entity.getPerms().stream().map(Access::getName).collect(Collectors.joining(" "));

        JwtClaimsSet claims =  JwtClaimsSet
                .builder()
                .issuedAt(Instant.now())
                .issuer("investing-app")
                .subject(entity.getId().toString())
                .expiresAt(Instant.now().plusSeconds(150L))
                .claim("perms", perms)
                .build();

        var jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponseDto(entity.getDocument(), jwt);
    }
}
