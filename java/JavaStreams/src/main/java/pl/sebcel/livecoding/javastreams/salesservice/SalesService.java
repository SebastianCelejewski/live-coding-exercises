package pl.sebcel.livecoding.javastreams.salesservice;

import java.util.List;
import java.util.Map;

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
		throw new UnsupportedOperationException();
	}
}
