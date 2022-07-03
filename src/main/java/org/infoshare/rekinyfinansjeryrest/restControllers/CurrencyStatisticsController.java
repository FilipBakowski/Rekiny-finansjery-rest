package org.infoshare.rekinyfinansjeryrest.restControllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.infoshare.rekinyfinansjeryrest.dto.CurrencyStatisticsDTO;
import org.infoshare.rekinyfinansjeryrest.service.CurrencyStatisticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CurrencyStatisticsController {
    CurrencyStatisticsService currencyStatisticsService;

    public CurrencyStatisticsController(CurrencyStatisticsService currencyStatisticsService) {
        this.currencyStatisticsService = currencyStatisticsService;
    }

    @GetMapping("/all")
    public List<CurrencyStatisticsDTO> getAllCurrencyStatistics(){
        return currencyStatisticsService.getAllCurrencyStatistics();
    }

    @GetMapping()
    public List<CurrencyStatisticsDTO> getRecentCurrencyStatistics(){
        return currencyStatisticsService.getRecentCurrencyStatistics();
    }

    @GetMapping("/currency/{code}")
    public List<CurrencyStatisticsDTO> getRecentCurrencyStatisticsForOneCurrency(@PathVariable("code") String code){
        return currencyStatisticsService.getRecentCurrencyStatisticsForOneCurrency(code);
    }

    @GetMapping("/history/{month}/{year}")
    public List<CurrencyStatisticsDTO> getCurrencyStatisticsFromSelectedMonth(
            @PathVariable("month") int month, @PathVariable("year") int year){
        return currencyStatisticsService.getCurrencyStatisticsFromSelectedMonth(month, year);
    }

    @GetMapping("/history/{month}/{year}/{code}")
    public List<CurrencyStatisticsDTO> getCurrencyStatisticsFromSelectedMonthForCurrency(
            @PathVariable("month") int month, @PathVariable("year") int year, @PathVariable("code") String code){
        return currencyStatisticsService.getRecentCurrencyStatisticsForOneCurrencyFromSelectedMonth(code, month, year);
    }


    @PostMapping("/requested_currencies")
    public ResponseEntity<List<CurrencyStatisticsDTO>> incrementCurrencyCounters(@RequestBody List<String> searchedCurrenciesList){
        List<CurrencyStatisticsDTO> incrementedCurrencies = currencyStatisticsService.incrementCurrencyCounters(searchedCurrenciesList);
        return ResponseEntity.accepted().body(incrementedCurrencies);
    }

    @ControllerAdvice
    public class ControllerExceptionHandler{
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorCause> handle(Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorCause(e.getMessage()));
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public class ErrorCause {

        private String cause;

    }
}
