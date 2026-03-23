package pl.sebcel.livecoding.javastreams.orderservice;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderService {

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
				.filter(this::isValid)
				.collect(Collectors.toMap(Order::customer, Order::amount, BigDecimal::add));
	}
	
	private boolean isValid(Order order) {
		if (order == null || order.amount() == null || order.currency() == null || order.customer() == null) {
			return false;
		}
		return true;
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
		// group by customer
		// convert to EUR
		// aggregate by amount
		// filter out amount < minTotalEUR
		// return topN
		
		validateInput(orders, exchangeRates, minTotalEUR, topN);
		
		Map<String, BigDecimal> exchangeRatesMap = exchangeRates.stream().collect(Collectors.toMap(ExchangeRate::currency, ExchangeRate::rateToEUR));
		
		return orders.stream()
				.filter(this::isValid)
				.collect(Collectors.toMap(Order::customer, o -> convertToEur(o, exchangeRatesMap), BigDecimal::add))
				.entrySet().stream()
				.filter(e -> e.getValue().compareTo(minTotalEUR) >= 0)
				.sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
				.limit(topN)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, _) -> a, LinkedHashMap::new));
	}
	
	private BigDecimal convertToEur(Order order, Map<String, BigDecimal> exchangeRatesMap) {
		String currency = order.currency();
		BigDecimal amount = order.amount();
		if (!exchangeRatesMap.containsKey(currency)) {
			throw new IllegalArgumentException(currency);
		}
		return exchangeRatesMap.get(currency).multiply(amount);
	}
	
	private void validateInput(
			List<Order> orders,
			List<ExchangeRate> exchangeRates,
			BigDecimal minTotalEUR,
			int topN
		) {
		Objects.requireNonNull(orders);
		Objects.requireNonNull(exchangeRates);
		Objects.requireNonNull(minTotalEUR);
		if (topN < 0) {
			throw new IllegalArgumentException();
		}
		if (minTotalEUR.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException();
		}
	}
}
