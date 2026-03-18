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
                .ToDictionary(g => g.Key, g => g.Sum(o => o.Amount));
        }

        public IReadOnlyDictionary<string, decimal> GetTopCustomersInEUR(IEnumerable<Order> orders, IEnumerable<ExchangeRate> exchangeRates, decimal minTotalEur, int topN)
        {
            ValidateInput(orders, exchangeRates, minTotalEur, topN);
            IDictionary<string, decimal> currencyMapping = exchangeRates.ToDictionary(e => e.Currency, e => e.RateToEur);

            return orders
                .Select(o => new { o.Customer, Amount = o.Amount * currencyMapping[o.Currency] })
                .GroupBy(f => f.Customer)
                .ToDictionary(g => g.Key, g => g.Sum(o => o.Amount))
                .Where(h => h.Value.CompareTo(minTotalEur) > 0)
                .OrderBy(i => i.Value)
                .Reverse()
                .Take(topN)
                .ToDictionary(x => x.Key, x => x.Value);
        }

        private void ValidateInput(IEnumerable<Order> orders, IEnumerable<ExchangeRate> exchangeRates, decimal minTotalEur, int topN)
        {
            ArgumentNullException.ThrowIfNull(orders);
            ArgumentNullException.ThrowIfNull(exchangeRates);
            ArgumentOutOfRangeException.ThrowIfNegative(minTotalEur);
            ArgumentOutOfRangeException.ThrowIfNegative(topN);

            List<string> availableCurrencies = exchangeRates.Select(e => e.Currency).ToList();
            
            Order? invalidOrder = orders.FirstOrDefault(o => !availableCurrencies.Contains(o.Currency));
            if (invalidOrder != null)
            {
                throw new MissingExchangeRateException(invalidOrder.Currency);
            }
        }
    }
}