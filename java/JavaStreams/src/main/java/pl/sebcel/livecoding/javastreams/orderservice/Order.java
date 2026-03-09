package pl.sebcel.livecoding.javastreams.orderservice;

import java.math.BigDecimal;

public record Order(String customer, BigDecimal amount, String currency) {
}
