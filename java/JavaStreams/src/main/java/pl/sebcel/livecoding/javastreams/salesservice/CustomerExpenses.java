package pl.sebcel.livecoding.javastreams.salesservice;

import java.math.BigDecimal;

public record CustomerExpenses(String customerId, BigDecimal sumOfExpenses) {

}
