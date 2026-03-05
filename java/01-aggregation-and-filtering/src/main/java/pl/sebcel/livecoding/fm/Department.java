package pl.sebcel.livecoding.fm;

import java.util.List;

public record Department(String name, List<Employee> employees) {

	public static Department dep(String name, Employee... employees) {
		return new Department(name, List.of(employees));
	}
}
