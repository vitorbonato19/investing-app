package com.investing.api.entity.dto;

import com.investing.api.entity.Stock;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record AccountResponseDto(UUID uuid,
                               String name,
                               String email,
                               BigDecimal equity,
                               List<Stock> stocks) {
}
