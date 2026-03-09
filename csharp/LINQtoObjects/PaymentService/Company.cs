using System.Collections.Generic;

namespace LiveCodingExercises.LINQtoObjects.PaymentService
{
    public record Company(string Name, IList<Department> Departments)
    {
    }
}
