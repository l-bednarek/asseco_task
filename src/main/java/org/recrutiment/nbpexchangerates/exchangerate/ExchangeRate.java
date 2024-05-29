package org.recrutiment.nbpexchangerates.exchangerate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String currency;
    private String code;
    private LocalDate date;
    private BigDecimal midRate;
    private BigDecimal askRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getMidRate() {
        return midRate;
    }

    public void setMidRate(BigDecimal midRate) {
        this.midRate = midRate;
    }

    public BigDecimal getAskRate() {
        return askRate;
    }

    public void setAskRate(BigDecimal askRate) {
        this.askRate = askRate;
    }
}
