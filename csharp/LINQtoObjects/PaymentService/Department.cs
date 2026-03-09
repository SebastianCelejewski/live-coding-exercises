using System.Collections.Generic;

namespace LiveCodingExercises.LINQtoObjects.PaymentService
{
    public record Department(string Name, IList<Employee> Employees)
    {
    }
}
