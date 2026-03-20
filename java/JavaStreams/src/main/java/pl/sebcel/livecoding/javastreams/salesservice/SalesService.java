package pl.sebcel.livecoding.javastreams.salesservice;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SalesService {

	/**
	 * Returns top customers per country
	 * 
	 * If country has less than topN customers, return them all
	 * For every customer should return sum of amounts
	 * Must throw NullPointerException if list of orders is null
	 * Must throw IllegalArgumentException if topN is negative
	 * Customers per country must be returned in descending order
	 * 
	 * @param orders list of orders
	 * @param topN number of customers to return per country
	 * @return list of top customers with sums of their orders per country
	 */
	public Map<String, List<CustomerExpenses>> getTopCustomersPerCountry(List<Order> orders, int topN) {
		validateInput(orders, topN);
		
		return orders.stream()
				.collect(Collectors.groupingBy(
						Order::country,
						Collectors.collectingAndThen(
								Collectors.toMap(Order::customerId, Order::amount, BigDecimal::add),
								x -> x.entrySet()
									.stream()
									.sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
									.limit(topN)
									.map(kv -> new CustomerExpenses(kv.getKey(), kv.getValue()))
									.collect(Collectors.toList())
								)
						)
				);
				
				
		// pogrupować po kraju
		// zsumować amounts dla każdego klienta
		// zmapować na CustomerExpenses
		// posortować malejąco po amount
		// wybrać ileś początkowych
		// zrobić mapę
	}
	
	private void validateInput(List<Order> orders, int topN) {
		Objects.requireNonNull(orders);
		if (topN < 0) {
			throw new IllegalArgumentException();
		}
		
		for (Order o: orders) {
			Objects.requireNonNull(o.customerId());
			Objects.requireNonNull(o.country());
			Objects.requireNonNull(o.amount());
			if (o.amount().compareTo(BigDecimal.ZERO) < 0) {
				throw new IllegalArgumentException();
			}
		}
	}
}
