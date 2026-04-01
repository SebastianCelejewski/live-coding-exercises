package pl.sebcel.livecoding.javastreams.advancedorderservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
		List<Order> orderWithInvalidCurrency = List.of(o("A", "PL", "2.50", "ZZZ", "2025-12-05T02:02:02Z"));
		Exception ex = assertThrows(MissingExchangeRateException.class, () -> cut.generateCustomerIntelligenceReport(orderWithInvalidCurrency, DEFAULT_EXCHANGE_RATES, NO_TOPN, NO_MIN_TOTAL, NO_MIN_ORDERS_COUNT));
		assertTrue(ex.getMessage().contains("ZZZ"));
	}

	@Test
	public void should_return_data_for_all_countries_present_in_input_data() {
		Map<String, List<CustomerReport>> result = cut.generateCustomerIntelligenceReport(DEFAULT_ORDERS, DEFAULT_EXCHANGE_RATES, NO_TOPN, NO_MIN_TOTAL, NO_MIN_ORDERS_COUNT);
		assertThatResultContainsExactCountries(result, "PL", "DE");
	}
	
	@Test
	public void should_return_list_of_customer_reports_for_customers_from_that_country() {
		Map<String, List<CustomerReport>> result = cut.generateCustomerIntelligenceReport(DEFAULT_ORDERS, DEFAULT_EXCHANGE_RATES, NO_TOPN, NO_MIN_TOTAL, NO_MIN_ORDERS_COUNT);
		assertThatDataForCountryContainsExactCustomers(result, "PL", "A", "B", "C");
		assertThatDataForCountryContainsExactCustomers(result, "DE", "X", "Y", "Z");
	}
	
	@Test
	public void should_calculate_sum_of_amounts_for_every_client() {
		Map<String, List<CustomerReport>> result = cut.generateCustomerIntelligenceReport(DEFAULT_ORDERS, DEFAULT_EXCHANGE_RATES, NO_TOPN, NO_MIN_TOTAL, NO_MIN_ORDERS_COUNT);
		assertThatReportForCustomerContainsSumOfAmounts(result, "PL", "A", new BigDecimal("160.00"));
		assertThatReportForCustomerContainsSumOfAmounts(result, "PL", "B", new BigDecimal("400.00"));
		assertThatReportForCustomerContainsSumOfAmounts(result, "PL", "C", new BigDecimal("390.00"));
		assertThatReportForCustomerContainsSumOfAmounts(result, "DE", "X", new BigDecimal("500.00"));
		assertThatReportForCustomerContainsSumOfAmounts(result, "DE", "Y", new BigDecimal("40.00"));
		assertThatReportForCustomerContainsSumOfAmounts(result, "DE", "Z", new BigDecimal("340.00"));
	}
	
	@Test
	public void should_return_timestamp_of_last_operation_for_every_client() {
		Map<String, List<CustomerReport>> result = cut.generateCustomerIntelligenceReport(DEFAULT_ORDERS, DEFAULT_EXCHANGE_RATES, NO_TOPN, NO_MIN_TOTAL, NO_MIN_ORDERS_COUNT);
		assertThatCustomerReportsContainTimestampOfTheLastOperation(result, "PL", "A", "2026-03-21T11:14:32Z");
		assertThatCustomerReportsContainTimestampOfTheLastOperation(result, "PL", "B", "2026-07-23T16:34:32Z");
		assertThatCustomerReportsContainTimestampOfTheLastOperation(result, "PL", "C", "2026-09-13T14:24:34Z");
		assertThatCustomerReportsContainTimestampOfTheLastOperation(result, "DE", "X", "2026-12-13T12:44:55Z");
		assertThatCustomerReportsContainTimestampOfTheLastOperation(result, "DE", "Y", "2026-07-05T15:14:05Z");
		assertThatCustomerReportsContainTimestampOfTheLastOperation(result, "DE", "Z", "2026-09-19T17:54:25Z");
	}
	
	@Test
	public void should_convert_currencies_to_EUR_when_calculating_total_amount() {
		Map<String, List<CustomerReport>> result = cut.generateCustomerIntelligenceReport(generateOrdersWithVariousCurrencies(), DEFAULT_EXCHANGE_RATES, NO_TOPN, NO_MIN_TOTAL, NO_MIN_ORDERS_COUNT);
		assertThatReportForCustomerContainsSumOfAmounts(result, "PL", "A", new BigDecimal("3.00"));
	}
	
	@Test
	public void should_return_customer_reports_in_descending_order_by_total_amount() {
		Map<String, List<CustomerReport>> result = cut.generateCustomerIntelligenceReport(DEFAULT_ORDERS, DEFAULT_EXCHANGE_RATES, NO_TOPN, NO_MIN_TOTAL, NO_MIN_ORDERS_COUNT);
		assertThatCustomersReportsAreSortedByTotalAmountsDescending(result, "PL");
		assertThatCustomersReportsAreSortedByTotalAmountsDescending(result, "DE");
	}
	
	@Test
	public void should_return_max_topN_customers_per_country() {
		Map<String, List<CustomerReport>> result = cut.generateCustomerIntelligenceReport(DEFAULT_ORDERS, DEFAULT_EXCHANGE_RATES, 2, NO_MIN_TOTAL, NO_MIN_ORDERS_COUNT);
		assertTrue(result.get("PL").size() <= 2);
		assertTrue(result.get("DE").size() <= 2);
	}
	
	@Test
	public void should_return_empty_customer_lists_if_topN_is_zero() {
		Map<String, List<CustomerReport>> result = cut.generateCustomerIntelligenceReport(DEFAULT_ORDERS, DEFAULT_EXCHANGE_RATES, 0, NO_MIN_TOTAL, NO_MIN_ORDERS_COUNT);
		assertTrue(result.get("PL").size() == 0);
		assertTrue(result.get("DE").size() == 0);
	}

	@Test
	public void should_return_customers_that_have_total_amount_greater_or_equal_minTotal() {
		Map<String, List<CustomerReport>> result = cut.generateCustomerIntelligenceReport(DEFAULT_ORDERS, DEFAULT_EXCHANGE_RATES, NO_TOPN, new BigDecimal("400.00"), NO_MIN_ORDERS_COUNT);		
		assertThatDataForCountryContainsExactCustomers(result, "PL", "B");
		assertThatDataForCountryContainsExactCustomers(result, "DE", "X");
	}
	
	@Test
	public void should_return_customers_who_had_more_orders_than_minOrders() {
		Map<String, List<CustomerReport>> result = cut.generateCustomerIntelligenceReport(DEFAULT_ORDERS, DEFAULT_EXCHANGE_RATES, NO_TOPN, NO_MIN_TOTAL, 4);		
		assertThatDataForCountryContainsExactCustomers(result, "PL", "B");
		assertThatDataForCountryContainsExactCustomers(result, "DE", "X");
	}
	
	private static List<Order> generateOrders() {
		return List.of(
				o("A", "PL", "20.0", "EUR", "2026-01-23T13:24:34Z"),
				o("A", "PL", "50.0", "EUR", "2026-02-22T12:14:33Z"),
				o("A", "PL", "90.0", "EUR", "2026-03-21T11:14:32Z"),
				o("B", "PL", "320.0", "EUR", "2026-04-27T13:24:31Z"),
				o("B", "PL", "10.0", "EUR", "2026-05-23T14:14:30Z"),
				o("B", "PL", "50.0", "EUR", "2026-06-22T15:14:31Z"),
				o("B", "PL", "20.0", "EUR", "2026-07-23T16:34:32Z"),
				o("C", "PL", "30.0", "EUR", "2026-08-21T15:54:33Z"),
				o("C", "PL", "360.0", "EUR", "2026-09-13T14:24:34Z"),
				o("X", "DE", "10.0", "EUR", "2026-11-13T13:54:45Z"),
				o("X", "DE", "50.0", "EUR", "2026-12-13T12:44:55Z"),
				o("X", "DE", "120.0", "EUR", "2026-03-23T11:34:45Z"),
				o("X", "DE", "320.0", "EUR", "2026-04-13T12:24:35Z"),
				o("Y", "DE", "10.0", "EUR", "2026-05-03T13:14:25Z"),
				o("Y", "DE", "10.0", "EUR", "2026-06-04T14:54:15Z"),
				o("Y", "DE", "20.0", "EUR", "2026-07-05T15:14:05Z"),
				o("Z", "DE", "220.0", "EUR", "2026-08-09T16:14:15Z"),
				o("Z", "DE", "120.0", "EUR", "2026-09-19T17:54:25Z")
				);
	}
	
	private static List<Order> generateOrdersWithVariousCurrencies() {
		return List.of(
				o("A", "PL", "4", "PLN", "2026-10-10T17:00:00Z"),
				o("A", "PL", "2", "USD", "2026-10-10T17:00:01Z"),
				o("A", "PL", "1", "EUR", "2026-10-10T17:00:02Z")
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
	
	private void assertThatResultContainsExactCountries(Map<String, List<CustomerReport>> result, String... expectedCountries) {
		assertEquals(expectedCountries.length, result.size());
		for (String country : expectedCountries) {
			assertTrue(result.keySet().contains(country));
		}
	}
	
	private void assertThatDataForCountryContainsExactCustomers(Map<String, List<CustomerReport>> result, String country, String... customerIds) {
		Set<String> customersInReports = result.get(country).stream().map(x -> x.customerId()).collect(Collectors.toSet());
		assertEquals(customerIds.length, customersInReports.size());
		for (String customerId : customerIds) {
			assertTrue(customersInReports.contains(customerId));
		}
	}
	
	private void assertThatReportForCustomerContainsSumOfAmounts(Map<String, List<CustomerReport>> result, String country, String customerId, BigDecimal expectedTotal) {
		BigDecimal actualTotal = result
									.get(country)
									.stream()
									.filter(x -> x.customerId().equals(customerId))
									.map(y -> y.totalEUR())
									.findFirst()
									.orElseThrow();
		
		assertTrue(expectedTotal.equals(actualTotal), "Expected: " + expectedTotal+", actual: " + actualTotal);
	}
	
	private void assertThatCustomersReportsAreSortedByTotalAmountsDescending(Map<String, List<CustomerReport>> result, String country) {
		List<CustomerReport> reports = result.get(country);
		for (int i = 1; i < reports.size(); i++) {
			assertTrue(reports.get(i - 1).totalEUR().compareTo(reports.get(i).totalEUR()) >= 0);
		}
	}
	
	private void assertThatCustomerReportsContainTimestampOfTheLastOperation(Map<String, List<CustomerReport>> result, String country, String customerId, String expectedTimeStamp) {
		Instant actualTimestamp = result
									.get(country)
									.stream()
									.filter(x -> x.customerId().equals(customerId))
									.map(x -> x.lastOrderTimestamp())
									.findFirst()
									.orElseThrow();
		assertTrue(Instant.parse(expectedTimeStamp).equals(actualTimestamp));
	}	
}
