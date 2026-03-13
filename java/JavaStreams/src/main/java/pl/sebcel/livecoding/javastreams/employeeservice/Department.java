package pl.sebcel.livecoding.javastreams.employeeservice;

import java.util.List;

public record Department(String name, List<Employee> employees) {
}
