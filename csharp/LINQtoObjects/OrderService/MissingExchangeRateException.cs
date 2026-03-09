using System;
using System.Runtime.Serialization;

namespace LiveCodingExercises.LINQtoObjects.OrderService
{
    [Serializable]
    public class MissingExchangeRateException(string currency) : Exception($"Missing exchange rate for currency '{currency}'."), ISerializable
    {
    }
}
