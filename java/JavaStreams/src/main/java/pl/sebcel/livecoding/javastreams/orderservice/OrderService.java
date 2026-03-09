package pl.sebcel.livecoding.javastreams.orderservice;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderService {

	private static boolean isValid(Order order) {
		if (order == null) {
			return false;
		}
		
		if (order.customer() == null || order.amount() == null || order.currency() == null) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Requirements:
	 * <ul>
	 *   <li>Aggregate total amount ignoring the currency (1 EUR = 1 USD = 1 PLN etc.)</li>
	 * </ul>
	 * @param orders list of orders
	 * @return map from client to total amount
	 */
	public Map<String, BigDecimal> calculateTotalPerClient(List<Order> orders) {
		if (orders == null) {
			return Collections.emptyMap();
		}
		
		return orders
				.stream()
				.filter(OrderService::isValid)
				.collect(Collectors.toMap(Order::customer, Order::amount, BigDecimal::add));
	}
	
	/**
	 * Requirements:
	 * <ul>
	 * 	 <li>Convert each order to EUR using the exchange rate</li>
	 *   <li>Aggregate total EUR per customer</li>
	 *	 <li>Filter out customers with total < minTotalEUR</li>
	 *	 <li>Sort descending by total</li>
	 *	 <li>Return only the first topN</li>
	 * </ul>
	 *	
	 *	<p>Return something like:</p>
	 *  <pre>
	 *	[
	 *	  { customer: "B", totalEUR: Decimal(...) },
	 *	  { customer: "A", totalEUR: Decimal(...) }
	 *	]
	 *  </pre>
	 **/
	public Map<String, BigDecimal> getTopCustomersInEUR(
		List<Order> orders,
		List<ExchangeRate> exchangeRates,
		BigDecimal minTotalEUR,
		int topN
	) {
		if (orders == null || exchangeRates == null || minTotalEUR == null) {
			throw new NullPointerException();
		}
		
		if (minTotalEUR.compareTo(new BigDecimal("0.0")) < 0 || topN < 0) {
			throw new IllegalArgumentException();
		}
		
		Map<String, BigDecimal> requiredExchangeRates = exchangeRates
				.stream()
				.collect(Collectors.toMap(ExchangeRate::currency, ExchangeRate::rateToEUR));
		
		return orders
				.stream()
				.filter(OrderService::isValid)
				.map(o -> convertCurrency(o, requiredExchangeRates))
				.collect(Collectors.toMap(Order::customer, Order::amount, BigDecimal::add))
				.entrySet()
				.stream()
				.filter(x -> x.getValue().compareTo(minTotalEUR) >= 0)
				.sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
				.limit(topN)
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						Map.Entry::getValue,
						(a, _) -> a,
						LinkedHashMap::new));
	}

	private Order convertCurrency(Order order, Map<String, BigDecimal> requiredExchangeRates) {
		if (!requiredExchangeRates.containsKey(order.currency())) {
			throw new IllegalArgumentException("Missing exchange rate for " + order.currency());
		}
		BigDecimal conversionRate = requiredExchangeRates.get(order.currency());
		return new Order(order.customer(), order.amount().multiply(conversionRate), "EUR");
	}

}
