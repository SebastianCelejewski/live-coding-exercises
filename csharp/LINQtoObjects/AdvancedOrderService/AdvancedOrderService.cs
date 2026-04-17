using LiveCodingExercises.LINQtoObjects.OrderService;
using System;
using System.Collections.Generic;
using System.Linq;

namespace LiveCodingExercises.LINQtoObjects.AdvancedOrderService
{
    public class AdvancedOrderService
    {

        /// <summary>
        /// Creates customer report for all countries
        /// <para>Returned data contains a list of consumer report for every country</para>
        /// <para>List of consumer reports contains data for consumers for which total amount is greater than minTotalEUR and and number of orders is greater than minOrdersCount</para>
        /// <para>List of consumer reports for a single country returns data for at most topN consumers</para>
        /// <para>If an order contains currency that is not present in exchangeRates data, an exception is be thrown</para>
        /// <para>List of consumer reports is sorted by total amounts descending</para>
        /// <para>If two consumers have the same total amount, their order is be consistent, i.e.it does not matter which goes first on the list, but but this order is be preserved between method calls</para>
        /// <para>Consumer report contains the time stamp of the last operation for given customer</para>
        /// <para>An exception is thrown if data is invalid, e.g. order is null or any value inside the order is null</para>
        /// <para>CustomerReport: string customerId, decimal totalEUR, int ordersCount, DateTime lastOrderTimestamp</para>
        /// </summary>
        /// <param name="orders">list of orders as an input</param>
        /// <param name="exchangeRates">mapping between currencies and their rate to EUR</param>
        /// <param name="topN">maximum number of customers to return for a single country</param>
        /// <param name="minTotalEUR">minimal total amount for a customer to be included in a report</param>
        /// <param name="minOrdersCount">minimal number of orders for a customer to be included in a report</param>
        /// <returns></returns>
        /// <exception cref="NotImplementedException"></exception>
        public IDictionary<string, IEnumerable<CustomerReport>> GenerateCustomerIntelligenceReport(
            IEnumerable<Order> orders,
            IDictionary<string, decimal> exchangeRates,
            int topN,
            decimal minTotalEUR,
            int minOrdersCount)
        {
            ValidateInput(orders, exchangeRates, topN, minTotalEUR, minOrdersCount);
            return orders                                                   // IEnumerable<Order>
                    .GroupBy(order => order.country)                        // IGrouping<string, IEnumerable<Order>>, where Order is for a single country, but multiple customers
                    .ToDictionary(                                          // IDictionary<string, IEnumerable<Order>>, where Order is for a single country, but multiple customers
                        g => g.Key,
                        g => g.GroupBy(order => order.customerId)
                              .Select(x => x.Aggregate(
                                                new Accumulator(exchangeRates),
                                                (acc, element) => acc.AddOrder(element),
                                                (acc) => acc.ToCustomerReport()
                                                ))
                              .Where(cr => cr.totalEUR >= minTotalEUR)
                              .Where(cr => cr.ordersCount >= minOrdersCount)
                              .OrderByDescending(cr => cr.totalEUR)
                              .ThenBy(cr => cr.customerId)
                              .Take(topN)
                    );
        }

        private class Accumulator
        {
            private IDictionary<string, decimal> exchangeRates;

            private string customerId = string.Empty;
            private decimal totalAmount = 0.00m;
            private int orderCount = 0;
            private DateTime lastOperationTimestamp = DateTime.MinValue;

            public Accumulator(IDictionary<string, decimal> exchangeRates)
            {
                this.exchangeRates = exchangeRates;
            }

            public Accumulator AddOrder(Order order)
            {
                customerId = order.customerId;
                totalAmount += order.amount * exchangeRates[order.currency];
                orderCount++;
                if (order.timestamp.CompareTo(lastOperationTimestamp) > 0)
                {
                    lastOperationTimestamp = order.timestamp;
                }
                return this;
            }

            public CustomerReport ToCustomerReport()
            {
                return new CustomerReport(customerId, totalAmount, orderCount, lastOperationTimestamp);
            }
        }

        private void ValidateInput(IEnumerable<Order> orders, IDictionary<string, decimal> exchangeRates, int topN, decimal minTotalEUR, int minOrdersCount)
        {
            ArgumentOutOfRangeException.ThrowIfNegative(topN);
            ArgumentOutOfRangeException.ThrowIfNegative(minOrdersCount);
            ArgumentOutOfRangeException.ThrowIfNegative(minTotalEUR);

            foreach (var order in orders)
            {
                if (!exchangeRates.ContainsKey(order.currency))
                {
                    throw new MissingExchangeRateException(order.currency);
                }
            }
        }
    }
}
