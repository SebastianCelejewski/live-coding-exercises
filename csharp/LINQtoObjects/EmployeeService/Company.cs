using System.Collections.Generic;

namespace LiveCodingExercises.LINQtoObjects.EmployeeService
{
    public record Company(string Name, IList<Department> Departments)
    {
    }
}
