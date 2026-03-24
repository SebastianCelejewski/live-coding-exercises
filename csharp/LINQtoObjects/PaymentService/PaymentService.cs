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
            ValidateInput(companies);
            ArgumentOutOfRangeException.ThrowIfNegative(topN);

            return companies
                .ToDictionary(
                    c => c.Name,
                    c => c.Departments
                            .SelectMany(d => d.Employees)
                            .OrderByDescending(e => e.Salary)
                            .Take(topN));
        }

        public IDictionary<string, Employee> GetHighestPaidEmployeePerCompany(IEnumerable<Company> companies)
        {
            ValidateInput(companies);
            return companies
                .GroupBy(c => c.Name)
                .Select(g => new { CompanyName = g.Key, Employee = g.SelectMany(c => c.Departments).SelectMany(d => d.Employees).MaxBy(e => e.Salary) })
                .Where(r => r.Employee != null)
                .ToDictionary(r => r.CompanyName, r => r.Employee!);
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
    }
}
