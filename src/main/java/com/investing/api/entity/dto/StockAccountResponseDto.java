package com.investing.api.entity.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record StockAccountResponseDto(UUID account_id,
                                      String name,
                                      String ticker,
                                      String currency,
                                      String shortName,
                                      Long quantity,
                                      BigDecimal actualRegularMarketPrice,
                                      BigDecimal totalValue) {
}
