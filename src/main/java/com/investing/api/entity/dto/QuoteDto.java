package com.investing.api.entity.dto;

public record QuoteDto(String symbol,
				        String currency,
				        String shortName,
				        Double regularMarketPrice) {

}
