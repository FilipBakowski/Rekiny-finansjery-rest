package org.infoshare.rekinyfinansjeryrest.repository;

import org.infoshare.rekinyfinansjeryrest.entity.CurrencyStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CurrencyStatisticsRepository extends JpaRepository<CurrencyStatistics, UUID> {
    List<CurrencyStatistics> findCurrencyStatisticsByCodeInAndDate(List<String> codes, LocalDate date);
    List<CurrencyStatistics> findCurrencyStatisticsByDateBetween(LocalDate start, LocalDate end);
    List<CurrencyStatistics> findCurrencyStatisticsByCodeAndDateBetween(String code, LocalDate start, LocalDate end);
}
