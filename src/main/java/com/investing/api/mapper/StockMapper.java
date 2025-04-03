package com.investing.api.mapper;

import com.investing.api.entity.Stock;
import com.investing.api.entity.dto.StockRequestDto;
import com.investing.api.entity.dto.StockResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface StockMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "longName", ignore = true)
    @Mapping(target = "quantity", ignore = true)
    @Mapping(target = "account", ignore = true)
    Stock responseToEntity(StockResponseDto response);

    StockResponseDto entityToResponse(Stock stock);

    List<StockResponseDto> entityToResponseList(List<Stock> stocks);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "longName", ignore = true)
    @Mapping(target = "shortName", ignore = true)
    @Mapping(target = "regularMarketPrice", ignore = true)
    @Mapping(target = "currency", ignore = true)
    Stock requestToEntity(StockRequestDto request);
}
