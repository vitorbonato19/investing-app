package com.investing.api.mapper;

import com.investing.api.entity.Account;
import com.investing.api.entity.dto.AccountRequestDto;
import com.investing.api.entity.dto.AccountResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR, uses = StockMapper.class)
public interface AccountMapper {

    @Mapping(target = "uuid", source ="account_id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "equity", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "perms", ignore = true)
    Account responseToEntity(AccountResponseDto response);

    @Mapping(target = "account_id", source = "uuid")
    List<AccountResponseDto> entityToResponse(List<Account> entity);

    @Mapping(target = "account_id", source = "uuid")
    AccountResponseDto entityToResponse(Account entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "stocks", ignore = true)
    @Mapping(target = "perms", ignore = true)
    Account requestToEntity(AccountRequestDto request);
}
