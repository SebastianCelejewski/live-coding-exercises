using LiveCodingExercises.LINQtoObjects.EmployeeService;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Xunit;

namespace LiveCodingExercises.LINQtoObjects.Test.EmployeeService
{
    public class GetMostFrequentEmployeesPerCompanyTests
    {
        private LINQtoObjects.EmployeeService.EmployeeService cut = new();

        [Fact]
        public void Should_return_data_for_all_companies()
        {
            var result = cut.GetMostFrequentEmployeesPerCompany(CreateCompanies());
            Assert.True(result.ContainsKey("A"));
            Assert.True(result.ContainsKey("B"));
        }

        [Fact]
        public void Should_return_most_frequent_employees_per_each_company()
        {
            var result = cut.GetMostFrequentEmployeesPerCompany(CreateCompanies());
            var mostFrequentEmployeesForCompanyA = result["A"].Select(e => e.Name).ToList();
            var mostFrequentEmployeesForCompanyB = result["B"].Select(e => e.Name).ToList();

            Assert.Single(mostFrequentEmployeesForCompanyA);
            Assert.Contains("Alice", mostFrequentEmployeesForCompanyA);

            Assert.Equal(2, mostFrequentEmployeesForCompanyB.Count);
            Assert.Contains("Tom", mostFrequentEmployeesForCompanyB);
            Assert.Contains("Jerry", mostFrequentEmployeesForCompanyB);
        }

        private IEnumerable<Company> CreateCompanies()
        {
            return [
                new Company("A", [
                    new Department("IT", [
                        new Employee("Alice"),
                        new Employee("Bob"),
                        new Employee("Charlie"),
                        ]),
                    new Department("HR", [
                        new Employee("Alice"),
                        new Employee("Bob"),
                        ]),
                    new Department("Sales", [
                        new Employee("Alice")
                        ])
                    ]),
                new Company("B", [
                    new Department("HR", [
                        new Employee("Tom"),
                        new Employee("Jerry")
                        ]),
                    new Department("Sales", [
                        new Employee("Tom"),
                        new Employee("Jerry")
                        ])
                ])
            ];
        }

    }
}
