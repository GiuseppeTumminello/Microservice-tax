package com.acoustic.service;

import com.acoustic.rate.RatesConfigurationProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class TaxServiceTest {

    @InjectMocks
    private TaxService taxService;

    @Mock
    private RatesConfigurationProperties ratesConfigurationProperties;

    public static final int TAX_GROSS_AMOUNT_THRESHOLD = 120000;

    @Test
    void getDescription() {
        assertThat(this.taxService.getDescription()).isEqualTo("Tax");
    }

    @ParameterizedTest
    @CsvSource({"6000, 4711.43, 391.99, 0.0832", "6040.30, 5496.70,457.33 , 0.0832"})
    public void getTaxAmountBasedOnRate17(BigDecimal input, BigDecimal grossMonthlyMinusZusAndHealth, BigDecimal expected, BigDecimal rate) {
        given(this.ratesConfigurationProperties.getTaxRate17Rate()).willReturn(rate);
        given(ratesConfigurationProperties.getTaxGrossAmountThreshold()).willReturn(BigDecimal.valueOf(
                TAX_GROSS_AMOUNT_THRESHOLD));
        assertThat(this.taxService.apply(input, grossMonthlyMinusZusAndHealth)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"15982.33, 12549.95, 1797.15, 0.1432", "20999.20, 16489.39,2361.28 , 0.1432"})
    public void getTaxAmountBasedOnRate32(BigDecimal input, BigDecimal grossMonthlyMinusZusAndHealth, BigDecimal expected, BigDecimal rate) {
        given(this.ratesConfigurationProperties.getTaxRate32Rate()).willReturn(rate);
        given(ratesConfigurationProperties.getTaxGrossAmountThreshold()).willReturn(BigDecimal.valueOf(
                TAX_GROSS_AMOUNT_THRESHOLD));
        assertThat(this.taxService.apply(input, grossMonthlyMinusZusAndHealth)).isEqualTo(expected);
    }
}