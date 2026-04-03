using System;
using System.Collections.Generic;
using System.Linq;
using LiveCodingExercises.LINQtoObjects.AdvancedOrderService;

namespace LiveCodingExercises.LINQtoObjects.Test.AdancedOrderService
{

    public record GenerateCustomerIntelligenceReportParameters(IEnumerable<Order> Orders, IDictionary<string, decimal> ExchangeRates, int TopN, decimal MinTotalEur, int MinOrdersCount)
    {
        public static GenerateCustomerIntelligenceReportParameters DefaultInputValues => new(DefaultOrders, DefaultExchangeRates, NoTopN, NoMinTotalEUR, NoMinOrdersCount);

        public static GenerateCustomerIntelligenceReportParameters CustomersWithSameTotalAmounts => new(OrdersWithTheSameAmount, DefaultExchangeRates, NoTopN, NoMinTotalEUR, NoMinOrdersCount);

        public static GenerateCustomerIntelligenceReportParameters SpecificOrders(IEnumerable<Order> orders) => new(orders, DefaultExchangeRates, NoTopN, NoMinTotalEUR, NoMinOrdersCount);

        public static GenerateCustomerIntelligenceReportParameters NegativeTopN => DefaultInputValues with { TopN = -1 };

        public static GenerateCustomerIntelligenceReportParameters NegativeTotalEUR => DefaultInputValues with { MinTotalEur = -1 };
        
        public static GenerateCustomerIntelligenceReportParameters NegativeOrdersCount => DefaultInputValues with { MinOrdersCount = -1 };

        public static GenerateCustomerIntelligenceReportParameters EmptyExchangeRates => DefaultInputValues with { ExchangeRates = EmptyExchangeRatesDictionary };

        public static GenerateCustomerIntelligenceReportParameters TopNSetTo(int topN) => DefaultInputValues with { TopN = topN };

        public static GenerateCustomerIntelligenceReportParameters MinOrdersCountSetTo(int minOrdersCount) => DefaultInputValues with { MinOrdersCount = minOrdersCount };

        public static GenerateCustomerIntelligenceReportParameters MinTotalEURSetTo(decimal minTotalEUR) => DefaultInputValues with { MinTotalEur = minTotalEUR};

        private static IEnumerable<Order> DefaultOrders = new List<Order>() {
            new Order("A", "PL", 10.00m, "PLN", DateTime.Parse("2025-01-01 12:00:00")),
            new Order("A", "PL", 20.00m, "PLN", DateTime.Parse("2025-01-02 12:00:00")),
            new Order("A", "PL", 30.00m, "PLN", DateTime.Parse("2025-01-03 12:00:00")),
            new Order("B", "PL", 40.00m, "PLN", DateTime.Parse("2025-02-04 12:00:00")),
            new Order("B", "PL", 50.00m, "PLN", DateTime.Parse("2025-01-05 12:00:00")),
            new Order("C", "PL", 160.00m, "PLN", DateTime.Parse("2025-01-06 12:00:00")),
            new Order("X", "DE", 10.00m, "EUR", DateTime.Parse("2025-01-07 12:00:00")),
            new Order("X", "DE", 20.00m, "EUR", DateTime.Parse("2025-01-08 12:00:00")),
            new Order("X", "DE", 30.00m, "EUR", DateTime.Parse("2025-01-09 12:00:00")),
            new Order("X", "DE", 40.00m, "EUR", DateTime.Parse("2025-01-10 12:00:00")),
            new Order("Y", "DE", 50.00m, "EUR", DateTime.Parse("2025-01-11 12:00:00")),
            new Order("Y", "DE", 60.00m, "EUR", DateTime.Parse("2025-02-12 12:00:00")),
            new Order("Y", "DE", 70.00m, "EUR", DateTime.Parse("2025-01-13 12:00:00")),
            new Order("Z", "DE", 180.00m, "EUR", DateTime.Parse("2025-01-14 12:00:00")),
            new Order("Z", "DE", 110.00m, "EUR", DateTime.Parse("2025-01-15 12:00:00")),
        };

        private static IEnumerable<Order> OrdersWithTheSameAmount = new List<Order>()
        {
            new Order("A", "PL", 10.00m, "PLN", DateTime.Parse("2025-01-01 12:00:00")),
            new Order("B", "PL", 10.00m, "PLN", DateTime.Parse("2025-01-01 12:00:00")),
            new Order("C", "PL", 10.00m, "PLN", DateTime.Parse("2025-01-01 12:00:00")),
            new Order("D", "PL", 10.00m, "PLN", DateTime.Parse("2025-01-01 12:00:00")),
            new Order("E", "PL", 10.00m, "PLN", DateTime.Parse("2025-01-01 12:00:00")),
            new Order("F", "PL", 10.00m, "PLN", DateTime.Parse("2025-01-01 12:00:00")),
            new Order("G", "PL", 10.00m, "PLN", DateTime.Parse("2025-01-01 12:00:00")),
            new Order("H", "PL", 10.00m, "PLN", DateTime.Parse("2025-01-01 12:00:00")),
        };

        private static IDictionary<string, decimal> DefaultExchangeRates = new Dictionary<string, decimal>()
        {
            { "EUR", 1.00m },
            { "USD", 0.50m },
            { "PLN", 0.25m }
        };

        private static IDictionary<string, decimal> EmptyExchangeRatesDictionary = new Dictionary<string, decimal>();

        private const int NoTopN = int.MaxValue;

        private const decimal NoMinTotalEUR = 0.00m;

        private const int NoMinOrdersCount = 0;
    }
}
