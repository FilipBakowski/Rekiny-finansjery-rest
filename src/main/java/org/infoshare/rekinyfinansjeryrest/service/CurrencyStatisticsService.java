package org.infoshare.rekinyfinansjeryrest.service;

import org.infoshare.rekinyfinansjeryrest.dto.CurrencyStatisticsDTO;
import org.infoshare.rekinyfinansjeryrest.entity.CurrencyStatistics;
import org.infoshare.rekinyfinansjeryrest.repository.CurrencyStatisticsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CurrencyStatisticsService {
    private final static int RECENT_DAYS_WINDOW_SIZE = 30;

    private CurrencyStatisticsRepository currencyStatisticsRepository;

    private ModelMapper modelMapper;

    public CurrencyStatisticsService(CurrencyStatisticsRepository currencyStatisticsRepository, ModelMapper modelMapper) {
        this.currencyStatisticsRepository = currencyStatisticsRepository;
        this.modelMapper = modelMapper;
    }

    public List<CurrencyStatisticsDTO> getAllCurrencyStatistics(){
        List<CurrencyStatistics> currencyStatistics = currencyStatisticsRepository.findAll();
        return mapCurrencyStatisticsListToDTO(currencyStatistics);
    }

    public List<CurrencyStatisticsDTO> getRecentCurrencyStatistics(){
        LocalDate beginning = LocalDate.now().minusDays(RECENT_DAYS_WINDOW_SIZE);
        LocalDate now = LocalDate.now();
        List<CurrencyStatistics> currencyStatistics = currencyStatisticsRepository
                .findCurrencyStatisticsByDateBetween(beginning, now);
        return mapCurrencyStatisticsListToDTO(currencyStatistics);
    }

    public List<CurrencyStatisticsDTO> getRecentCurrencyStatisticsForOneCurrency(String code){
        LocalDate beginning = LocalDate.now().minusDays(RECENT_DAYS_WINDOW_SIZE);
        LocalDate now = LocalDate.now();
        List<CurrencyStatistics> currencyStatistics = currencyStatisticsRepository
                .findCurrencyStatisticsByCodeAndDateBetween(code, beginning, now);
        return mapCurrencyStatisticsListToDTO(currencyStatistics);
    }

    public List<CurrencyStatisticsDTO> getCurrencyStatisticsFromSelectedMonth(int month, int year){
        LocalDate beginning = LocalDate.of(year, month, 1);
        LocalDate end = beginning.withDayOfMonth(beginning.getMonth().length(beginning.isLeapYear()));
        List<CurrencyStatistics> currencyStatistics = currencyStatisticsRepository
                .findCurrencyStatisticsByDateBetween(beginning, end);
        return mapCurrencyStatisticsListToDTO(currencyStatistics);
    }

    public List<CurrencyStatisticsDTO> getRecentCurrencyStatisticsForOneCurrencyFromSelectedMonth(
            String code, int month, int year){
        LocalDate beginning = LocalDate.of(year, month, 1);
        LocalDate end = beginning.withDayOfMonth(beginning.getMonth().length(beginning.isLeapYear()));
        List<CurrencyStatistics> currencyStatistics = currencyStatisticsRepository
                .findCurrencyStatisticsByCodeAndDateBetween(code, beginning, end);
        return mapCurrencyStatisticsListToDTO(currencyStatistics);
    }

    @Transactional
    public List<CurrencyStatisticsDTO> incrementCurrencyCounters(List<String> codes){
        List<CurrencyStatistics> currencyStatistics = currencyStatisticsRepository.findCurrencyStatisticsByCodeInAndDate(codes, LocalDate.now());
        Map<String, CurrencyStatistics> currencies = currencyStatistics.stream().collect(Collectors.toMap(CurrencyStatistics::getCode, Function.identity()));
        List<CurrencyStatistics> newCurrencyStatistics = new ArrayList<>();
        List<String> existingCodes = currencyStatistics.stream().map(stat -> stat.getCode()).toList();
        codes.stream().distinct().forEach(code -> {
            if(existingCodes.contains(code)){
                CurrencyStatistics incrementedCurrency = currencies.get(code);
                incrementedCurrency.setCounter(incrementedCurrency.getCounter()+1);
                newCurrencyStatistics.add(incrementedCurrency);
            }
            else{
                CurrencyStatistics newCurrencyStatistic = new CurrencyStatistics(code, LocalDate.now(), 1L);
                newCurrencyStatistics.add(newCurrencyStatistic);
                currencyStatistics.add(newCurrencyStatistic);
            }
        });
        currencyStatisticsRepository.saveAll(newCurrencyStatistics);
        return mapCurrencyStatisticsListToDTO(currencyStatistics);
    }

    private List<CurrencyStatisticsDTO> mapCurrencyStatisticsListToDTO(List<CurrencyStatistics> currencyStatistics){
        return currencyStatistics
                .stream()
                .map(currencyStatistic -> modelMapper.map(currencyStatistic, CurrencyStatisticsDTO.class))
                .toList();
    }
}
