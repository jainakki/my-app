package io.bootify.my_app.service;

import io.bootify.my_app.domain.Employee;
import io.bootify.my_app.dto.EmployeeResponse;

import java.util.Optional;

public interface EmployeeService {
    Optional<Employee> getEmployee(Long id);

    EmployeeResponse createEmployee(Employee employee);

    Employee updateEmployee(Long id, Employee employee);

    void deleteEmployee(Long id);
}
