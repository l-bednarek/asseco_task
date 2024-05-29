package org.recrutiment.nbpexchangerates.exchangerate;

import org.recrutiment.nbpexchangerates.nbpclient.ExchangeRateMapper;
import org.recrutiment.nbpexchangerates.nbpclient.ExchangeRateResponse;
import org.recrutiment.nbpexchangerates.nbpclient.ExchangeRateTableResponse;
import org.recrutiment.nbpexchangerates.nbpclient.NbpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class ExchangeRateService {

    private final NbpClient nbpClient;
    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateMapper exchangeRateMapper;

    @Autowired
    public ExchangeRateService(NbpClient nbpClient, ExchangeRateRepository exchangeRateRepository, ExchangeRateMapper exchangeRateMapper) {
        this.nbpClient = nbpClient;
        this.exchangeRateRepository = exchangeRateRepository;
        this.exchangeRateMapper = exchangeRateMapper;
    }

    public ExchangeRate getSellExchangeRate(String code, LocalDate date) {
        ExchangeRate rate = exchangeRateRepository.findByCodeAndDate(code, date);
        if (rate != null && rate.getAskRate() == null) {
            rate.setAskRate(fetchSingleExchangeRate(code, date).getAskRate());
            return exchangeRateRepository.save(rate);
        }
        else if (rate == null)
        {
            return exchangeRateRepository.save(fetchSingleExchangeRate(code, date));
        }
        return rate;
    }

    private ExchangeRate fetchSingleExchangeRate(String code, LocalDate date) {
        ExchangeRateResponse response = nbpClient.getSingleExchangeRate(code, date.toString());
        return exchangeRateMapper.toExchangeRate(response, date);


    }

    private List<ExchangeRate> fetchExchangeRates(LocalDate date) {
        ExchangeRateTableResponse response = nbpClient.getExchangeRateTable(date.toString());
        return exchangeRateMapper.toExchangeRates(response);
    }



    public BigDecimal calculateTotalCostInPLN(List<String> currencyCodes, LocalDate date) {
        List<ExchangeRate> rates = exchangeRateRepository.findByDateAndCodeIn(date, currencyCodes);

        Map<String, ExchangeRate> rateMap = rates.stream()
                .collect(Collectors.toMap(ExchangeRate::getCode, Function.identity()));

        List<String> missingOrIncompleteCodes = currencyCodes.stream()
                .filter(code -> {
                    ExchangeRate rate = rateMap.get(code);
                    return rate == null || rate.getMidRate() == null;
                })
                .toList();

        if (!missingOrIncompleteCodes.isEmpty()) {
            List<ExchangeRate> fetchedRates = fetchExchangeRates(date);
            for (ExchangeRate fetchedRate : fetchedRates) {
                ExchangeRate existingRate = rateMap.get(fetchedRate.getCode());
                if (existingRate != null) {
                    fetchedRate.setId(existingRate.getId());
                }
            }
            exchangeRateRepository.saveAll(fetchedRates);

            rates.addAll(fetchedRates);
        }

        BigDecimal totalCostInPLN = BigDecimal.ZERO;
        for (String code : currencyCodes) {
            ExchangeRate rate = rateMap.get(code);
            if (rate != null && rate.getMidRate() != null) {
                totalCostInPLN = totalCostInPLN.add(rate.getMidRate());
            }
        }
        return totalCostInPLN;
    }
}
