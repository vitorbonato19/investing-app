package com.investing.api.entity.dto;

import java.math.BigDecimal;

public record StockResponseDto(String ticker,
                               String currency,
                               String shortName,
                               BigDecimal regularMarketPrice) {
}
