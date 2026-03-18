using System.Collections.Generic;

namespace LiveCodingExercises.LINQtoObjects.EmployeeService
{
    public record Department(string Name, IList<Employee> Employees)
    {
    }
}
