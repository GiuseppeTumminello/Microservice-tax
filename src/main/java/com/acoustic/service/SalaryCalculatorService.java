package com.acoustic.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public interface SalaryCalculatorService {

    String getDescription();

    BigDecimal apply(BigDecimal grossMonthlySalary, BigDecimal grossMonthlyMinusZusAndHealth);


}
