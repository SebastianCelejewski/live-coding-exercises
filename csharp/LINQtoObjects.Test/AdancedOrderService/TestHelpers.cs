using LiveCodingExercises.LINQtoObjects.AdvancedOrderService;
using System.Collections.Generic;

namespace LiveCodingExercises.LINQtoObjects.Test.AdancedOrderService
{
    public static class TestHelpers
    {
        public static IDictionary<string, IEnumerable<CustomerReport>> RunWith(this AdvancedOrderService.AdvancedOrderService cut, GenerateCustomerIntelligenceReportParameters parameters)
        {
            return cut.GenerateCustomerIntelligenceReport(parameters.Orders, parameters.ExchangeRates, parameters.TopN, parameters.MinTotalEur, parameters.MinOrdersCount);
        }
    }
}
