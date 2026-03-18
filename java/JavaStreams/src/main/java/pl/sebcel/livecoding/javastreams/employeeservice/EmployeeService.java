package pl.sebcel.livecoding.javastreams.employeeservice;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EmployeeService {

	public Map<String, List<Employee>> getMostFrequentEmployeesPerCompany(List<Company> companies) {
		validateInput(companies);
		
		return companies.stream()
				.collect(Collectors.toMap(
						Company::name,
						c -> getMostFrequentEmployeesPerSingleCompany(c.departments()
																	.stream()
																	.flatMap(d -> d.employees().stream())
																	.toList())
						));
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
	
	private List<Employee> getMostFrequentEmployeesPerSingleCompany(List<Employee> employees) {
		Map<Employee, Integer> counts = employees.stream()
											.collect(Collectors.toMap(Function.identity(), _ -> 1, (a, b) -> a + b));
		int max = counts.entrySet()
					.stream()
					.sorted(Map.Entry.<Employee, Integer>comparingByValue().reversed())
					.findFirst()
					.orElseThrow()
					.getValue();
		
		return counts.entrySet()
				.stream()
				.filter(e -> e.getValue().equals(max)).map(f -> f.getKey())
				.toList();
	}
}
