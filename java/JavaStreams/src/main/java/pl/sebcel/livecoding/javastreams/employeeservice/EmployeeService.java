package pl.sebcel.livecoding.javastreams.employeeservice;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmployeeService {

	public Map<String, List<Employee>> getMostFrequentEmployeesPerCompany(List<Company> companies) {
		validateInput(companies);
		return companies.stream()
				.collect(Collectors.toMap(
						Company::name,
						c -> getMostFrequentEmployees(c.departments()
								.stream()
								.flatMap(d -> d.employees().stream())
								))
						);
	}
	
	private List<Employee> getMostFrequentEmployees(Stream<Employee> employees) {
		Map<Employee, Integer> counts = employees.collect(Collectors.toMap(Function.identity(), _ -> 1, Integer::sum));
		int max = counts.entrySet().stream().map(Map.Entry::getValue).max(Integer::compare).orElseThrow();
		return counts.entrySet().stream().filter(e -> e.getValue() == max).map(Map.Entry::getKey).toList();
	}
	
	private void validateInput(List<Company> companies) {
		Objects.requireNonNull(companies);
		for (Company c : companies) {
			Objects.requireNonNull(c.departments());
			for (Department d : c.departments()) {
				Objects.requireNonNull(d.employees());
			}
		}
	}
}
