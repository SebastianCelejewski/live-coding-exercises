package pl.sebcel.livecoding.javastreams.advancedorderservice;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class GenerateCustomerIntelligenceReportTests {

	private final static List<Order> DEFAULT_ORDERS = generateOrders();
	private final static Map<String, BigDecimal> DEFAULT_EXCHANGE_RATES = generateExchangeRates();
	private final static int NO_TOPN = 999;
	private final static BigDecimal NO_MIN_TOTAL = new BigDecimal("0.0");
	private final static int NO_MIN_ORDERS_COUNT = 0;
	
	private AdvancedOrderService cut = new AdvancedOrderService();

	@Test
	public void should_throw_NullPointerException_if_list_of_orders_is_null() {
		assertThrows(NullPointerException.class, () -> cut.generateCustomerIntelligenceReport(null, DEFAULT_EXCHANGE_RATES, NO_TOPN, NO_MIN_TOTAL, NO_MIN_ORDERS_COUNT));
	}
	
	@Test
	public void should_throw_NullPointerException_if_map_of_exchange_rates_is_null() {
		assertThrows(NullPointerException.class, () -> cut.generateCustomerIntelligenceReport(DEFAULT_ORDERS, null, NO_TOPN, NO_MIN_TOTAL, NO_MIN_ORDERS_COUNT));
	}
	
	@Test
	public void should_throw_IllegalArgumentException_if_topN_is_negative() {
		assertThrows(IllegalArgumentException.class, () -> cut.generateCustomerIntelligenceReport(DEFAULT_ORDERS, DEFAULT_EXCHANGE_RATES, -5, NO_MIN_TOTAL, NO_MIN_ORDERS_COUNT));
	}

	@Test
	public void should_throw_NullPointerException_if_minTotalEUR_is_null() {
		assertThrows(NullPointerException.class, () -> cut.generateCustomerIntelligenceReport(DEFAULT_ORDERS, DEFAULT_EXCHANGE_RATES, NO_TOPN, null, NO_MIN_ORDERS_COUNT));
	}
		
	@Test
	public void should_throw_IllegalArgumentException_if_minTotalEUR_is_negative() {
		assertThrows(IllegalArgumentException.class, () -> cut.generateCustomerIntelligenceReport(DEFAULT_ORDERS, DEFAULT_EXCHANGE_RATES, NO_TOPN, new BigDecimal("-5.0"), NO_MIN_ORDERS_COUNT));
	}
	
	@Test
	public void should_throw_IllegalArgumentException_if_minOrdersCount_is_negative() {
		assertThrows(IllegalArgumentException.class, () -> cut.generateCustomerIntelligenceReport(DEFAULT_ORDERS, DEFAULT_EXCHANGE_RATES, NO_TOPN, NO_MIN_TOTAL, -5));
	}
	
	@Test
	public void should_throw_NullPointerException_if_customerId_in_an_order_is_null() {
		List<Order> orderWithNullCustomerId = List.of(o(null, "PL", "2.00", "PLN", "2025-12-05T03:05:06Z"));
		assertThrows(NullPointerException.class, () -> cut.generateCustomerIntelligenceReport(orderWithNullCustomerId, DEFAULT_EXCHANGE_RATES, NO_TOPN, NO_MIN_TOTAL, NO_MIN_ORDERS_COUNT));
	}
	
	@Test
	public void should_throw_NullPointerException_if_country_in_an_order_is_null() {
		List<Order> orderWithNullCountry = List.of(o("A", null, "2.00", "PLN", "2025-12-05T03:05:06Z"));
		assertThrows(NullPointerException.class, () -> cut.generateCustomerIntelligenceReport(orderWithNullCountry, DEFAULT_EXCHANGE_RATES, NO_TOPN, NO_MIN_TOTAL, NO_MIN_ORDERS_COUNT));
	}
	
	@Test
	public void should_throw_NullPointerException_if_amount_in_an_order_is_null() {
		List<Order> orderWithNullAmount = List.of(o("A", "PL", null, "PLN", "2025-12-05T03:05:06Z"));
		assertThrows(NullPointerException.class, () -> cut.generateCustomerIntelligenceReport(orderWithNullAmount, DEFAULT_EXCHANGE_RATES, NO_TOPN, NO_MIN_TOTAL, NO_MIN_ORDERS_COUNT));
	}
	
	@Test
	public void should_throw_NullPointerException_if_currency_in_an_order_is_null() {
		List<Order> orderWithNullCurrency = List.of(o("A", "PL", "2.50", null, "2025-12-05T03:05:06Z"));
		assertThrows(NullPointerException.class, () -> cut.generateCustomerIntelligenceReport(orderWithNullCurrency, DEFAULT_EXCHANGE_RATES, NO_TOPN, NO_MIN_TOTAL, NO_MIN_ORDERS_COUNT));
	}
	
	@Test
	public void should_throw_NullPointerException_if_timestamp_in_an_order_is_null() {
		List<Order> orderWithNullTimestamp = List.of(o("A", "PL", "2.50", "PLN", null));
		assertThrows(NullPointerException.class, () -> cut.generateCustomerIntelligenceReport(orderWithNullTimestamp, DEFAULT_EXCHANGE_RATES, NO_TOPN, NO_MIN_TOTAL, NO_MIN_ORDERS_COUNT));
	}
	
	@Test
	public void should_throw_MissingExchangeRateException_if_map_of_exchange_rates_does_not_contain_data_for_currency_present_in_an_order() {
		List<Order> orderWithInvalidCurrency = List.of(o("A", "PL", "2.50", "ZZZ", null));
		Exception ex = assertThrows(MissingExchangeRateException.class, () -> cut.generateCustomerIntelligenceReport(orderWithInvalidCurrency, DEFAULT_EXCHANGE_RATES, NO_TOPN, NO_MIN_TOTAL, NO_MIN_ORDERS_COUNT));
		assertTrue(ex.getMessage().contains("ZZZ"));
	}

	private static List<Order> generateOrders() {
		return List.of(
				o("A", "PL", "20.0", "PLN", "2026-01-23T13:24:35Z") 
				);
	}
	
	private static Map<String, BigDecimal> generateExchangeRates() {
		return Map.of(
				"PLN", new BigDecimal("0.25"),
				"USD", new BigDecimal("0.5"),
				"EUR", new BigDecimal("1.0")
				);
	}
	
	private static Order o(String customerId, String country, String amount, String currency, String timestamp) {
		BigDecimal amountOrNull = null;
		Instant timestampOrNull = null;
		
		if (amount != null) {
			amountOrNull = new BigDecimal(amount);
		}
		if (timestamp != null) {
			timestampOrNull = Instant.parse(timestamp);
		}
		
		return new Order(customerId, country, amountOrNull, currency, timestampOrNull);
	}
}
