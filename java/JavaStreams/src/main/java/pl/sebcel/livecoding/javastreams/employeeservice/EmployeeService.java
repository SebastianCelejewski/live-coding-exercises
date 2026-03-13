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
		Map<Employee, Integer> count = new HashMap<>();
		for(Employee e : employees) {
			count.merge(e, 1,  Integer::sum);
		}
		
		List<Employee> result = count   // Map<Employee, Integer>
			.entrySet()  // Set<Map.Entry<Employee, Integer>>
			.stream()    // Stream<Map.Entry<Employee, Integer>>
			.collect(Collectors.groupingBy(Map.Entry<Employee, Integer>::getValue, Collectors.mapping(x -> x.getKey(), Collectors.toList())))  // Map<Integer, List<Employee>>
			.entrySet() // Set<Map.Entry<Integer, List<Employee>>>
			.stream() // Stream<Map.Entry<Integer, List<Employee>>>
			.max(Map.Entry.comparingByKey()) // Optional<Map.Entry<Integer, List<Employee>>>
			.orElseThrow() // <Map.Entry<Integer, List<Employee>>
			.getValue(); //List<Employee>

		return result;
	}
}
