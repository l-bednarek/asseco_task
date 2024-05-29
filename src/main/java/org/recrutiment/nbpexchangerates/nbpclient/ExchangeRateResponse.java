package org.recrutiment.nbpexchangerates.nbpclient;

import java.math.BigDecimal;
import java.util.List;

public class ExchangeRateResponse {

    private String table;
    private String currency;
    private String code;
    private List<Rate> rates;

    public String getCurrency() {
        return currency;
    }

    public String getCode() {
        return code;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public static class Rate {
        private String no;
        private String effectiveDate;
        private BigDecimal bid;
        private BigDecimal ask;

        public BigDecimal getAsk() {
            return ask;
        }

    }
}