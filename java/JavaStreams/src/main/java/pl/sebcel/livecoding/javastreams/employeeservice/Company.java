package pl.sebcel.livecoding.javastreams.employeeservice;

import java.util.List;

public record Company(String name, List<Department> departments) {
}
