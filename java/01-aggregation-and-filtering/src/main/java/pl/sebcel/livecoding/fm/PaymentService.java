package pl.sebcel.livecoding.fm;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PaymentService {
	
	/**
	 * Returns list of top N employees with the highest salary per company
	 * 
	 * @param companies list of companies with their employees per department
	 * @param topN number of employees with the highest salary to return
	 * @return list of top N employees with the highest salary per company
	 */
	public Map<String, List<Employee>> getTopPaidEmployeesPerCompany(List<Company> companies, int topN) {
		
		if (companies == null) {
			throw new IllegalArgumentException();
		}
		
		return companies
				.stream()
				.peek(this::throwExceptionWhenCompanyHasNullDepartments)
				.collect(Collectors.toMap(Company::name, c -> c.departments()
						.stream()
						.peek(this::throwExceptionWhenDepartmentHasNullEmployees)
						.flatMap(d -> d.employees().stream())
						.sorted(Comparator.comparing(Employee::salary).reversed())
						.limit(topN)
						.toList(), (a, _) -> a));
	}
	
	private Company throwExceptionWhenCompanyHasNullDepartments(Company company) {
		if (company.departments() == null) {
			throw new IllegalArgumentException();
		}
		return company;
	}
	

	private Department throwExceptionWhenDepartmentHasNullEmployees(Department department) {
		if (department.employees() == null) {
			throw new IllegalArgumentException();
		}
		return department;
	}
	
}
