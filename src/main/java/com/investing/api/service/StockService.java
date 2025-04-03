package com.investing.api.service;

import com.investing.api.entity.Stock;
import com.investing.api.entity.dto.StockRequestDto;
import com.investing.api.entity.dto.StockResponseDto;
import com.investing.api.mapper.StockMapper;
import com.investing.api.repository.AccountRepository;
import com.investing.api.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class StockService {

    private final StockRepository stockRepository;

    private final AccountRepository accountRepository;

    private final StockMapper stockMapper;

    public StockService(StockRepository stockRepository, AccountRepository accountRepository, StockMapper stockMapper) {
        this.stockRepository = stockRepository;
        this.accountRepository = accountRepository;
        this.stockMapper = stockMapper;
    }

    public StockResponseDto create(StockRequestDto request) {

        var accountId = accountRepository.findByUUID(UUID.fromString(request.account_id())).getId();

        Stock stock = stockRepository.findByTicker(accountId ,request.ticker());

        if (stock != null) {
            stock.setQuantity(request.quantity());
            stock.setRegularMarketPrice(stock.getRegularMarketPrice().multiply(BigDecimal.valueOf(request.quantity())));
            stockRepository.save(stock);
            return stockMapper.entityToResponse(stock);
        } else {
             var entity = stockRepository.save(stockMapper.requestToEntity(request));
             return stockMapper.entityToResponse(entity);
        }
    }
}
