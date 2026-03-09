package pl.sebcel.livecoding.javastreams.paymentservice;

import java.util.List;

public record Company(String name, List<Department> departments) {

}
