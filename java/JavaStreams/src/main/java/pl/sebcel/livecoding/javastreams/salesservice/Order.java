package pl.sebcel.livecoding.javastreams.salesservice;

import java.math.BigDecimal;

public record Order(String customerId, String country, BigDecimal amount) {

	public Order(String customerId, String country, String amount) {
		this(customerId, country, new BigDecimal(amount));
	}
}
