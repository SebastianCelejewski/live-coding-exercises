import Decimal from "decimal.js";
import { Order } from "./order";

export function getTotalAmountPerCustomer(orders: Order[]): Record<string, Decimal> {
  return orders.reduce<Record<string, Decimal>>((acc, { customer, amount }) => {
    acc[customer] = (acc[customer] ?? new Decimal("0"))
      .plus(amount);
    return acc;
  }, {});
}
