using LiveCodingExercises.LINQtoObjects.PaymentService;
using System;
using System.Collections.Generic;
using System.Linq;
using Xunit;

namespace LiveCodingExercises.LINQtoObjects.Test.PaymentService
{
    public class GetHighestPaidEmployeePerCompanyTests
    {
        private LINQtoObjects.PaymentService.PaymentService cut = new LINQtoObjects.PaymentService.PaymentService();

        [Fact]
        public void Should_return_data_for_all_companies_that_have_employees()
        {
            var result = cut.GetHighestPaidEmployeePerCompany(CreateCompanies());
            Assert.True(result.ContainsKey("A"));
            Assert.True(result.ContainsKey("B"));
            Assert.False(result.ContainsKey("C"));
        }

        [Fact]
        public void Should_return_highest_paid_employe_for_every_company_that_has_employees()
        {
            var result = cut.GetHighestPaidEmployeePerCompany(CreateCompanies());
            Assert.Contains(result["A"].Name, new List<string>(["David", "Eve"]));
            Assert.Equal("Lydia", result["B"].Name);
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
                ]),
                new Company("C", [
                    new Department("Cleaning", [])
                    ])
            ];
        }

    }
}
