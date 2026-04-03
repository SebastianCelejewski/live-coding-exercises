using System;

namespace LiveCodingExercises.LINQtoObjects.AdvancedOrderService
{
    public record CustomerReport(string customerId, decimal totalEUR, int ordersCount, DateTime lastOrderTimestamp)
    {
    }
}
