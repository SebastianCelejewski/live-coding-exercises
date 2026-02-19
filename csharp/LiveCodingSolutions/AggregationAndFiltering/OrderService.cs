namespace LiveCodingSolutions.AggregationAndFiltering
{

    public class OrderService
    {
        public IDictionary<string, decimal> GetTotalAmountPerCustomer(IEnumerable<Order> orders)
        {
            if (orders == null)
            {
                return new Dictionary<string, decimal>();
            }

            return orders
                .Where(IsValid)
                .GroupBy(o => o.Customer)
                .ToDictionary(g => g.Key, g => g.Sum(o => o.Amount));
        }

        private bool IsValid(Order order)
        {
            if (order == null)
            {
                return false;
            }

            if (order.Customer == null || order.Currency == null)
            {
                return false;
            }

            return true;
        }

    }
}