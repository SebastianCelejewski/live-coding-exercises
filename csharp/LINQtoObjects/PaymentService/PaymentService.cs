using System;
using System.Collections.Generic;
using System.Linq;

namespace LiveCodingExercises.LINQtoObjects.PaymentService
{
    public class PaymentService
    {
        /// <summary>
        /// Returns list of top N employees with the highest salary per company 
        /// </summary>
        /// 
        /// <param name="companies">list of companies with their employees per department</param>
        /// <param name="topN">number of employees with the highest salary to return per company</param>
        /// <returns>list of top N employees with the highest salary per company</returns>
        /// <exception cref="NotImplementedException"></exception>
        public IDictionary<string, IEnumerable<Employee>> GetTopPaidEmployeesPerCompany(IEnumerable<Company> companies, int topN)
        {
            ValidateInput(companies, topN);
            return companies
                        .SelectMany(c => c.Departments, (c, d) => new { Company = c, Department = d })
                        .SelectMany(cd => cd.Department.Employees, (cd, e) => new { cd.Company, Employee = e })
                        .GroupBy(g => g.Company.Name)
                        .ToDictionary(kv => kv.Key, kv => GetTopPaidEmployeesPerSingleCompany(kv.Select(x => x.Employee), topN));
        }

        private IEnumerable<Employee> GetTopPaidEmployeesPerSingleCompany(IEnumerable<Employee> employees, int topN)
        {
            return employees.OrderBy(o => o.Salary).Reverse().Take(topN);
        }

        private void ValidateInput(IEnumerable<Company> companies, int topN)
        {
            ArgumentNullException.ThrowIfNull(companies);
            ArgumentOutOfRangeException.ThrowIfNegative(topN);
            foreach (Company c in companies)
            {
                ArgumentNullException.ThrowIfNull(c.Departments);
                foreach (Department d in c.Departments)
                {
                    ArgumentNullException.ThrowIfNull(d.Employees);
                }
            }
        }
    }
}
