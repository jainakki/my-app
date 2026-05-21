package io.bootify.my_app.service;

import io.bootify.my_app.domain.Department;
import io.bootify.my_app.domain.Employee;
import io.bootify.my_app.dto.EmployeeResponse;
import io.bootify.my_app.repos.DepartmentRepository;
import io.bootify.my_app.repos.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(0, result.size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    public void testGetEmployeeByIdFound() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getEmployee(1L);

        assertEquals(Optional.of(employee), result);
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetEmployeeByIdNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Employee> result = employeeService.getEmployee(1L);

        assertEquals(Optional.empty(), result);
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateEmployee() {


        Department department = new Department();
        department.setId(1L);
        department.setDeptName("Engineering");

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setDepartment(department);

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        EmployeeResponse result = employeeService.createEmployee(employee);

        assertEquals(new Long(1), result.id());
        assertEquals("John Doe", result.name());
        assertEquals(new Long(1), result.departmentId());
        assertEquals("Engineering", result.departmentName());
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(departmentRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateEmployeeFound() {
        Employee existingEmployee = new Employee();
        existingEmployee.setId(1L);
        existingEmployee.setName("John Doe");

        when(employeeRepository.existsById(1L)).thenReturn(true);
       // when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
       // when(employeeRepository.save(any(Employee.class))).thenReturn(existingEmployee);

        when(employeeRepository.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(1L);
        updatedEmployee.setName("Jane Doe");

        Employee result = employeeService.updateEmployee(1L, updatedEmployee);

        assertEquals(updatedEmployee.getId(), result.getId());
        assertEquals(updatedEmployee.getName(), result.getName());
        verify(employeeRepository, times(1)).existsById(1L);
//        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void testUpdateEmployeeNotFound() {
        when(employeeRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            employeeService.updateEmployee(1L, new Employee());
        });

        assertEquals("Employee not found", exception.getMessage());
        verify(employeeRepository, times(1)).existsById(1L);
    }

    @Test
    public void testDeleteEmployeeFound() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).existsById(1L);
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteEmployeeNotFound() {
        when(employeeRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            employeeService.deleteEmployee(1L);
        });

        assertEquals("Employee not found", exception.getMessage());
        verify(employeeRepository, times(1)).existsById(1L);
    }
}