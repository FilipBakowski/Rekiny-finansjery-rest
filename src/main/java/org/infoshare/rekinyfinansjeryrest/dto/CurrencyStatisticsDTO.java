package org.infoshare.rekinyfinansjeryrest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyStatisticsDTO {
    String code;
    LocalDate date;
    Long counter;
}
