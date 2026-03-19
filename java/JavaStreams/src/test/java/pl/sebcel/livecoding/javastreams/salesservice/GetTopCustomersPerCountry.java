package pl.sebcel.livecoding.javastreams.salesservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class GetTopCustomersPerCountry {
	
	private SalesService cut = new SalesService();
	
	@Test
	public void should_validate_input() {
		assertThrows(NullPointerException.class, () -> cut.getTopCustomersPerCountry(null, 0));
		assertThrows(IllegalArgumentException.class, () -> cut.getTopCustomersPerCountry(createOrders(), -5));
	}
	
	@Test
	public void should_return_data_for_all_countries_present_in_input() {
		Map<String, List<CustomerExpenses>> result = cut.getTopCustomersPerCountry(createOrders(), 99);
		assertThatResultContainsExactCountries(result, "PL", "DE");
	}
	
	@Test
	public void should_aggregate_amounts_per_customer_per_country() {
		Map<String, List<CustomerExpenses>> result = cut.getTopCustomersPerCountry(createOrders(), 99);
		assertThatAmountsAreAggregated(result, "PL", "A", "150");
		assertThatAmountsAreAggregated(result, "PL", "B", "200");
		assertThatAmountsAreAggregated(result, "DE", "X", "300");
		assertThatAmountsAreAggregated(result, "DE", "Y", "100");
		assertThatAmountsAreAggregated(result, "DE", "Z", "200");
	}
	
	@Test
	public void should_limit_results_to_topN_customers_per_country() {
		Map<String, List<CustomerExpenses>> result = cut.getTopCustomersPerCountry(createOrders(), 2);
		assertThatResultContainsExactCustomers(result, "DE", "X", "Z");
	}
	
	@Test
	public void should_return_all_customers_if_there_are_less_customers_than_topN() {
		Map<String, List<CustomerExpenses>> result = cut.getTopCustomersPerCountry(createOrders(), 5);
		assertThatResultContainsExactCustomers(result, "PL", "A", "B");
		assertThatResultContainsExactCustomers(result, "DE", "X", "Y", "Z");
	}

	@Test
	public void should_return_no_customers_if_topN_is_zero() {
		Map<String, List<CustomerExpenses>> result = cut.getTopCustomersPerCountry(createOrders(), 0);
		assertThatResultDoesNotContainCustomers(result, "PL");
		assertThatResultDoesNotContainCustomers(result, "DE");
	}

	@Test
	public void should_return_customers_in_descending_order() {
		Map<String, List<CustomerExpenses>> result = cut.getTopCustomersPerCountry(createOrders(), 99);
		assertThatCustomersAreSortedByExpensesDescending(result, "PL");
		assertThatCustomersAreSortedByExpensesDescending(result, "DE");
	}

	private void assertThatResultContainsExactCountries(Map<String, List<CustomerExpenses>> result, String... countries) {
		Set<String> actualCountries = result.keySet();
		Set<String> expectedCountries = Set.of(countries);
		assertEquals(expectedCountries, actualCountries);
	}

	private void assertThatAmountsAreAggregated(Map<String, List<CustomerExpenses>> result, String country, String customerId, String expectedSumOfExpenses) {
		BigDecimal actualSum = result.get(country).stream().filter(c -> c.customerId().equals(customerId)).findFirst().orElseThrow().sumOfExpenses();
		BigDecimal expectedSum = new BigDecimal(expectedSumOfExpenses);
		assertEquals(expectedSum, actualSum);
	}
	
	private void assertThatResultContainsExactCustomers(Map<String, List<CustomerExpenses>> result, String country, String... customerIds) {
		Set<String> actualCustomerIds = result.get(country).stream().map(x -> x.customerId()).collect(Collectors.toSet());
		Set<String> expectedCustomerIds = Set.of(customerIds);
		assertEquals(expectedCustomerIds, actualCustomerIds);
	}
	
	private void assertThatResultDoesNotContainCustomers(Map<String, List<CustomerExpenses>> result, String country) {
		assertTrue(result.get(country).isEmpty());
	}

	private void assertThatCustomersAreSortedByExpensesDescending(Map<String, List<CustomerExpenses>> result, String country) {
		List<CustomerExpenses> ce = result.get(country);
		for (int i = 1; i < ce.size(); i++) {
			assertTrue(ce.get(i).sumOfExpenses().compareTo(ce.get(i-1).sumOfExpenses()) < 0);
		}
	}

	private List<Order> createOrders() {
		return List.of(
				new Order("A", "PL", "100"),
				new Order("A", "PL", "50"),
				new Order("B", "PL", "200"),
				new Order("C", "PL", "10"),
				new Order("X", "DE", "300"),
				new Order("Y", "DE", "100"),
				new Order("Z", "DE", "200")
				);
	}
}
