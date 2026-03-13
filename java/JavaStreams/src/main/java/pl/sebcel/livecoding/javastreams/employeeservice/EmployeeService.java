package pl.sebcel.livecoding.javastreams.employeeservice;

import java.security.InvalidParameterException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmployeeService {

	public Map<String, List<Employee>> getMostFrequentEmployeesPerCompany(List<Company> companies) {
		if (companies == null) {
			throw new InvalidParameterException();
		}
		
		return companies
				.stream() // Stream<Company>
				.flatMap(c -> c.departments().stream().map(d -> Map.entry(c.name(), d))) // Stream<Map.Entry<String, Department>>
				.flatMap(kv -> kv.getValue().employees().stream().map(e -> Map.entry(kv.getKey(), e))) // Stream<Map.Entry<String, Employee>>
				.collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList()))) // Map<String, List<Employee>>
				.entrySet() // Set<Map.Entry<String, List<Employee>>>
				.stream() // Stream<Map.Entry<String, List<Employee>>>
				.map(x -> Map.entry(x.getKey(), getMostFrequentEmployeesPerSingleCompany(x.getValue()))) // Stream<Map.Entry<String, List<Employee>>>
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
	
	record EmpWithCount(String name, Integer count) {}

	
	private List<Employee> getMostFrequentEmployeesPerSingleCompany(List<Employee> employees) {
		
		Map<Employee, Integer> count = new HashMap<>();
		for(Employee e : employees) {
			count.merge(e, 1,  Integer::sum);
		}
		
		// count: Map<Employee, Integer>
		// r: Map.Entry<Employee, Integer>
		
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
