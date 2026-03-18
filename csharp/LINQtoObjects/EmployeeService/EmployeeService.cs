using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;

namespace LiveCodingExercises.LINQtoObjects.EmployeeService
{
    public class EmployeeService
    {
        public IDictionary<string, IEnumerable<Employee>> GetMostFrequentEmployeesPerCompany(IEnumerable<Company> companies)
        {
            ValidateInput(companies);
            return companies
                        .SelectMany(c => c.Departments, (c, d) => new { Company = c, Department = d })
                        .SelectMany(cd => cd.Department.Employees, (cd, e) => new { Company = cd.Company, Employee = e })
                        .GroupBy(g => g.Company)
                        .ToDictionary(x => x.Key.Name, x => GetMostFrequentEmployeesPerSingleCompany(x.Select(v => v.Employee)));
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
            IDictionary<Employee, int> counts = employees.GroupBy(e => e).ToDictionary(kv => kv.Key, kv => kv.Sum(e => 1));
            int max = counts.Max(kv => kv.Value);
            return counts.Where(c => c.Value == max).Select(c => c.Key);
        }
    }
}
