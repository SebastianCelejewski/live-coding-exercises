package pl.sebcel.livecoding.javastreams.advancedorderservice;

import java.math.BigDecimal;
import java.time.Instant;

public record CustomerReport(String customerId, BigDecimal totalEUR, int ordersCount, Instant lastOrderTimestamp) {

}
