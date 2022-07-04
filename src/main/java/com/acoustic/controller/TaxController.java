package com.acoustic.controller;


import com.acoustic.entity.Tax;
import com.acoustic.repository.TaxRepository;
import com.acoustic.service.SalaryCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tax")
public class TaxController {

    public static final String TOTAL_ZUS_DESCRIPTION = "Total zus";
    public static final String HEALTH_DESCRIPTION = "Health";
    private static final String TOTAL_ZUS_ENDPOINT = "http://TOTAL-ZUS/totalZus/getTotalZus/";
    private static final String HEALTH_ENDPOINT = "http://HEALTH-TAX/health/getHealth/";
    private final TaxRepository taxRepository;
    private final SalaryCalculatorService salaryCalculatorService;
    private final RestTemplate restTemplate;


    @PostMapping("/getTax/{grossMonthlySalary}")
    public Map<String, BigDecimal> calculateHealth(@PathVariable @Min(2000) BigDecimal grossMonthlySalary) {
        var tax = this.salaryCalculatorService.apply(grossMonthlySalary, grossMonthlySalary.subtract((getTotalZus(grossMonthlySalary).add(getHealth(grossMonthlySalary)))));
        this.taxRepository.save(Tax.builder().taxAmount(tax).build());
        return new LinkedHashMap<>(Map.of(this.salaryCalculatorService.getDescription(), tax));
    }


    public BigDecimal getTotalZus(BigDecimal grossMonthlySalary) {
        var totalZus = Objects.requireNonNull(this.restTemplate.postForEntity(TOTAL_ZUS_ENDPOINT + grossMonthlySalary, HttpMethod.POST, HashMap.class).getBody()).get(TOTAL_ZUS_DESCRIPTION);
        return BigDecimal.valueOf(((Double) (totalZus)));
    }

    public BigDecimal getHealth(BigDecimal grossMonthlySalary) {
        var health = Objects.requireNonNull(this.restTemplate.postForEntity(HEALTH_ENDPOINT + grossMonthlySalary, HttpMethod.POST, HashMap.class).getBody()).get(HEALTH_DESCRIPTION);
        return BigDecimal.valueOf(((Double) (health)));
    }
}
