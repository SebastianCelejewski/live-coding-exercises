using System.Runtime.Serialization;

namespace LiveCodingSolutions.AggregationAndFiltering
{
    [Serializable]
    public class MissingExchangeRateException(string currency) : Exception($"Missing exchange rate for currency '{currency}'."), ISerializable
    {
    }
}
