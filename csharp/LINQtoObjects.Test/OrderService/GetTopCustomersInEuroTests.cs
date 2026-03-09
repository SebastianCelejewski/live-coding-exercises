using LiveCodingExercises.LINQtoObjects.OrderService;
using System;
using System.Collections.Generic;
using Xunit;

namespace LiveCodingExercises.LINQtoObjects.Test.OrderService
{
    public class GetTopCustomersInEuroTests
    {
        private readonly LINQtoObjects.OrderService.OrderService cut = new LINQtoObjects.OrderService.OrderService();

        [Fact]
        public void Should_throw_ArgumentOutOfRangeException_when_minimum_total_is_negative()
        {
            Assert.Throws<ArgumentOutOfRangeException>(() => cut.GetTopCustomersInEUR(CreateOrders(), CreateExchangeRates(), -7.0m, 99));
        }

        [Fact]
        public void Should_throw_ArgumentOutOfRangeException_when_numer_of_returned_orders_is_negative()
        {
            Assert.Throws<ArgumentOutOfRangeException>(() => cut.GetTopCustomersInEUR(CreateOrders(), CreateExchangeRates(), 7.0m, -4));
        }

        [Fact]
        public void Should_return_data_for_all_customers()
        {
            IReadOnlyDictionary<string, decimal> result = cut.GetTopCustomersInEUR(CreateOrders(), CreateExchangeRates(), 0.0m, 99);
            Assert.True(result.ContainsKey("A"));
            Assert.True(result.ContainsKey("B"));
        }

        [Fact]
        public void Should_handle_single_order()
        {
            List<Order> orders = [new Order("A", 1.0m, "PLN")];
            IReadOnlyDictionary<string, decimal> result = cut.GetTopCustomersInEUR(orders, CreateExchangeRates(), 0.0m, 99);
            Assert.True(result.ContainsKey("A"));
            Assert.Equal(0.25m, result["A"]);
        }

        [Fact]
        public void Should_return_sum_of_amounts_per_customer_in_EUR()
        {
            IReadOnlyDictionary<string, decimal> result = cut.GetTopCustomersInEUR(CreateOrders(), CreateExchangeRates(), 0.0m, 99);
            Assert.Equal(4.25m, result["A"]);
            Assert.Equal(9.5m, result["B"]);
        }

        [Fact]
        public void Should_return_topN_results()
        {
            IReadOnlyDictionary<string, decimal> result = cut.GetTopCustomersInEUR(CreateOrders(), CreateExchangeRates(), 0.0m, 1);
            Assert.False(result.ContainsKey("A"));
            Assert.True(result.ContainsKey("B"));
        }

        [Fact]
        public void Should_filter_out_results_with_total_amount_less_than_a_threshold()
        {
            IReadOnlyDictionary<string, decimal> result = cut.GetTopCustomersInEUR(CreateOrders(), CreateExchangeRates(), 8.0m, 99);
            Assert.False(result.ContainsKey("A"));
            Assert.True(result.ContainsKey("B"));
        }

        [Fact]
        public void Should_throw_MissingExchangeRateException_when_order_contains_currency_not_present_in_exchange_rates()
        {
            IList<Order> orders = CreateOrders();
            orders.Add(new Order("A", 1.0m, "XYZ"));
            Assert.Throws<MissingExchangeRateException>(() =>
            {
                cut.GetTopCustomersInEUR(orders, CreateExchangeRates(), 0m, 99);
            });
        }

        private List<Order> CreateOrders()
        {
            List<Order> orders =
            [
                new Order("A", 1.0m, "PLN"),
                new Order("A", 2.0m, "USD"),
                new Order("A", 3.0m, "EUR"),
                new Order("B", 4.0m, "PLN"),
                new Order("B", 5.0m, "USD"),
                new Order("B", 6.0m, "EUR"),
            ];

            return orders;
        }
        private IEnumerable<ExchangeRate> CreateExchangeRates()
        {
            List<ExchangeRate> exchangeRates = [
                new ExchangeRate("PLN", 0.25m),
                new ExchangeRate("USD", 0.5m),
                new ExchangeRate("EUR", 1.0m)
            ];
            return exchangeRates;
        }
    }
}