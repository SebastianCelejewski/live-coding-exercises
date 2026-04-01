package pl.sebcel.livecoding.javastreams.advancedorderservice;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class AdvancedOrderService {

	/**
	 * Creates customer reports for all countries
	 * 
	 * <ul>
	 * <li>Returned data contains a list of consumer report for every country</li>
	 * <li>List of consumer reports contains data for consumers for which:
	 *   <ul>
	 *     <li>total amount is greater than minTotalEUR</li>
	 *     <li>and number of orders is greater than minOrdersCount</li>
	 *   </ul>
	 * </li>
	 * <li>List of consumer reports for a single country returns data for at most topN consumers</li>
	 * <li>If an order contains currency that is not present in exchangeRates data, an exception must be thrown</li>
	 * <li>List of consumer reports is sorted by total amounts descending</li>
	 * <li>If two consumers have the same total amount, their order must be consistent,
	 *   i.e. it does not matter which goes first on the list, but but this order must be preserved between method calls </li>
	 * <li>An exception must be thrown if data is invalid, e.g. order is null or any value inside the order is null</li>   
	 * </ul>
	 * @param orders
	 * @param exchangeRates
	 * @param topN
	 * @param minTotalEUR
	 * @param minOrdersOunt
	 * @return
	 */
	public Map<String, List<CustomerReport>> generateCustomerIntelligenceReport(
			List<Order> orders,
			Map<String, BigDecimal> exchangeRates,
			int topN,
			BigDecimal minTotalEUR,
			int minOrdersCount) {
		
		validateInput(orders, exchangeRates, topN, minTotalEUR, minOrdersCount);
		
		// group by country
		// aggregate amounts with currency conversion
		// map to CustomerReport
		
		return orders
				.stream()
				.collect(Collectors.groupingBy(          // Map<String, List<Order>>, where List<Orders> is per country
						Order::country, 
						Collectors.collectingAndThen(
								Collectors.groupingBy(           // Map<String, Map<String, List<Order>>>, where List<Orders> is per single customerId 
										Order::customerId,
										Collector.of(			 // Map<String, Map<String, CustomerReport>>>
												MutableAccumulator::new,
												(acc, order) -> acc.addOrder(order, exchangeRates),
												(a, b) -> a.merge(b),
												(acc) -> acc.toCustomerReport()
										)
								),
								map -> map.values()  // Map<String, List<CustomerReport>>
									.stream()
									.filter(cr -> cr.totalEUR().compareTo(minTotalEUR) >= 0)
									.filter(cr -> cr.ordersCount() >= minOrdersCount)
									.sorted(Comparator.comparing(CustomerReport::totalEUR).reversed())
									.limit(topN)
									.toList()    
							)
				));
	}
	
	private class MutableAccumulator {
		public String customerId = null;
		public BigDecimal totalEUR = BigDecimal.ZERO;
		public int numerOfOrders = 0;
		public Instant latestOrderTimestamp = Instant.MIN;
		
		public void addOrder(Order order, Map<String, BigDecimal> conversionRates) {
			this.customerId = order.customerId();
			this.totalEUR = this.totalEUR.add(order.amount().multiply(conversionRates.get(order.currency())));
			this.numerOfOrders += 1;
			if (order.timestamp().isAfter(latestOrderTimestamp)) {
				this.latestOrderTimestamp = order.timestamp();
			}
		}
		
		public MutableAccumulator merge(MutableAccumulator other) {
			this.totalEUR = this.totalEUR.add(other.totalEUR);
			this.numerOfOrders = this.numerOfOrders + other.numerOfOrders;
			if (other.latestOrderTimestamp.isAfter(this.latestOrderTimestamp)) {
				this.latestOrderTimestamp = other.latestOrderTimestamp;
			}
			return this;
		}
		
		public CustomerReport toCustomerReport() {
			return new CustomerReport(customerId, totalEUR, numerOfOrders, latestOrderTimestamp);
		}
	}
	

	private void validateInput(
			List<Order> orders,
			Map<String, BigDecimal> exchangeRates,
			int topN,
			BigDecimal minTotalEUR,
			int minOrdersCount) {

		Objects.requireNonNull(orders);
		Objects.requireNonNull(exchangeRates);
		Objects.requireNonNull(minTotalEUR);
		
		if (topN < 0) {
			throw new IllegalArgumentException();
		}
		if (minTotalEUR.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException();
		}
		if (minOrdersCount < 0) {
			throw new IllegalArgumentException();
		}
		
		for(Order o : orders) {
			Objects.requireNonNull(o);
			Objects.requireNonNull(o.amount());
			Objects.requireNonNull(o.country());
			Objects.requireNonNull(o.currency());
			Objects.requireNonNull(o.customerId());
			Objects.requireNonNull(o.timestamp());
			if (!exchangeRates.containsKey(o.currency())) {
				throw new MissingExchangeRateException(o.currency());
			}
		}
	}
}
