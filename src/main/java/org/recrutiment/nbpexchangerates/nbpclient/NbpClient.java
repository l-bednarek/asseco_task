package org.recrutiment.nbpexchangerates.nbpclient;

import org.recrutiment.nbpexchangerates.exceptions.ExchangeRateNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class NbpClient {

    private final RestTemplate restTemplate;

    public NbpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ExchangeRateResponse getSingleExchangeRate(String code, String date) {
        String url = String.format("http://api.nbp.pl/api/exchangerates/rates/c/%s/%s/?format=json", code, date);
        ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);
        return Optional.ofNullable(response)
                .orElseThrow(() -> new ExchangeRateNotFoundException("Exchange rate not found for code: " + code + " on date: " + date));
    }

    public ExchangeRateTableResponse getExchangeRateTable(String date) {
        String url = String.format("http://api.nbp.pl/api/exchangerates/tables/a/%s/?format=json", date);
        ExchangeRateTableResponse[] responses = restTemplate.getForObject(url, ExchangeRateTableResponse[].class);
        return (responses != null && responses.length > 0 ? Optional.of(responses[0]) : Optional.<ExchangeRateTableResponse>empty())
                .orElseThrow(() -> new ExchangeRateNotFoundException("Exchange rate table not found for date: " + date));
    }
}
