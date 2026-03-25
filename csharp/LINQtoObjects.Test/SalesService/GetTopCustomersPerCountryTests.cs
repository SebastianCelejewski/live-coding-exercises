using LiveCodingExercises.LINQtoObjects.SalesService;
using System;
using System.Collections.Generic;
using System.Linq;
using Xunit;

namespace LiveCodingExercises.LINQtoObjects.Test.SalesService
{
    public class GetTopCustomersPerCountryTests
    {
        private LINQtoObjects.SalesService.SalesService cut = new LINQtoObjects.SalesService.SalesService();

        [Fact]
        public void Should_throw_ArgumentOutOfRangeException_if_topN_is_negative()
        {
            Assert.Throws<ArgumentOutOfRangeException>(() =>
            {
                cut.GetTopCustomersPerCountry(CreateOrders(), -5);
            });
        }

        [Fact]
        public void Should_return_data_for_all_countries_present_in_input_data()
        {
            IDictionary<string, IEnumerable<CustomerExpenses>> result = cut.GetTopCustomersPerCountry(CreateOrders(), 99);
            AssertThatResultContainsExactlyThoseCountries(result, "PL", "DE");
        }

        [Fact]
        public void Should_return_top_customers_per_single_country()
        {
            IDictionary<string, IEnumerable<CustomerExpenses>> result = cut.GetTopCustomersPerCountry(CreateOrders(), 99);
            AssertThatResultContainsProperCustomersPerCountry(result["PL"], "A", "B", "C");
            AssertThatResultContainsProperCustomersPerCountry(result["DE"], "X", "Y", "Z");
        }

        [Fact]
        public void Should_return_sum_of_amounts_for_every_customer()
        {
            IDictionary<string, IEnumerable<CustomerExpenses>> result = cut.GetTopCustomersPerCountry(CreateOrders(), 99);
            AssertThatCustomerContainsSumOfAmounts(result["PL"], [
                ( "A", 150m ),
                ( "B", 200m ),
                ( "C", 10m)
            ]);
            AssertThatCustomerContainsSumOfAmounts(result["DE"], [
                ( "X", 300m ),
                ( "Y", 100m ),
                ( "Z", 200m)
            ]);
        }

        [Fact]
        public void Should_return_max_topN_customers_per_country()
        {
            IDictionary<string, IEnumerable<CustomerExpenses>> result = cut.GetTopCustomersPerCountry(CreateOrders(), 2);
            foreach (IEnumerable<CustomerExpenses> customerExpenses in result.Values)
            {
                Assert.True(customerExpenses.Count() <= 2);
            }
        }

        [Fact]
        public void Should_return_customers_sorted_by_sum_of_amounts_descending()
        {
            IDictionary<string, IEnumerable<CustomerExpenses>> result = cut.GetTopCustomersPerCountry(CreateOrders(), 99);
            foreach (IEnumerable<CustomerExpenses> customerExpenses in result.Values)
            {
                AssertThatOrderIsDescendingBySumOfAmounts(customerExpenses);
            }
        }

        private IEnumerable<Order> CreateOrders()
        {
            return [
                new Order("A", "PL", 100m),
                new Order("A", "PL", 50m),
                new Order("B", "PL", 200m),
                new Order("C", "PL", 10m),
                new Order("X", "DE", 300m),
                new Order("Y", "DE", 100m),
                new Order("Z", "DE", 200m)
                ];
        }

        private void AssertThatResultContainsExactlyThoseCountries(IDictionary<string, IEnumerable<CustomerExpenses>> result, params string[] countries)
        {
            Assert.Equal(countries.Length, result.Keys.Count);
            foreach (string country in countries)
            {
                Assert.Contains(country, result.Keys);
            }
        }

        private void AssertThatResultContainsProperCustomersPerCountry(IEnumerable<CustomerExpenses> customerExpenses, params string[] customerIds)
        {
            IDictionary<string, decimal> customers = customerExpenses.ToDictionary(ce => ce.customerId, ce => ce.sumOfAmounts);
            Assert.Equal(customerIds.Length, customers.Keys.Count);
            foreach (string customerId in customerIds)
            {
                Assert.Contains(customerId, customers.Keys);
            }
        }

        private void AssertThatCustomerContainsSumOfAmounts(IEnumerable<CustomerExpenses> customerExpenses, IList<(string, decimal)> expectedSums)
        {
            IDictionary<string, decimal> actualExpenses = customerExpenses.ToDictionary(ce => ce.customerId, ce => ce.sumOfAmounts);
            foreach (var expense in expectedSums)
            {
                var (customerId, expectedValue) = expense;
                Assert.Equal(expectedValue, actualExpenses[customerId]);
            }
        }

        private void AssertThatOrderIsDescendingBySumOfAmounts(IEnumerable<CustomerExpenses> customerExpenses)
        {
            IList<decimal> sums = customerExpenses.Select(ce => ce.sumOfAmounts).ToList();
            for (int i = 1; i < sums.Count; i++)
            {
                Assert.True(sums[i] <= sums[i - 1]);
            }
        }
    }
}
