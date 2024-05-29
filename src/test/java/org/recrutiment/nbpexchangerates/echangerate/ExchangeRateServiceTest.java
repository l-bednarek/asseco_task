package org.recrutiment.nbpexchangerates.echangerate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.recrutiment.nbpexchangerates.exchangerate.ExchangeRate;
import org.recrutiment.nbpexchangerates.exchangerate.ExchangeRateRepository;
import org.recrutiment.nbpexchangerates.exchangerate.ExchangeRateService;
import org.recrutiment.nbpexchangerates.nbpclient.ExchangeRateMapper;
import org.recrutiment.nbpexchangerates.nbpclient.NbpClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ExchangeRateServiceTest {

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @Mock
    private NbpClient nbpClient;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private ExchangeRateMapper exchangeRateMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSellExchangeRate() {
        String code = "USD";
        LocalDate date = LocalDate.now();
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setCode(code);
        exchangeRate.setDate(date);
        exchangeRate.setAskRate(BigDecimal.valueOf(3.75));

        when(exchangeRateRepository.findByCodeAndDate(code, date)).thenReturn(exchangeRate);

        ExchangeRate result = exchangeRateService.getSellExchangeRate(code, date);

        assertEquals(exchangeRate, result);
    }

    @Test
    void testCalculateTotalCostInPLN() {
        List<String> currencyCodes = Arrays.asList("USD", "EUR");
        LocalDate date = LocalDate.now();
        ExchangeRate usdRate = new ExchangeRate();
        usdRate.setCode("USD");
        usdRate.setDate(date);
        usdRate.setMidRate(BigDecimal.valueOf(3.75));
        ExchangeRate eurRate = new ExchangeRate();
        eurRate.setCode("EUR");
        eurRate.setDate(date);
        eurRate.setMidRate(BigDecimal.valueOf(4.50));

        when(exchangeRateRepository.findByDateAndCodeIn(date, currencyCodes)).thenReturn(Arrays.asList(usdRate, eurRate));

        BigDecimal result = exchangeRateService.calculateTotalCostInPLN(currencyCodes, date);

        assertEquals(BigDecimal.valueOf(8.25), result);
    }
}
