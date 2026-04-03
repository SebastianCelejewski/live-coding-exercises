using System;

namespace LiveCodingExercises.LINQtoObjects.AdvancedOrderService
{
    public class MissingExchangeRateException : Exception
    {
        public MissingExchangeRateException(string currency) : base(currency) { }
    }
}
