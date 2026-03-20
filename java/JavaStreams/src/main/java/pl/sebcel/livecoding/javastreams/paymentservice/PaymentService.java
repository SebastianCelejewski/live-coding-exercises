package pl.sebcel.livecoding.javastreams.paymentservice;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PaymentService {
	
	/**
	 * Returns list of top N employees with the highest salary per company
	 * 
	 * @param companies list of companies with their employees per department
	 * @param topN number of employees with the highest salary to return per company
	 * @return list of top N employees with the highest salary per company
	 */
	public Map<String, List<Employee>> getTopPaidEmployeesPerCompany(List<Company> companies, int topN) {
		validateInput(companies, topN);
		
		return companies
				.stream()
				.collect(Collectors.toMap(
						Company::name,
						c -> c.departments()
								.stream()
								.flatMap(d -> d.employees().stream())
								.sorted(Comparator.comparing(Employee::salary).reversed())
								.limit(topN)
								.toList()
						));
	}
	
	/**
	 * Returns the highest paid employee per each company
	 * 
	 * If a company has no employees, it does not appear in the result
	 * 
	 * @param companies companies for which to find the highest paid employee
	 * @return mapping between companies and the highest paid employees for that company
	 */
	public Map<String, Employee> getHighestPaidEmployeePerCompany(List<Company> companies) {
		validateInput(companies);
		return companies.stream()
				.collect(Collectors.toMap(
						Company::name,
						c -> c.departments()
								.stream()
								.flatMap(d -> d.employees().stream())
								.max(Comparator.comparing(Employee::salary))
						))
				.entrySet()
				.stream()
				.filter(kv -> kv.getValue().isPresent())
				.collect(Collectors.toMap(kv -> kv.getKey(), kv -> kv.getValue().get()));
	}
	
	
	private void validateInput(List<Company> companies, int topN) {
		if (topN < 0) {
			throw new IllegalArgumentException();
		}

		validateInput(companies);
	}
	
	private void validateInput(List<Company> companies) {
		Objects.requireNonNull(companies);

		for (Company c: companies) {
			Objects.requireNonNull(c.departments());
			for (Department d : c.departments()) {
				Objects.requireNonNull(d.employees());
			}
		}
	}

}
