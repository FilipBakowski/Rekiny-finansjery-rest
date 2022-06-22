package org.infoshare.rekinyfinansjeryrest.service;

import org.infoshare.rekinyfinansjeryrest.dto.CurrencyStatisticsDTO;
import org.infoshare.rekinyfinansjeryrest.dto.SearchedCurrenciesListDTO;
import org.infoshare.rekinyfinansjeryrest.entity.CurrencyStatistics;
import org.infoshare.rekinyfinansjeryrest.repository.CurrencyStatisticsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
    public boolean incrementCurrencyCounters(List<String> codes){
        try {
            codes.forEach(code -> incrementCurrencyCounter(code));
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    private void incrementCurrencyCounter(String code){
        Optional<CurrencyStatistics> currencyStatisticsOptional =
                currencyStatisticsRepository.findCurrencyStatisticsByCodeAndDate(code, LocalDate.now());
        if(currencyStatisticsOptional.isEmpty()){
            CurrencyStatistics newCurrencyStatistic = new CurrencyStatistics(code, LocalDate.now(), 1L);
            currencyStatisticsRepository.save(newCurrencyStatistic);
        }
        else{
            CurrencyStatistics currencyStatistics = currencyStatisticsOptional.get();
            currencyStatistics.setCounter(currencyStatistics.getCounter()+1);
            currencyStatisticsRepository.save(currencyStatistics);

        }
    }

    private List<CurrencyStatisticsDTO> mapCurrencyStatisticsListToDTO(List<CurrencyStatistics> currencyStatistics){
        return currencyStatistics
                .stream()
                .map(currencyStatistic -> modelMapper.map(currencyStatistic, CurrencyStatisticsDTO.class))
                .toList();
    }
}
