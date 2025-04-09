package com.investing.api.service;

import com.investing.api.entity.Access;
import com.investing.api.repository.AccessRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class AccessService {

    private final AccessRepository accessRepository;

    public AccessService(AccessRepository accessRepository) {
        this.accessRepository = accessRepository;
    }

    @PostConstruct
    public void initAccess() {
        if (accessRepository.countData() == 0) {
            accessRepository.save(new Access(null, "ADMIN"));
            accessRepository.save(new Access(null, "HIGH"));
            accessRepository.save(new Access(null, "MEDIUM"));
            accessRepository.save(new Access(null, "BASIC"));
        }
    }
}
