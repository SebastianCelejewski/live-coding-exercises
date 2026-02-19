from order import Order
from decimal import Decimal
from collections import defaultdict


class OrderService:
    def get_total_amount_per_customer(self, orders: list[Order]) -> dict[str, Decimal]:
        if not orders:
            return {}

        valid_orders = (
            o for o in orders
            if o and o.customer and o.amount and o.currency
        )

        result = defaultdict(Decimal)

        for order in valid_orders:
            result[order.customer] += order.amount

        return dict(result)

