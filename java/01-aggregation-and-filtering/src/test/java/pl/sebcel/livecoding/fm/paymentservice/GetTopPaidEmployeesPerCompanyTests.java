package pl.sebcel.livecoding.fm.paymentservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import pl.sebcel.livecoding.fm.Company;
import pl.sebcel.livecoding.fm.Department;
import pl.sebcel.livecoding.fm.Employee;
import pl.sebcel.livecoding.fm.PaymentService;

public class GetTopPaidEmployeesPerCompanyTests {

	private PaymentService cut = new PaymentService();
	
	@Test
	public void should_throw_IllegalArgumentException_when_list_of_companies_is_null() {
		assertThrows(IllegalArgumentException.class, () -> {
			cut.getTopPaidEmployeesPerCompany(null, 3);
		});
	}
	
	@Test
	public void should_throw_IllegalArgumentException_when_list_of_departments_for_a_company_is_null() {
		Company companyWithNullDepartments = new Company("A", null);
		assertThrows(IllegalArgumentException.class, () -> {
			cut.getTopPaidEmployeesPerCompany(List.of(companyWithNullDepartments), 3);
		});
	}

	@Test
	public void should_throw_IllegalArgumentException_when_list_of_employees_for_a_department_is_null() {
		Department departmentWithNullEmployees = new Department("Null", null);
		Company company = new Company("A", List.of(departmentWithNullEmployees));
		assertThrows(IllegalArgumentException.class, () -> {
			cut.getTopPaidEmployeesPerCompany(List.of(company), 3);
		});
	}

	@Test
	public void should_return_data_for_all_companies() {
		Map<String, List<Employee>> result = cut.getTopPaidEmployeesPerCompany(createCompanies(), 99);
		assertTrue(result.containsKey("A"));
		assertTrue(result.containsKey("B"));
	}
	
	@Test
	public void should_return_employees_in_descending_order() {
		Map<String, List<Employee>> result = cut.getTopPaidEmployeesPerCompany(createCompanies(), 99);
		Stream<BigDecimal> salariesA = result.get("A").stream().map(Employee::salary); 
		Stream<BigDecimal> salariesB = result.get("B").stream().map(Employee::salary);
		assertTrue(isDescending(salariesA));
		assertTrue(isDescending(salariesB));
	}


	@Test
	public void should_return_topN_employees() {
		Map<String, List<Employee>> result = cut.getTopPaidEmployeesPerCompany(createCompanies(), 3);
		Stream<BigDecimal> salariesA = result.get("A").stream().map(Employee::salary); 
		Stream<BigDecimal> salariesB = result.get("B").stream().map(Employee::salary);
		assertEquals(3, salariesA.count());
		assertEquals(3, salariesB.count());
	}

	private <T extends Comparable<T>> boolean isDescending(Stream<T> stream) {
		List<T> list = stream.toList();
		for (int i = 1; i < list.size(); i++) {
			if (list.get(i-1).compareTo(list.get(i)) < 0) {
				return false;
			}
		}
		return true;
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
						)
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
