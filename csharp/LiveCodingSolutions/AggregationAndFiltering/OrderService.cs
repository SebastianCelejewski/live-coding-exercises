using System.Collections.Immutable;

namespace LiveCodingSolutions.AggregationAndFiltering
{

    public class OrderService
    {
        public IReadOnlyDictionary<string, decimal> GetTotalAmountPerCustomer(IEnumerable<Order> orders)
        {
            if (orders == null)
            {
                return new Dictionary<string, decimal>();
            }

            return orders
                .GroupBy(o => o.Customer)
                .ToDictionary(g => g.Key, g => g.Sum(o => o.Amount));
        }

        public IReadOnlyDictionary<string, decimal> GetTopCustomersInEUR(IEnumerable<Order> orders, IEnumerable<ExchangeRate> exchangeRates, decimal minTotalEur, int topN)
        {
            ArgumentNullException.ThrowIfNull(orders);
            ArgumentNullException.ThrowIfNull(exchangeRates);
            ArgumentOutOfRangeException.ThrowIfNegative(minTotalEur);
            ArgumentOutOfRangeException.ThrowIfNegative(topN);

            Dictionary<string, decimal> requiredExchangeRates = exchangeRates.ToDictionary(x => x.Currency, x => x.RateToEur);

            return orders
                .Select(x => ConvertExchangeRate(x, requiredExchangeRates))
                .GroupBy(x => x.Customer)
                .Select(g => new { Customer = g.Key, Total = g.Sum(o => o.Amount)})
                .Where(x => x.Total >= minTotalEur)
                .OrderByDescending(x => x.Total)
                .Take(topN)
                .ToDictionary(x => x.Customer, x => x.Total);
        }

        private Order ConvertExchangeRate(Order order, Dictionary<string, decimal> exchangeRates)
        {
            if (!exchangeRates.TryGetValue(order.Currency, out var rate))
            {
                throw new MissingExchangeRateException(order.Currency);
            }

            return new Order(order.Customer, order.Amount * rate, "EUR");
        }
    }
}