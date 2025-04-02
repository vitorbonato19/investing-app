package com.investing.api.entity.dto;

import java.math.BigDecimal;
import java.util.List;

public record BrapiQuoteDto(List<QuoteDto> results) {
}
