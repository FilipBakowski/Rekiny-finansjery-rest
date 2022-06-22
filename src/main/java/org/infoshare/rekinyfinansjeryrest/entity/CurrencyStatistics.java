package org.infoshare.rekinyfinansjeryrest.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = CurrencyStatistics.TABLE_NAME)
public class CurrencyStatistics {
    public static final String TABLE_NAME = "currency_statistics";
    public static final String COLUMN_PREFIX = "c_";
    @Id
    @GeneratedValue
    @org.hibernate.annotations.Type(type = "uuid-char")
    @Column(name = COLUMN_PREFIX + "id")
    private UUID id;
    @NotNull
    @Column(name = COLUMN_PREFIX + "code")
    String code;
    @NotNull
    @Column(name = COLUMN_PREFIX + "date")
    LocalDate date;
    @NotNull
    @Column(name = COLUMN_PREFIX + "counter")
    Long counter;

    public CurrencyStatistics(String code, LocalDate date, Long counter) {
        this.code = code;
        this.date = date;
        this.counter = counter;
    }
}
