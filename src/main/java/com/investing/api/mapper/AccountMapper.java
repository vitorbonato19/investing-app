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
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "document", ignore = true)
    @Mapping(target = "password", ignore = true)
    Account responseToEntity(AccountResponseDto response);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "document", ignore = true)
    @Mapping(target = "password", ignore = true)
    List<AccountResponseDto> entityToResponse(List<Account> entity);


    AccountResponseDto entityToResponse(Account entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "stocks", ignore = true)
    Account requestToEntity(AccountRequestDto request);
}
