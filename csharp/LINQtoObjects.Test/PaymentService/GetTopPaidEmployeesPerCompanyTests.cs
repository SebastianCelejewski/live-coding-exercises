using LiveCodingExercises.LINQtoObjects.PaymentService;
using System;
using System.Collections.Generic;
using System.Linq;
using Xunit;

namespace LiveCodingExercises.LINQtoObjects.Test.PaymentService
{
    public class GetTopPaidEmployeesPerCompanyTests
    {
        private LINQtoObjects.PaymentService.PaymentService cut = new LINQtoObjects.PaymentService.PaymentService();

        [Fact]
        public void Should_throw_ArgumentOutOfRangeException_if_topN_is_negative()
        {
            Assert.Throws<ArgumentOutOfRangeException>(() =>
            {
                cut.GetTopPaidEmployeesPerCompany(new List<Company>(), -5);
            });
        }


        [Fact]
        public void Should_return_data_for_all_companies()
        {
            var result = cut.GetTopPaidEmployeesPerCompany(CreateCompanies(), 99);
            Assert.True(result.ContainsKey("A"));
            Assert.True(result.ContainsKey("B"));
        }

        [Fact]
        public void Should_return_employees_in_descending_order()
        {
            var result = cut.GetTopPaidEmployeesPerCompany(CreateCompanies(), 99);
            Assert.True(IsDescending(result["A"]));
            Assert.True(IsDescending(result["B"]));
        }

        [Fact]
        public void Should_return_topN_employees()
        {
            var result = cut.GetTopPaidEmployeesPerCompany(CreateCompanies(), 3);
            Assert.Equal(3, result["A"].Count());
            Assert.Equal(3, result["B"].Count());
        }

        private IEnumerable<Company> CreateCompanies()
        {
            return [
                new Company("A", [
                    new Department("IT", [
                        new Employee("Alice", 5000m),
                        new Employee("Bob", 4000m),
                        new Employee("Charlie", 3000m),
                        new Employee("David", 7000m)
                        ]),
                    new Department("HR", [
                        new Employee("Eve", 7000m),
                        new Employee("Frank", 6000m),
                        new Employee("Georgia", 4500m),
                        new Employee("Henry", 2500m)
                        ])
                    ]),
                new Company("B", [
                    new Department("Sales", [
                        new Employee("Ike", 3000m),
                        new Employee("Janet", 4000m),
                        new Employee("Kate", 3500m),
                        new Employee("Lydia", 4500m)
                    ])
                ])
            ];
        }

        private bool IsDescending(IEnumerable<Employee> employees) {
            Employee? previous = null;
            foreach (var employee in employees)
            {
                if (previous != null && previous.Salary < employee.Salary)
                {
                    return false;
                }
                previous = employee;
            }
            return true;
        }
    }
}
