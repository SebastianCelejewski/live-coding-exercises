package pl.sebcel.livecoding.javastreams.paymentservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class GetHighestPaidEmployeePerCompanyTests {
	
	private PaymentService cut = new PaymentService();
	
	@Test
	public void should_throw_NullPointerException_when_list_of_companies_is_null() {
		assertThrows(NullPointerException.class, () -> {
			cut.getHighestPaidEmployeePerCompany(null);
		});
	}
	
	@Test
	public void should_throw_NullPointerException_when_list_of_departments_for_a_company_is_null() {
		Company companyWithNullDepartments = new Company("A", null);
		assertThrows(NullPointerException.class, () -> {
			cut.getHighestPaidEmployeePerCompany(List.of(companyWithNullDepartments));
		});
	}

	@Test
	public void should_throw_NullPointerException_when_list_of_employees_for_a_department_is_null() {
		Department departmentWithNullEmployees = new Department("Null", null);
		Company company = new Company("A", List.of(departmentWithNullEmployees));
		assertThrows(NullPointerException.class, () -> {
			cut.getHighestPaidEmployeePerCompany(List.of(company));
		});
	}

	@Test
	public void should_return_data_for_all_companies_that_have_employees() {
		Map<String, Employee> result = cut.getHighestPaidEmployeePerCompany(createCompanies());
		assertTrue(result.containsKey("A"));
		assertTrue(result.containsKey("B"));
		assertFalse(result.containsKey("C"));
	}
	
	@Test
	public void should_return_highest_paid_employee_per_each_company_that_has_employees() {
		Map<String, Employee> result = cut.getHighestPaidEmployeePerCompany(createCompanies());
		assertEquals("David", result.get("A").name());
		assertEquals("Lydia", result.get("B").name());
	}

	private List<Company> createCompanies() {
		return List.of(
				com("A",
						dep("HR",
								emp("Alice", "5000"),
								emp("Bob", "4000"),
								emp("Charlie", "3000"),
								emp("David", "7000")),
						dep("IT", 
								emp("Eve", "7000"),
								emp("Frank", "6000"),
								emp("Georgia", "4500"),
								emp("Henry", "2500"))
						),
				com("B",
						dep("Sales",
								emp("Ike","3000"),
								emp("Janet","4000"),
								emp("Kate","3500"),
								emp("Lydia","4500"))
						),
				com("C",
						dep("Cleaning"))
				);
	}
	
	private Employee emp(String name, String salary) {
		return new Employee(name, new BigDecimal(salary));
	}
	
	private Department dep(String name, Employee... employees) {
		return new Department(name, List.of(employees));
	}

	private Company com(String name, Department... departments) {
		return new Company(name, List.of(departments));
	}
}
