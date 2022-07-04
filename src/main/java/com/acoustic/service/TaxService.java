package com.acoustic.service;

import com.acoustic.rate.RatesConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
public class TaxService implements SalaryCalculatorService {

    public static final int MONTHS_NUMBER = 12;
    private final RatesConfigurationProperties rate;


    @Override
    public BigDecimal apply(BigDecimal grossMonthlySalary, BigDecimal grossMonthlyMinusZusAndHealth) {
        return (grossMonthlySalary.multiply(BigDecimal.valueOf(MONTHS_NUMBER))
                .compareTo(this.rate.getTaxGrossAmountThreshold()) < 0)
                ? getTaxAmountBasedOnRate(
                grossMonthlyMinusZusAndHealth,
                this.rate.getTaxRate17Rate())
                : getTaxAmountBasedOnRate(grossMonthlyMinusZusAndHealth, this.rate.getTaxRate32Rate());
    }


    @Override
    public String getDescription() {
        return "Tax";
    }


    private BigDecimal getTaxAmountBasedOnRate(BigDecimal grossMonthlyMinusZusAndHealth, BigDecimal rate) {
        return grossMonthlyMinusZusAndHealth.multiply(rate).setScale(2, RoundingMode.HALF_EVEN);

    }
}
