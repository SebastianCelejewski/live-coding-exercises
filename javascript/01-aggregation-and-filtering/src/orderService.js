import Decimal from 'decimal.js';

export function getTotalAmountPerCustomer(orders) {

  if (orders == null) {
    return {};
  }

  return orders
    .filter(isValid)
    .reduce((acc, { customer, amount }) => {
        acc[customer] = (acc[customer] ?? new Decimal("0.0")).plus(amount);
        return acc;
    }, {})
}

function isValid(order) {
    return order
        && order.customer != null
        && order.amount != null
        && order.currency != null;
}