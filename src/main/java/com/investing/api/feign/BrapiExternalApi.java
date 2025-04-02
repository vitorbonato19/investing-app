package com.investing.api.feign;

import com.investing.api.entity.dto.BrapiQuoteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="BrapiAPI", url = "https://brapi.dev/api")
public interface BrapiExternalApi {

    @GetMapping("/quote/{ticker}")
    public BrapiQuoteDto quote(@RequestParam("token") String token, @PathVariable String ticker);
}
