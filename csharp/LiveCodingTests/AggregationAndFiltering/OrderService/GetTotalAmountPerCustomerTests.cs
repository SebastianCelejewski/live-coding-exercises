using LiveCodingSolutions.AggregationAndFiltering;
using Xunit;

namespace LiveCodingTests.AggregationAndFiltering.OrderService
{
    public class GetTotalAmountPerCustomerTests
    {
        private LiveCodingSolutions.AggregationAndFiltering.OrderService cut = new LiveCodingSolutions.AggregationAndFiltering.OrderService();

        [Fact]
        public void Should_never_return_null()
        {
            Assert.NotNull(cut.GetTotalAmountPerCustomer(null));
        }

        [Fact]
        public void Should_return_data_for_all_customers()
        {
            IDictionary<string, decimal> result = cut.GetTotalAmountPerCustomer(CreateOrders());
            Assert.True(result.ContainsKey("A"));
        }

        [Fact]
        public void Should_handle_single_order()
        {
            List<Order> orders = new List<Order>();
            orders.Add(new Order("A", 1.0m, "PLN"));
            IDictionary<string, decimal> result = cut.GetTotalAmountPerCustomer(orders);
            Assert.True(result.ContainsKey("A"));
            Assert.Equal(1.0m, result["A"]);
        }

        [Fact]
        public void Should_return_sum_of_amounts_per_customer_while_ignoring_currency()
        {
            IDictionary<string, decimal> result = cut.GetTotalAmountPerCustomer(CreateOrders());
            Assert.Equal(6.0m, result["A"]);
            Assert.Equal(15.0m, result["B"]);
        }

        [Fact]
        public void Should_ignore_null_orders()
        {
            IList<Order> orders = CreateOrders();
            orders.Add(null);
            IDictionary<string, decimal> result = cut.GetTotalAmountPerCustomer(orders);
            Assert.Equal(6.0m, result["A"]);
            Assert.Equal(15.0m, result["B"]);
        }
        
        [Fact]
        public void Should_ignore_orders_with_null_empty_customer()
        {
            IList<Order> orders = CreateOrders();
            orders.Add(new Order(Customer: null, 1.0m, "PLN"));
            IDictionary<string, decimal> result = cut.GetTotalAmountPerCustomer(orders);
            Assert.Equal(6.0m, result["A"]);
            Assert.Equal(15.0m, result["B"]);
        }

        [Fact]
        public void Should_ignore_orders_with_null_currency()
        {
            IList<Order> orders = CreateOrders();
            orders.Add(new Order("A", 1.0m, null));
            IDictionary<string, decimal> result = cut.GetTotalAmountPerCustomer(orders);
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
