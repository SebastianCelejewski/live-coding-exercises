using LiveCodingExercises.LINQtoObjects.AdvancedOrderService;
using System;
using static LiveCodingExercises.LINQtoObjects.Test.AdancedOrderService.GenerateCustomerIntelligenceReportParameters;
using Xunit;
using System.Collections.Generic;
using System.Linq;

namespace LiveCodingExercises.LINQtoObjects.Test.AdancedOrderService
{
    public class GenerateCustomerIntelligenceReportTests
    {
        private static AdvancedOrderService.AdvancedOrderService cut = new AdvancedOrderService.AdvancedOrderService();

        [Fact]
        public void Should_throw_ArgumentOutOfRangeException_if_topN_is_negative()
        {
            Assert.Throws<ArgumentOutOfRangeException>(() => cut.RunWith(NegativeTopN));
        }

        [Fact]
        public void Should_throw_ArgumentOutOfRangeException_if_minTotalEUR_is_negative()
        {
            Assert.Throws<ArgumentOutOfRangeException>(() => cut.RunWith(NegativeTotalEUR));
        }

        [Fact]
        public void Should_throw_ArgumentOutOfRangeException_if_minOrdersCount_is_negative()
        {
            Assert.Throws<ArgumentOutOfRangeException>(() => cut.RunWith(NegativeOrdersCount));
        }

        [Fact]
        public void Should_throw_MissingExchangeRateException_if_order_contains_currency_not_present_in_exchange_rates_dictionary()
        {
            Assert.Throws<MissingExchangeRateException>(() => cut.RunWith(EmptyExchangeRates));
        }

        [Fact]
        public void Should_return_list_of_reports_for_every_country_present_in_input_data()
        {
            IDictionary<string, IEnumerable<CustomerReport>> result = cut.RunWith(DefaultInputValues);
            AssertThatResultContainsSpecificCountries(result, "PL", "DE");
        }

        [Fact]
        public void Customer_report_should_contain_the_sum_of_all_amounts_for_this_customer_in_EUR() {
            IDictionary<string, IEnumerable<CustomerReport>> result = cut.RunWith(DefaultInputValues);
            AssertThatResultContainSpecificSumOfAmounts(result, "PL", "A", 15.00m);
            AssertThatResultContainSpecificSumOfAmounts(result, "PL", "B", 22.50m);
            AssertThatResultContainSpecificSumOfAmounts(result, "PL", "C", 40.00m);
            AssertThatResultContainSpecificSumOfAmounts(result, "DE", "X", 100.00m);
            AssertThatResultContainSpecificSumOfAmounts(result, "DE", "Y", 180.00m);
            AssertThatResultContainSpecificSumOfAmounts(result, "DE", "Z", 290.00m);
        }

        [Fact]
        public void Customer_report_should_contain_the_datetime_of_the_most_recent_customer_order()
        {
            IDictionary<string, IEnumerable<CustomerReport>> result = cut.RunWith(DefaultInputValues);
            AssertThatResultContainSpecificLastOperationTimestamp(result, "PL", "A", DateTime.Parse("2025-01-03 12:00:00"));
            AssertThatResultContainSpecificLastOperationTimestamp(result, "PL", "B", DateTime.Parse("2025-02-04 12:00:00"));
            AssertThatResultContainSpecificLastOperationTimestamp(result, "PL", "C", DateTime.Parse("2025-01-06 12:00:00"));
            AssertThatResultContainSpecificLastOperationTimestamp(result, "DE", "X", DateTime.Parse("2025-01-10 12:00:00"));
            AssertThatResultContainSpecificLastOperationTimestamp(result, "DE", "Y", DateTime.Parse("2025-02-12 12:00:00"));
            AssertThatResultContainSpecificLastOperationTimestamp(result, "DE", "Z", DateTime.Parse("2025-01-15 12:00:00"));
        }

        [Fact]
        public void Customer_report_should_contain_the_number_of_all_orders_for_this_customer()
        {
            IDictionary<string, IEnumerable<CustomerReport>> result = cut.RunWith(DefaultInputValues);
            AssertThatResultContainSpecificNumberOfOrders(result, "PL", "A", 3);
            AssertThatResultContainSpecificNumberOfOrders(result, "PL", "B", 2);
            AssertThatResultContainSpecificNumberOfOrders(result, "PL", "C", 1);
            AssertThatResultContainSpecificNumberOfOrders(result, "DE", "X", 4);
            AssertThatResultContainSpecificNumberOfOrders(result, "DE", "Y", 3);
            AssertThatResultContainSpecificNumberOfOrders(result, "DE", "Z", 2);

        }

