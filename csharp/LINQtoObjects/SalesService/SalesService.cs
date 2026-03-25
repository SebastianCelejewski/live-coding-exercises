using System;
using System.Collections.Generic;
using System.Linq;

namespace LiveCodingExercises.LINQtoObjects.SalesService
{
    public class SalesService
    {
        /// <summary>
        /// Return top customers per country with sums of their amounts
        /// <para>If country has less than topN customers, return them all</para>
        /// <para>For every customer should return sum of amounts</para>
        /// <para>Must throw ArgumentNullException if list of orders is null</para>
        /// <para>Must throw ArgumentOutOfRangeException if topN is negative</para>
        /// <para>Customers per country must be returned in descending order</para>
        /// </summary>
        /// <param name="orders">list of orders</param>
        /// <param name="topN">maximum numer of customers to return per country</param>
        /// <returns>list of top customers per country with sums of their amounts</returns>
        /// <exception cref="NotImplementedException"></exception>
        public IDictionary<string, IEnumerable<CustomerExpenses>> GetTopCustomersPerCountry(IEnumerable<Order> orders, int topN)
        {
            ArgumentOutOfRangeException.ThrowIfNegative(topN);
            ArgumentNullException.ThrowIfNull(orders);

            return orders
                .GroupBy(o => new { o.Country, o.CustomerId })   // IGrouping<(string, string), Order>
                .Select(g => new { g.Key.Country, g.Key.CustomerId, Sum = g.Sum(o => o.Amount) })  // IEnumerable<(string, string, decimal)>
                .GroupBy(h => h.Country) // IGrouping<string, <(string, string, decimal)>>
                .ToDictionary(
                    i => i.Key,
                    i => i.OrderByDescending(i => i.Sum)
                            .Take(topN)
                            .Select(k => new CustomerExpenses(k.CustomerId, k.Sum)));
        }
    }
}
