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
    public BigDecimal apply(BigDecimal grossMonthlySalary) {
       return (grossMonthlySalary.multiply(BigDecimal.valueOf(MONTHS_NUMBER))
                .compareTo(this.rate.getTaxGrossAmountThreshold()) < 0)
                ? getTaxAmountBasedOnRate(
               grossMonthlySalary,
                this.rate.getTaxRate17Rate())
                : getTaxAmountBasedOnRate(grossMonthlySalary, this.rate.getTaxRate32Rate());
   }

    @Override
    public String getDescription() {
        return "Tax";
    }

    private BigDecimal calculateTotalZus(BigDecimal grossMonthlySalary) {
        return grossMonthlySalary.subtract(grossMonthlySalary.multiply(this.rate.getTotalZusRate()));
    }

    private BigDecimal calculateHealth(BigDecimal grossMonthlySalary) {
        return grossMonthlySalary.subtract(grossMonthlySalary.multiply(this.rate.getHealthRate()));
    }





    private BigDecimal getTaxAmountBasedOnRate(BigDecimal grossMonthlySalary, BigDecimal rate) {
        var salaryMinusTotalZus = calculateTotalZus(grossMonthlySalary);
        var salaryMinusHealth = calculateHealth(salaryMinusTotalZus);
        return salaryMinusHealth.multiply(rate).setScale(2, RoundingMode.HALF_EVEN);

    }
}
