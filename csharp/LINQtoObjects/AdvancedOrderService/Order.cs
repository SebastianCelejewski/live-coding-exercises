using System;

namespace LiveCodingExercises.LINQtoObjects.AdvancedOrderService
{
    public record Order(string customerId, string country, decimal amount, string currency, DateTime timestamp)
    {

    }
}
