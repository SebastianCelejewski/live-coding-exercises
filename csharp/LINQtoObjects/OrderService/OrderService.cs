using System;
using System.Collections.Generic;
using System.Linq;

namespace LiveCodingExercises.LINQtoObjects.OrderService
{
    public class OrderService
    {
        public IReadOnlyDictionary<string, decimal> GetTotalAmountPerCustomer(IEnumerable<Order> orders)
        {
            ArgumentNullException.ThrowIfNull(orders);
            return orders
                .GroupBy(o => o.Customer)
                .ToDictionary(x => x.Key, x => x.Sum(o => o.Amount));
        }

        public IReadOnlyDictionary<string, decimal> GetTopCustomersInEUR(IEnumerable<Order> orders, IEnumerable<ExchangeRate> exchangeRates, decimal minTotalEur, int topN)
        {
            ValidateInput(orders, exchangeRates, minTotalEur, topN);
            IDictionary<string, decimal> exchangeRatesMap = exchangeRates.ToDictionary(e => e.Currency, e => e.RateToEur);
            return orders
                .GroupBy(o => o.Customer)
                .Select(x => new { Customer = x.Key, Total = x.Sum(o => o.Amount * exchangeRatesMap[o.Currency]) })
                .Where(y => y.Total > minTotalEur)
                .OrderByDescending(y => y.Total)
                .Take(topN)
                .ToDictionary(y => y.Customer, y => y.Total);
        }

        private void ValidateInput(IEnumerable<Order> orders, IEnumerable<ExchangeRate> exchangeRates, decimal minTotalEur, int topN)
        {
            ArgumentNullException.ThrowIfNull(orders);
            ArgumentNullException.ThrowIfNull(exchangeRates);
            ArgumentOutOfRangeException.ThrowIfNegative(minTotalEur);
            ArgumentOutOfRangeException.ThrowIfNegative(topN);
            IDictionary<string, decimal> exchangeRatesMap = exchangeRates.ToDictionary(e => e.Currency, e => e.RateToEur);
            foreach (var order in orders)
            {
                if (!exchangeRatesMap.ContainsKey(order.Currency))
                {
                    throw new MissingExchangeRateException(order.Currency);
                }
            }
        }
    }
}