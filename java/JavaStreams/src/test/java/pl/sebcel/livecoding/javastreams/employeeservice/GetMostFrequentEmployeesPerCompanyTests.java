package pl.sebcel.livecoding.javastreams.employeeservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class GetMostFrequentEmployeesPerCompanyTests {
	
	private EmployeeService cut = new EmployeeService();
	
	@Test
	public void should_throw_NullPointerException_when_companies_are_null() {
		assertThrows(NullPointerException.class, () -> cut.getMostFrequentEmployeesPerCompany(null));
	}
	
	@Test
	public void should_throw_NullPointerException_when_list_of_departments_for_a_company_is_null() {
		Company companyWithNullDepartments = new Company("A", null);
		assertThrows(NullPointerException.class, () -> {
			cut.getMostFrequentEmployeesPerCompany(List.of(companyWithNullDepartments));
		});
	}

	@Test
	public void should_throw_NullPointerException_when_list_of_employees_for_a_department_is_null() {
		Department departmentWithNullEmployees = new Department("Null", null);
		Company company = new Company("A", List.of(departmentWithNullEmployees));
		assertThrows(NullPointerException.class, () -> {
			cut.getMostFrequentEmployeesPerCompany(List.of(company));
		});
	}

	@Test
	public void should_return_data_for_all_companies() {
		Map<String, List<Employee>> result = cut.getMostFrequentEmployeesPerCompany(createCompanies());
		assertTrue(result.containsKey("A"));
		assertTrue(result.containsKey("B"));
	}

	@Test
	public void should_return_most_frequent_employees_per_company() {
		Map<String, List<Employee>> result = cut.getMostFrequentEmployeesPerCompany(createCompanies());
		assertEquals(1, result.get("A").size());
		assertTrue(result.get("A").stream().anyMatch(e -> e.name().equals("Alice")));
		
		assertEquals(2, result.get("B").size());
		List<String> employeeNames = result.get("B").stream().map(e -> e.name()).toList();
		assertTrue(employeeNames.contains("Tom"));
		assertTrue(employeeNames.contains("Jerry"));
	}

	
	private List<Company> createCompanies() {
		return List.of(
				com("A",
						dep("HR",
								emp("Alice"),
								emp("Bob"),
								emp("Charlie")),
						dep("IT", 
								emp("Alice"),
								emp("Bob")),
						dep("Sales", 
								emp("Alice"))),
				com("B",
						dep("Sales",
								emp("Tom"),
								emp("Jerry")),
						dep("HR",
								emp("Tom"),
								emp("Jerry")))
				);
	}
	
	private Employee emp(String name) {
		return new Employee(name);
	}
	
	private Department dep(String name, Employee... employees) {
		return new Department(name, List.of(employees));
	}

	private Company com(String name, Department... departments) {
		return new Company(name, List.of(departments));
	}

}
