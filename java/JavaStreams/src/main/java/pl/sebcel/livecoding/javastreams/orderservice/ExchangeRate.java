package pl.sebcel.livecoding.javastreams.orderservice;

import java.math.BigDecimal;

public record ExchangeRate(String currency, BigDecimal rateToEUR) {

}
