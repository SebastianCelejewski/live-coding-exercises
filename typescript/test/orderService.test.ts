import { describe, it, expect } from "vitest";
import Decimal from "decimal.js";
import { getTotalAmountPerCustomer } from "../src/orderService";
import type { Order } from "../src/order";

describe("getTotalAmountPerCustomer", () => {

  const createOrders = (): Order[] => [
    { customer: "A", amount: new Decimal("1.0"), currency: "PLN" },
    { customer: "A", amount: new Decimal("2.0"), currency: "EUR" },
    { customer: "A", amount: new Decimal("3.0"), currency: "USD" },
    { customer: "B", amount: new Decimal("4.0"), currency: "PLN" },
    { customer: "B", amount: new Decimal("5.0"), currency: "EUR" },
    { customer: "B", amount: new Decimal("6.0"), currency: "USD" },
  ];

  it("returns totals per customer", () => {
    const result = getTotalAmountPerCustomer(createOrders());

    expect(result["A"].equals(new Decimal("6.0"))).toBe(true);
    expect(result["B"].equals(new Decimal("15.0"))).toBe(true);
  });

  it("works for a single order", () => {
    const result = getTotalAmountPerCustomer([
      { customer: "X", amount: new Decimal("17.0"), currency: "PLN" }
    ]);

    expect(result["X"].equals(new Decimal("17.0"))).toBe(true);
  });

  it("returns empty object for empty list", () => {
    const result = getTotalAmountPerCustomer([]);

    expect(Object.keys(result)).toHaveLength(0);
  });

});
