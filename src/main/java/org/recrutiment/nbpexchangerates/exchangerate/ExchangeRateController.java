package org.recrutiment.nbpexchangerates.exchangerate;

import org.recrutiment.nbpexchangerates.exceptions.ExchangeRateNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/exchange-rates")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/sell")
    public ExchangeRate getSellExchangeRate(@RequestParam String code, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return exchangeRateService.getSellExchangeRate(code, date);
    }

    @GetMapping("/total-cost")
    public BigDecimal calculateTotalCostInPLN(@RequestParam List<String> currencyCodes, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return exchangeRateService.calculateTotalCostInPLN(currencyCodes, date);
    }

    @ExceptionHandler(ExchangeRateNotFoundException.class)
    public ResponseEntity<String> handleExchangeRateNotFoundException(ExchangeRateNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
