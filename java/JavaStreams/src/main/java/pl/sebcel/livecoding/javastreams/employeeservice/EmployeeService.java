package pl.sebcel.livecoding.javastreams.employeeservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmployeeService {

	public Map<String, List<Employee>> getMostFrequentEmployeesPerCompany(List<Company> companies) {
		validateInput(companies);
		
		return companies
				.stream()
				.collect(Collectors.toMap(
						Company::name,
						c -> getMostFrequentEmployeesPerSingleCompany(c.departments()
								.stream()
								.flatMap(d -> d.employees().stream()).toList())));
	}
	
	private void validateInput(List<Company> companies) {
		if (companies == null) {
			throw new IllegalArgumentException();
		}
		for (Company c : companies) {
			if (c.departments() == null) {
				throw new IllegalArgumentException();
			}
			for (Department d: c.departments()) {
				if (d.employees() == null) {
					throw new IllegalArgumentException();
				}
			}
		}
	}
	
	private List<Employee> getMostFrequentEmployeesPerSingleCompany(List<Employee> employees) {
		Map<Employee, Integer> counts = employees.stream().collect(Collectors.groupingBy(e -> e, Collectors.summingInt(_ -> 1)));
		int max = counts.values().stream().max(Integer::compare).orElseThrow();
		return counts.entrySet().stream().filter(kv -> kv.getValue().equals(max)).map(ku -> ku.getKey()).toList();
	}
}
