package pl.sebcel.livecoding.javastreams.advancedorderservice;

import java.math.BigDecimal;
import java.time.Instant;

public record Order(String customerId, String country, BigDecimal amount, String currency, Instant timestamp) {

}
