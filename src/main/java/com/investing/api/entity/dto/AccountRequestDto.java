package com.investing.api.entity.dto;

import java.math.BigDecimal;

public record AccountRequestDto(String name,
                                String document,
                                String password,
                                String email,
                                BigDecimal equity) {

}
