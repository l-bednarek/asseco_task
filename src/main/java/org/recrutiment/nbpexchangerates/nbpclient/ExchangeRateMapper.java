package org.recrutiment.nbpexchangerates.nbpclient;

import org.recrutiment.nbpexchangerates.exchangerate.ExchangeRate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ExchangeRateMapper {

    public ExchangeRate toExchangeRate(ExchangeRateResponse response, LocalDate date) {
        ExchangeRateResponse.Rate rateInfo = response.getRates().getFirst();
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setCurrency(response.getCurrency());
        exchangeRate.setCode(response.getCode());
        exchangeRate.setDate(date);
        exchangeRate.setAskRate(rateInfo.getAsk());
        return exchangeRate;
    }

    public List<ExchangeRate> toExchangeRates(ExchangeRateTableResponse response) {
        return response.getRates().stream()
                .map(rate -> {
                    ExchangeRate exchangeRate = new ExchangeRate();
                    exchangeRate.setCurrency(rate.getCurrency());
                    exchangeRate.setCode(rate.getCode());
                    exchangeRate.setMidRate(rate.getMid());
                    exchangeRate.setDate(LocalDate.parse(response.getEffectiveDate()));
                    return exchangeRate;
                })
                .toList();
    }
}