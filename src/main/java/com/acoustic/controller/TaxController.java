package com.acoustic.controller;


import com.acoustic.entity.Tax;
import com.acoustic.repository.TaxRepository;
import com.acoustic.service.SalaryCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tax")
public class TaxController {

    public static final String DESCRIPTION = "description";
    public static final String VALUE = "value";
    private final TaxRepository taxRepository;
    private final SalaryCalculatorService salaryCalculatorService;


    @PostMapping("/getTax/{grossMonthlySalary}")
    public Map<String, String> calculateTax(@PathVariable @Min(2000) BigDecimal grossMonthlySalary) {
        var tax = this.salaryCalculatorService.apply(grossMonthlySalary);
        this.taxRepository.save(Tax.builder().taxAmount(tax).build());
        return Map.of(DESCRIPTION,salaryCalculatorService.getDescription(), VALUE, String.valueOf(tax));
    }


}
