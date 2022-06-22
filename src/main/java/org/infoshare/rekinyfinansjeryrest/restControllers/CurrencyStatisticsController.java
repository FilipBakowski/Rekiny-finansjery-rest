package org.infoshare.rekinyfinansjeryrest.restControllers;

import org.infoshare.rekinyfinansjeryrest.dto.CurrencyStatisticsDTO;
import org.infoshare.rekinyfinansjeryrest.dto.SearchedCurrenciesListDTO;
import org.infoshare.rekinyfinansjeryrest.service.CurrencyStatisticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CurrencyStatisticsController {
    CurrencyStatisticsService currencyStatisticsService;

    public CurrencyStatisticsController(CurrencyStatisticsService currencyStatisticsService) {
        this.currencyStatisticsService = currencyStatisticsService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CurrencyStatisticsDTO>> getAllCurrencyStatistics(){
        return new ResponseEntity<>(currencyStatisticsService.getAllCurrencyStatistics(), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<CurrencyStatisticsDTO>> getRecentCurrencyStatistics(){
        return new ResponseEntity<>(currencyStatisticsService.getRecentCurrencyStatistics(), HttpStatus.OK);
    }

    @GetMapping("/currency/{code}")
    public ResponseEntity<List<CurrencyStatisticsDTO>> getRecentCurrencyStatisticsForOneCurrency(@PathVariable("code") String code){
        return new ResponseEntity<>(currencyStatisticsService.getRecentCurrencyStatisticsForOneCurrency(code), HttpStatus.OK);
    }

    @GetMapping("/history/{month}/{year}")
    public ResponseEntity<List<CurrencyStatisticsDTO>> getCurrencyStatisticsFromSelectedMonth(
            @PathVariable("month") int month, @PathVariable("year") int year){
        return new ResponseEntity<>(currencyStatisticsService.getCurrencyStatisticsFromSelectedMonth(month, year),
                HttpStatus.OK);
    }

    @GetMapping("/history/{month}/{year}/{code}")
    public ResponseEntity<List<CurrencyStatisticsDTO>> getCurrencyStatisticsFromSelectedMonthForCurrency(
            @PathVariable("month") int month, @PathVariable("year") int year, @PathVariable("code") String code){
        return new ResponseEntity<>(currencyStatisticsService
                .getRecentCurrencyStatisticsForOneCurrencyFromSelectedMonth(code, month, year),
                HttpStatus.OK);
    }


    @PostMapping("/increment")
    @Transactional
    public ResponseEntity<Void> incrementCurrencyCounters(@RequestBody SearchedCurrenciesListDTO searchedCurrenciesList){
        if(currencyStatisticsService.incrementCurrencyCounters(searchedCurrenciesList.getSearchedCurrenciesCodes())){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
