package org.recrutiment.nbpexchangerates.exchangerate;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    ExchangeRate findByCodeAndDate(String code, LocalDate date);
    List<ExchangeRate> findByDateAndCodeIn(LocalDate date, List<String> codes);

}