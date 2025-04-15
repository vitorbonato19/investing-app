package com.investing.api.service;

import com.investing.api.entity.Stock;
import com.investing.api.entity.dto.BrapiQuoteDto;
import com.investing.api.entity.dto.StockRequestDto;
import com.investing.api.entity.dto.StockResponseDto;
import com.investing.api.feign.BrapiExternalApi;
import com.investing.api.mapper.StockMapper;
import com.investing.api.repository.AccountRepository;
import com.investing.api.repository.StockRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class StockService {

    @Value("${api.key}")
    private String BRAPI_TOKEN;

    private final BrapiExternalApi brapi;

    private final StockRepository stockRepository;

    private final AccountService accountService;

    private final StockMapper stockMapper;

    public StockService(BrapiExternalApi brapi, StockRepository stockRepository, AccountService accountService, StockMapper stockMapper) {
        this.brapi = brapi;
        this.stockRepository = stockRepository;
        this.accountService = accountService;
        this.stockMapper = stockMapper;
    }


    public StockResponseDto create(StockRequestDto request) {

        Long accountId = accountService.findByUuid(request.account()).getId();

        Stock stock = stockRepository.findByTicker(
                accountId
                ,request.ticker());

        if (stock != null) {
            stock.setQuantity(request.quantity());
            stock.setAccount(accountService.findByUuid(request.account()));
            stock.setRegularMarketPrice(BigDecimal.valueOf(getRegularMarketPrice(request.ticker())));
            stockRepository.save(stock);
            return stockMapper.entityToResponse(stock);

        } else {
            Stock newStock = new Stock();
            newStock.setTicker(request.ticker());
            newStock.setCurrency(getActualCurrency(request.ticker()));
            newStock.setShortName(getAtualShortName(request.ticker()));
            newStock.setLongName(getActualLongName(request.ticker()));
            newStock.setQuantity(request.quantity());
            newStock.setAccount(accountService.findByUuid(request.account()));
            newStock.setRegularMarketPrice(BigDecimal.valueOf(getRegularMarketPrice(request.ticker())));
            stockRepository.save(newStock);
            return stockMapper.entityToResponse(newStock);
        }
    }

    public Double getRegularMarketPrice(String ticker) {

         var data = brapi.quote(BRAPI_TOKEN, ticker);

         return data.results().getFirst().regularMarketPrice();
    }

    public String getActualCurrency(String ticker) {

        var data = brapi.quote(BRAPI_TOKEN, ticker);

        return data.results().getFirst().currency();
    }

    public String getAtualShortName(String ticker) {

        var data = brapi.quote(BRAPI_TOKEN, ticker);

        return data.results().getFirst().shortName();
    }


    public String getActualLongName(String ticker) {

        var data = brapi.quote(BRAPI_TOKEN, ticker);

        return data.results().getFirst().longName();
    }
}