        [Fact]
        public void List_of_reports_should_contain_at_most_topN_reports()
        {
            IDictionary<string, IEnumerable<CustomerReport>> result = cut.RunWith(TopNSetTo(2));
            AssertThatResultContainsSpecificCustomers(result, "PL", "B", "C");
            AssertThatResultContainsSpecificCustomers(result, "DE", "Y", "Z");
        }

        [Fact]
        public void List_of_reports_should_contain_data_of_customers_with_most_total_amounts()
        {
            IDictionary<string, IEnumerable<CustomerReport>> result = cut.RunWith(DefaultInputValues);
            AssertThatResultContainsSpecificCustomers(result, "PL", "A", "B", "C");
            AssertThatResultContainsSpecificCustomers(result, "DE", "X", "Y", "Z");
        }

        [Fact]
        public void Customer_reports_should_be_sorted_by_total_amounts_decreasing()
        {
            IDictionary<string, IEnumerable<CustomerReport>> result = cut.RunWith(DefaultInputValues);
            AssertThatReportsAreSortedByTotalAmountDecreasing(result, "PL");
            AssertThatReportsAreSortedByTotalAmountDecreasing(result, "DE");
        }

        [Fact]
        public void Customer_reports_should_not_contain_data_for_customers_who_had_less_orders_than_minOrdersCount()
        {
            IDictionary<string, IEnumerable<CustomerReport>> result = cut.RunWith(MinOrdersCountSetTo(3));
            AssertThatResultContainsSpecificCustomers(result, "PL", "A");
            AssertThatResultContainsSpecificCustomers(result, "DE", "X", "Y");
        }

        [Fact]
        public void Customer_reports_should_not_contain_data_for_customers_whose_total_amount_was_less_than_minTotalEUR()
        {
            IDictionary<string, IEnumerable<CustomerReport>> result = cut.RunWith(MinTotalEURSetTo(200));
            AssertThatResultContainsNoCustomers(result, "PL");
            AssertThatResultContainsSpecificCustomers(result, "DE", "Z");
        }

        [Fact]
        public void TheSameListOfCustomersShouldBeReturnedIfTotalAmountsAreTheSame()
        {
            IDictionary<string, IEnumerable<CustomerReport>> result1 = cut.RunWith(CustomersWithSameTotalAmounts);

            var reversedOrders = CustomersWithSameTotalAmounts.Orders.Reverse();

            IDictionary<string, IEnumerable<CustomerReport>> result2 = cut.RunWith(SpecificOrders(reversedOrders));

            string customerIds1 = string.Join("-", result1.Values.First().Select(x => x.customerId));
            string customerIds2 = string.Join("-", result2.Values.First().Select(x => x.customerId));

            Assert.Equal(customerIds1, customerIds2);
        }

        private void AssertThatResultContainsSpecificCountries(IDictionary<string, IEnumerable<CustomerReport>> result, params string[] countries)
        {
            Assert.Equal(countries.Length, result.Keys.Count);
            foreach (string country in countries)
            {
                Assert.Contains(country, result.Keys);
            }
        }

        private void AssertThatResultContainSpecificSumOfAmounts(IDictionary<string, IEnumerable<CustomerReport>> result, string country, string customerId, decimal expectedSum)
        {
            Assert.Equal(expectedSum, result[country].First(x => x.customerId == customerId).totalEUR);
        }

        private void AssertThatResultContainSpecificLastOperationTimestamp(IDictionary<string, IEnumerable<CustomerReport>> result, string country, string customerId, DateTime expectedTimeStamp)
        {
            Assert.Equal(expectedTimeStamp, result[country].First(x => x.customerId == customerId).lastOrderTimestamp);
        }

        private void AssertThatResultContainSpecificNumberOfOrders(IDictionary<string, IEnumerable<CustomerReport>> result, string country, string customerId, int expectedNumberOfOrders)
        {
            Assert.Equal(expectedNumberOfOrders, result[country].First(x => x.customerId == customerId).ordersCount);
        }
        private void AssertThatResultContainsSpecificCustomers(IDictionary<string, IEnumerable<CustomerReport>> result, string country, params string[] customerIds)
        {
            Assert.Equal(customerIds.Length, result[country].Count());
            foreach (var customerId in customerIds)
            {
                Assert.Contains(customerId, result[country].Select(x => x.customerId));
            }
        }

        private void AssertThatResultContainsNoCustomers(IDictionary<string, IEnumerable<CustomerReport>> result, string country)
        {
            Assert.Empty(result[country]);
        }

        private void AssertThatReportsAreSortedByTotalAmountDecreasing(IDictionary<string, IEnumerable<CustomerReport>> result, string country)
        {
            List<CustomerReport> reports = result[country].ToList();
            
            for (int i = 0; i < reports.Count() - 1; i++) {
                Assert.True(reports[i + 1].totalEUR <= reports[i].totalEUR);
            }
        }
    }
}
