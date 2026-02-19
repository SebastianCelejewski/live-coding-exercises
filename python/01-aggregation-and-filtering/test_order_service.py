import unittest
from decimal import Decimal
from order import Order
from order_service import OrderService


class MyTestCase(unittest.TestCase):

    def setUp(self):
        self.cut = OrderService()

    def test_should_never_return_None(self):
        self.assertIsNotNone(self.cut.get_total_amount_per_customer(None))

    def test_should_return_data_for_all_customers(self):
        orders = self.prepare_orders()
        result = self.cut.get_total_amount_per_customer(orders)
        self.assertIn("A", result)
        self.assertIn("B", result)

    def test_should_return_total_amount_per_customer(self):
        orders = self.prepare_orders()
        result = self.cut.get_total_amount_per_customer(orders)
        self.assertEqual(Decimal("6.0"), result["A"])
        self.assertEqual(Decimal("15.0"), result["B"])

    def test_should_work_correctly_for_a_single_record(self):
        orders = [Order("X", Decimal("17.0"), "PLN")]
        result = self.cut.get_total_amount_per_customer(orders)
        self.assertEqual(Decimal("17.0"), result["X"])

    def test_should_ignore_customer_that_is_none(self):
        orders = self.prepare_orders()
        orders.append(Order(None, Decimal("13.0"), "PLN"))
        result = self.cut.get_total_amount_per_customer(orders)
        self.assertEqual(Decimal("6.0"), result["A"])

    def test_should_ignore_amount_that_is_none(self):
        orders = self.prepare_orders()
        orders.append(Order("A", None,  "PLN"))
        result = self.cut.get_total_amount_per_customer(orders)
        self.assertEqual(Decimal("6.0"), result["A"])

    def test_should_ignore_currency_that_is_none(self):
        orders = self.prepare_orders()
        orders.append(Order("A", Decimal("13.0"), None))
        result = self.cut.get_total_amount_per_customer(orders)
        self.assertEqual(Decimal("6.0"), result["A"])
        self.assertEqual(Decimal("15.0"), result["B"])

    def prepare_orders(self) -> list[Order]:
        return [
            Order("A", Decimal("1.0"), "PLN"),
            Order("A", Decimal("2.0"), "EUR"),
            Order("A", Decimal("3.0"), "USD"),
            Order("B", Decimal("4.0"), "PLN"),
            Order("B", Decimal("5.0"), "EUR"),
            Order("B", Decimal("6.0"), "USD")
        ]

if __name__ == '__main__':
    unittest.main()
