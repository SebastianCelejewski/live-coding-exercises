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
            ArgumentOutOfRangeException.ThrowIfNegative(topN);

            // c: Company
            // d: Department
            // r: {string Name, Department d}
            // e: Employee
            // x: {string Name, Employee e}
            // y: IGrouping<string, {string Name, Employee e}>
            // z: IGrouping<string, Employee>
            return companies
                .SelectMany(c => c.Departments, (c, d) => new { c.Name, Department = d })    
                .SelectMany(r => r.Department.Employees, (r, e) => new { r.Name, Employee = e })    
                .GroupBy(x => x.Name)                                           
                .ToDictionary(y => y.Key, y => y
                    .Select(z => z.Employee)
                    .OrderByDescending(e => e.Salary)
                    .Take(topN));             
        }
    }
}
