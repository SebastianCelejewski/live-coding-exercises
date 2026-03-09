package pl.sebcel.livecoding.javastreams.orderservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import pl.sebcel.livecoding.javastreams.orderservice.Order;
import pl.sebcel.livecoding.javastreams.orderservice.OrderService;

public class CalculateTotalPerClientTests {

	private OrderService cut = new OrderService();
	
	@Test
	public void should_never_return_null() {
		assertNotNull(cut.calculateTotalPerClient(null));
	}
	
	@Test
	public void should_handle_single_order() {
		List<Order> orders = createOrders();
		orders.add(new Order("X", new BigDecimal("1.0"), "PLN"));
		Map<String, BigDecimal> result = cut.calculateTotalPerClient(orders);
		assertTrue(result.containsKey("X"));
		assertEquals(new BigDecimal("1.0"), result.get("X"));
	}
	
	@Test
	public void should_return_data_for_all_clients() {
		List<Order> orders = createOrders();
		Map<String, BigDecimal> result = cut.calculateTotalPerClient(orders);
		assertTrue(result.containsKey("A"));
		assertTrue(result.containsKey("B"));
	}

	@Test
	public void should_return_sum_of_orders_per_client_while_ignoring_currency() {
		List<Order> orders = createOrders();
		Map<String, BigDecimal> result = cut.calculateTotalPerClient(orders);
		assertEquals(new BigDecimal("6.0"), result.get("A"));
		assertEquals(new BigDecimal("15.0"), result.get("B"));
	}
	
	@Test
	public void should_ignore_null_orders() {
		List<Order> orders = createOrders();
		orders.add(null);
		Map<String, BigDecimal> result = cut.calculateTotalPerClient(orders);
		assertEquals(new BigDecimal("6.0"), result.get("A"));
		assertEquals(new BigDecimal("15.0"), result.get("B"));
	}

	@Test
	public void should_ignore_orders_with_null_customer() {
		List<Order> orders = createOrders();
		orders.add(new Order(null, new BigDecimal("1.0"), "PLN"));
		Map<String, BigDecimal> result = cut.calculateTotalPerClient(orders);
		assertEquals(new BigDecimal("6.0"), result.get("A"));
		assertEquals(new BigDecimal("15.0"), result.get("B"));
	}

	@Test
	public void should_ignore_orders_with_null_amount() {
		List<Order> orders = createOrders();
		orders.add(new Order("A", null, "PLN"));
		Map<String, BigDecimal> result = cut.calculateTotalPerClient(orders);
		assertEquals(new BigDecimal("6.0"), result.get("A"));
		assertEquals(new BigDecimal("15.0"), result.get("B"));
	}
	
	@Test
	public void should_ignore_orders_with_null_currency() {
		List<Order> orders = createOrders();
		orders.add(new Order("A", new BigDecimal("1.0"), null));
		Map<String, BigDecimal> result = cut.calculateTotalPerClient(orders);
		assertEquals(new BigDecimal("6.0"), result.get("A"));
		assertEquals(new BigDecimal("15.0"), result.get("B"));
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
}
