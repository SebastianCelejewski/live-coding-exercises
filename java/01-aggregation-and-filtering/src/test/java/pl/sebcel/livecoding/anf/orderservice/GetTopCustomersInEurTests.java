package pl.sebcel.livecoding.anf.orderservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import pl.sebcel.livecoding.anf.ExchangeRate;
import pl.sebcel.livecoding.anf.Order;
import pl.sebcel.livecoding.anf.OrderService;

public class GetTopCustomersInEurTests {
	private OrderService cut = new OrderService();
	
	@Test
	public void should_throw_ArgumentNullException_if_list_of_orders_is_null() {
		assertThrows(NullPointerException.class, () -> {
			cut.getTopCustomersInEUR(null, createExchangeRates(), new BigDecimal("0.0"), 10);
		});
	}

	@Test
	public void should_throw_ArgumentNullException_if_list_of_exchange_rates_is_null() {
		assertThrows(NullPointerException.class, () -> {
			cut.getTopCustomersInEUR(createOrders(), null, new BigDecimal("0.0"), 10);
		});
	}

	@Test
	public void should_throw_ArgumentNullException_if_minimum_total_is_null() {
		assertThrows(NullPointerException.class, () -> {
			cut.getTopCustomersInEUR(createOrders(), createExchangeRates(), null, 10);
		});
	}
	
	@Test
	public void should_throw_IllegalArgumentException_if_minimum_total_is_negative() {
		assertThrows(IllegalArgumentException.class, () -> {
			cut.getTopCustomersInEUR(createOrders(), createExchangeRates(), new BigDecimal("-5.0"), 10);
		});
	}

	@Test
	public void should_throw_IllegalArgumentException_if_numer_of_returned_results_is_negative() {
		assertThrows(IllegalArgumentException.class, () -> {
			cut.getTopCustomersInEUR(createOrders(), createExchangeRates(), new BigDecimal("0.0"), -5);
		});
	}
	
	@Test
	public void should_return_data_for_all_customers() {
		Map<String, BigDecimal> result = cut.getTopCustomersInEUR(createOrders(), createExchangeRates(), new BigDecimal("0.0"), 99);
		assertTrue(result.containsKey("A"));
		assertTrue(result.containsKey("B"));
	}
	
	@Test
	public void should_return_total_amount_in_EUR() {
		Map<String, BigDecimal> result = cut.getTopCustomersInEUR(createOrders(), createExchangeRates(), new BigDecimal("0.0"), 99);
		assertEquals(0, new BigDecimal("4.25").compareTo(result.get("A")));
		assertEquals(0, new BigDecimal("9.5").compareTo(result.get("B")));
	}

	@Test
	public void should_ignore_null_orders() {
		List<Order> orders = createOrders();
		orders.add(null);
		Map<String, BigDecimal> result = cut.getTopCustomersInEUR(orders, createExchangeRates(), new BigDecimal("0.0"), 99);
		assertEquals(0, new BigDecimal("4.25").compareTo(result.get("A")));
		assertEquals(0, new BigDecimal("9.5").compareTo(result.get("B")));
	}

	@Test
	public void should_ignore_orders_with_null_customer() {
		List<Order> orders = createOrders();
		orders.add(new Order(null, new BigDecimal("1.0"), "PLN"));
		Map<String, BigDecimal> result = cut.getTopCustomersInEUR(orders, createExchangeRates(), new BigDecimal("0.0"), 99);
		assertEquals(0, new BigDecimal("4.25").compareTo(result.get("A")));
		assertEquals(0, new BigDecimal("9.5").compareTo(result.get("B")));
	}

	@Test
	public void should_ignore_orders_with_null_amount() {
		List<Order> orders = createOrders();
		orders.add(new Order("A", null, "PLN"));
		Map<String, BigDecimal> result = cut.getTopCustomersInEUR(orders, createExchangeRates(), new BigDecimal("0.0"), 99);
		assertEquals(0, new BigDecimal("4.25").compareTo(result.get("A")));
		assertEquals(0, new BigDecimal("9.5").compareTo(result.get("B")));
	}
	
	@Test
	public void should_ignore_orders_with_null_currency() {
		List<Order> orders = createOrders();
		orders.add(new Order("A", new BigDecimal("1.0"), null));
		Map<String, BigDecimal> result = cut.getTopCustomersInEUR(orders, createExchangeRates(), new BigDecimal("0.0"), 99);
		assertEquals(0, new BigDecimal("4.25").compareTo(result.get("A")));
		assertEquals(0, new BigDecimal("9.5").compareTo(result.get("B")));
	}
	
	@Test
	public void should_throw_IllegalArgumentException_if_exchange_rate_for_order_currency_is_missing() {
		Exception ex = assertThrows(IllegalArgumentException.class, () -> {
			List<Order> orders = createOrders();
			orders.add(new Order("A", new BigDecimal("1.0"), "XYZ"));
			cut.getTopCustomersInEUR(orders, createExchangeRates(), new BigDecimal("0.0"), 99);
		});
		assertTrue(ex.getMessage().contains("XYZ"));
	}
	
	@Test
	public void should_filter_out_customers_with_total_less_than_minTotalEUR() {
		List<Order> orders = createOrders();
		Map<String, BigDecimal> result = cut.getTopCustomersInEUR(orders, createExchangeRates(), new BigDecimal("8.0"), 99);
		assertFalse(result.containsKey("A"));
		assertTrue(result.containsKey("B"));
	}
	
	@Test
	public void should_return_topN_results() {
		List<Order> orders = createOrders();
		Map<String, BigDecimal> result = cut.getTopCustomersInEUR(orders, createExchangeRates(), new BigDecimal("0.0"), 1);
		assertFalse(result.containsKey("A"));
		assertTrue(result.containsKey("B"));
	}

	private List<Order> createOrders() {
		List<Order> result = new ArrayList<>();

		result.add(new Order("A", new BigDecimal("1.0"), "PLN"));
		result.add(new Order("A", new BigDecimal("2.0"), "USD"));
		result.add(new Order("A", new BigDecimal("3.0"), "EUR"));
		result.add(new Order("B", new BigDecimal("4.0"), "PLN"));
		result.add(new Order("B", new BigDecimal("5.0"), "USD"));
		result.add(new Order("B", new BigDecimal("6.0"), "EUR"));

		return result;
	}
	
	private List<ExchangeRate> createExchangeRates() {
		List<ExchangeRate> result = new ArrayList<>();
		
		result.add(new ExchangeRate("PLN", new BigDecimal("0.25")));
		result.add(new ExchangeRate("USD", new BigDecimal("0.5")));
		result.add(new ExchangeRate("EUR", new BigDecimal("1.")));
		
		return result;
	}
}
