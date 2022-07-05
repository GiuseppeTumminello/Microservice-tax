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

    public static final String TAX_DESCRIPTION = "Tax";
    public static final double TOTAL_ZUS_RATE = 0.1371;
    public static final double HEALTH_RATE = 0.09;

    public static final int TAX_GROSS_AMOUNT_THRESHOLD = 120000;
    @InjectMocks
    private TaxService taxService;

    @Mock
    private RatesConfigurationProperties ratesConfigurationProperties;


    @Test
    void getDescription() {
        assertThat(this.taxService.getDescription()).isEqualTo(TAX_DESCRIPTION);
    }

    @ParameterizedTest
    @CsvSource({"6000,  391.99, 0.0832", "7000, 457.32 , 0.0832"})
    public void getTaxAmountBasedOnRate17(BigDecimal input, BigDecimal expected, BigDecimal rate) {
        given(this.ratesConfigurationProperties.getTaxRate17Rate()).willReturn(rate);
        given(this.ratesConfigurationProperties.getTotalZusRate()).willReturn(BigDecimal.valueOf(TOTAL_ZUS_RATE));
        given(this.ratesConfigurationProperties.getHealthRate()).willReturn(BigDecimal.valueOf(HEALTH_RATE));
        given(this.ratesConfigurationProperties.getTaxGrossAmountThreshold()).willReturn(BigDecimal.valueOf(TAX_GROSS_AMOUNT_THRESHOLD));
        assertThat(this.taxService.apply(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"15982.33, 1797.15, 0.1432", "20999.20,2361.28, 0.1432"})
    public void getTaxAmountBasedOnRate32(BigDecimal input, BigDecimal expected, BigDecimal rate) {
        given(this.ratesConfigurationProperties.getTaxRate32Rate()).willReturn(rate);
        given(this.ratesConfigurationProperties.getTotalZusRate()).willReturn(BigDecimal.valueOf(TOTAL_ZUS_RATE));
        given(this.ratesConfigurationProperties.getHealthRate()).willReturn(BigDecimal.valueOf(HEALTH_RATE));
        given(this.ratesConfigurationProperties.getTaxGrossAmountThreshold()).willReturn(BigDecimal.valueOf(TAX_GROSS_AMOUNT_THRESHOLD));
        assertThat(this.taxService.apply(input)).isEqualTo(expected);
    }
}