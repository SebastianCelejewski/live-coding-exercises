using LiveCodingSolutions.AggregationAndFiltering;
using Xunit;

namespace LiveCodingTests.AggregationAndFiltering.OrderService
{
    public class GetTotalAmountPerCustomerTests
    {
        private readonly LiveCodingSolutions.AggregationAndFiltering.OrderService cut = new LiveCodingSolutions.AggregationAndFiltering.OrderService();

        [Fact]
        public void Should_return_data_for_all_customers()
        {
            IReadOnlyDictionary<string, decimal> result = cut.GetTotalAmountPerCustomer(CreateOrders());
            Assert.True(result.ContainsKey("A"));
            Assert.True(result.ContainsKey("B"));
        }

        [Fact]
        public void Should_handle_single_order()
        {
            List<Order> orders = [new Order("A", 1.0m, "PLN")];
            IReadOnlyDictionary<string, decimal> result = cut.GetTotalAmountPerCustomer(orders);
            Assert.True(result.ContainsKey("A"));
            Assert.Equal(1.0m, result["A"]);
        }

        [Fact]
        public void Should_return_sum_of_amounts_per_customer_while_ignoring_currency()
        {
            IReadOnlyDictionary<string, decimal> result = cut.GetTotalAmountPerCustomer(CreateOrders());
            Assert.Equal(6.0m, result["A"]);
            Assert.Equal(15.0m, result["B"]);
        }

        private List<Order> CreateOrders()
        {
            List<Order> orders =
            [
                new Order("A", 1.0m, "PLN"),
                new Order("A", 2.0m, "EUR"),
                new Order("A", 3.0m, "USD"),
                new Order("B", 4.0m, "PLN"),
                new Order("B", 5.0m, "EUR"),
                new Order("B", 6.0m, "USD"),
            ];

            return orders;
        }
    }
}
