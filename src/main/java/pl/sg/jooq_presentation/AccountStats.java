package pl.sg.jooq_presentation;

import java.math.BigDecimal;

public record AccountStats(long id, BigDecimal balance, BigDecimal balanceFromApi, BigDecimal balanceFromUser) {
}
