import test from 'node:test';
import assert from 'node:assert';
import { getTotalAmountPerCustomer } from '../src/orderService.js';
import Decimal from 'decimal.js';
import { Order } from "../src/order.js";

test('should not return null', () => {
  const result = getTotalAmountPerCustomer(null);
  assert.ok(result !== null);
});

test('should not return undefined', () => {
  const result = getTotalAmountPerCustomer(null);
  assert.ok(result !== undefined);
});

test('should return data for all customers', () => {
    const orders = createOrders();
    const result = getTotalAmountPerCustomer(orders);
    assert.ok(Object.hasOwn(result, "A"));
    assert.ok(Object.hasOwn(result, "B"));
});

test('should return total amount per customer', () => {
    const orders = createOrders();
    const result = getTotalAmountPerCustomer(orders);
    assert.ok(result["A"].equals(new Decimal("6.0")));
    assert.ok(result["B"].equals(new Decimal("15.0")));
});

test('should work for a single order', () => {
    const result = getTotalAmountPerCustomer([new Order("X", new Decimal("17.0"), "PLN")]);
    assert.ok(result["X"].equals(new Decimal("17.0")));
});

test('should ignore null order', () => {
    const orders = createOrders();
    orders.push(null);
    const result = getTotalAmountPerCustomer(orders);
    assert.ok(Object.keys(result).length === 2);
});

test('should ignore orders with null or undefined customer', () => {
    const orders = createOrders();
    orders.push(new Order(null, new Decimal("17.0"), "PLN"));
    orders.push(new Order(undefined, new Decimal("17.0"), "PLN"));
    
    const result = getTotalAmountPerCustomer(orders);

    assert.ok(Object.keys(result).length === 2);
    assert.ok(result["A"].equals(new Decimal("6.0")));
});

test('should ignore orders with null or undefined amount', () => {
    const orders = createOrders();
    orders.push(new Order("A", null, "PLN"));
    orders.push(new Order("A", undefined, "PLN"));
    
    const result = getTotalAmountPerCustomer(orders);

    assert.ok(Object.keys(result).length === 2);
    assert.ok(result["A"].equals(new Decimal("6.0")));
});

test('should ignore orders with null or undefined currency', () => {
    const orders = createOrders();
    orders.push(new Order("A", new Decimal("17.0"), null));
    orders.push(new Order("A", new Decimal("17.0"), undefined));
    
    const result = getTotalAmountPerCustomer(orders);

    assert.ok(Object.keys(result).length === 2);
    assert.ok(result["A"].equals(new Decimal("6.0")));
});

function createOrders() {
    return [
        new Order("A", new Decimal("1.0"), "PLN"),
        new Order("A", new Decimal("2.0"), "EUR"),
        new Order("A", new Decimal("3.0"), "USD"),
        new Order("B", new Decimal("4.0"), "PLN"),
        new Order("B", new Decimal("5.0"), "EUR"),
        new Order("B", new Decimal("6.0"), "USD"),
    ]
}