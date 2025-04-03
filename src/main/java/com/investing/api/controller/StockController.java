package com.investing.api.controller;

import com.investing.api.entity.dto.StockRequestDto;
import com.investing.api.entity.dto.StockResponseDto;
import com.investing.api.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    public ResponseEntity<StockResponseDto> pay(@RequestBody StockRequestDto request) {
        return ResponseEntity.status(201).body(stockService.create(request));
    }
}
