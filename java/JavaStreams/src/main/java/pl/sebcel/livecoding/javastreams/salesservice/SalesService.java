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
		Objects.requireNonNull(orders);
		if (topN < 0) {
			throw new IllegalArgumentException();
		}
		
		// group by country   -> Map<String, List<Order>>
		// inside country
		//   customer-amount aggregate by amount (sum)   ->     List<Order> -> Map<String, BigDecimal> (aggregation, sum)
		//   sort by aggregated amount desc
		//   take topN
		
		
		return orders
				.stream()
				.collect(Collectors.groupingBy(
						Order::country,						// Map<String, List<Order>>
						Collectors.collectingAndThen(
							Collectors.toMap(
									Order::customerId,			// Map<String, Map<String, List<Order>>>
									Order::amount,
									BigDecimal::add),
							x -> x.entrySet()
									.stream()
									.sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
									.limit(topN)
									.map(e -> new CustomerExpenses(e.getKey(), e.getValue())).toList() // Map<String, List<CustomerExpenses>>
						)));  
				
	}
	
}
