package com.investing.api.entity.dto;

public record StockRequestDto(String ticker,
                              String account,
                              Long quantity) {
}
