package pl.sebcel.livecoding.anf;

import java.math.BigDecimal;

public record Order(String customer, BigDecimal amount, String currency) {
}
