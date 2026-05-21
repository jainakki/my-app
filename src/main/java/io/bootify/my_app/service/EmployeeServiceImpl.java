package io.bootify.my_app.service;

import io.bootify.my_app.domain.Department;
import io.bootify.my_app.domain.Employee;
import io.bootify.my_app.dto.EmployeeResponse;
import io.bootify.my_app.repos.DepartmentRepository;
import io.bootify.my_app.repos.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployee(Long id) {
        return employeeRepository.findById(id);
    }

    public EmployeeResponse createEmployee(Employee employee) {
        Employee saved = employeeRepository.save(employee);

        Department dept = departmentRepository.findById(saved.getDepartment().getId())
                .orElseThrow();

        return new EmployeeResponse(
                saved.getId(),
                saved.getName(),
                saved.getDepartment().getId(),
                dept.getDeptName(),
                saved.getLastUpdated(),
                saved.getDateCreated()
        );
    }

    public Employee updateEmployee(Long id, Employee employee) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found");
        }
        employee.setId(id);
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found");
        }
        employeeRepository.deleteById(id);
    }
}
