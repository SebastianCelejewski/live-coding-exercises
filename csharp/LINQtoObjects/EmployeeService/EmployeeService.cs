using System;
using System.Collections.Generic;
using System.Linq;

namespace LiveCodingExercises.LINQtoObjects.EmployeeService
{
    public class EmployeeService
    {
        public IDictionary<string, IEnumerable<Employee>> GetMostFrequentEmployeesPerCompany(IEnumerable<Company> companies)
        {
            ValidateInput(companies);
            return companies.ToDictionary(c => c.Name, c => GetMostFrequentEmployeesPerSingleCompany(c.Departments.SelectMany(d => d.Employees)));
        }

        private void ValidateInput(IEnumerable<Company> companies)
        {
            ArgumentNullException.ThrowIfNull(companies);
            foreach (Company c in companies)
            {
                ArgumentNullException.ThrowIfNull(c.Departments);
                foreach (Department d in c.Departments)
                {
                    ArgumentNullException.ThrowIfNull(d.Employees);
                }
            }
        }
        
        private IEnumerable<Employee> GetMostFrequentEmployeesPerSingleCompany(IEnumerable<Employee> employees)
        {
            IDictionary<Employee, int> counts = employees.GroupBy(e => e).ToDictionary(e => e.Key, e => e.Count());
            int max = counts.Values.Max();
            return counts.Where(e => e.Value == max).Select(e => e.Key).ToList();
        }
    }
}
