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
	 * <li>If an order contains currency that is not present in exchangeRates data, an exception is thrown</li>
	 * <li>List of consumer reports is sorted by total amounts descending</li>
	 * <li>If two consumers have the same total amount, their order is be consistent,
	 *   i.e. it does not matter which goes first on the list, but but this order is preserved between method calls </li>
	 * <li>Consumer report contains the time stamp of the last operation for given customer</li>
	 * <li>An exception is thrown if data is invalid, e.g. order is null or any value inside the order is null</li>
	 * <li>CustomerReport: String customerId, BigDecimal totalEUR, int ordersCount, Instant lastOrderTimestamp</li>   
	 * </ul>
	 * @param orders list of orders as an input
	 * @param exchangeRates mapping between currencies and their rate to EUR
	 * @param topN maximum number of customers to return for a single country
	 * @param minTotalEUR minimal total amount for a customer to be included in a report
	 * @param minOrdersOunt minimal number of orders for a customer to be included in a report
	 * @return
	 */
	public Map<String, List<CustomerReport>> generateCustomerIntelligenceReport(
			List<Order> orders,
			Map<String, BigDecimal> exchangeRates,
			int topN,
			BigDecimal minTotalEUR,
			int minOrdersCount) {
		
		validateInput(orders, exchangeRates, topN, minTotalEUR, minOrdersCount);
		return orders.stream()									// Stream<Order>
				.collect(Collectors.groupingBy(					// Map<String, Stream<Order>>
						Order::country, 
						Collectors.collectingAndThen(
							Collectors.groupingBy(					// Map<String, Map<String, Stream<Order>>>
								Order::customerId,
								Collector.of(										// Map<String, Map<String, CustomerReport>>
										() -> new Accumulator(exchangeRates),
										Accumulator::addOrder,
										Accumulator::merge,
										Accumulator::toCustomerReport	
								)
							),
							x -> x.values()
									.stream()														// Stream<CustomerReport>
									.filter(cr -> cr.ordersCount() >= minOrdersCount)
									.filter(cr -> cr.totalEUR().compareTo(minTotalEUR) >= 0)
									.sorted(Comparator.comparing(CustomerReport::totalEUR).reversed().thenComparing(CustomerReport::customerId))
									.limit(topN)
									.collect(Collectors.toList())									// List<CustomerReport>
						)
				));
	}
	
	private class Accumulator {
		
		private Map<String, BigDecimal> exchangeRates;
		
		public String customerId;
		public BigDecimal totalAmount = BigDecimal.ZERO;
		public int ordersCount = 0;
		public Instant lastOperationTimestamp = Instant.MIN;
		
		
		public Accumulator(Map<String, BigDecimal> exchangeRates) {
			this.exchangeRates = exchangeRates;
		}
		
		public Accumulator addOrder(Order order) {
			this.customerId = order.customerId();
			this.totalAmount = this.totalAmount.add(order.amount().multiply(exchangeRates.get(order.currency())));
			this.ordersCount += 1;
			insertIfLater(order.timestamp());
			return this;
		}
		
		public Accumulator merge(Accumulator other) {
			this.totalAmount = this.totalAmount.add(other.totalAmount);
			this.ordersCount += other.ordersCount;
			insertIfLater(other.lastOperationTimestamp);
			return this;
		}
		
		public CustomerReport toCustomerReport() {
			return new CustomerReport(customerId, totalAmount, ordersCount, lastOperationTimestamp);
		}
		
		private void insertIfLater(Instant other) {
			if (other.isAfter(this.lastOperationTimestamp)) {
				this.lastOperationTimestamp = other;
			}
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
