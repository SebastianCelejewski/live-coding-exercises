package pl.sebcel.livecoding.javastreams.advancedorderservice;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
		throw new UnsupportedOperationException();
	}
}
