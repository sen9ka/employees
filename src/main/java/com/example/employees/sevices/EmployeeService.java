package com.example.employees.sevices;

import com.example.employees.controllers.exceptionHandler.exceptions.DepartmentNotFoundException;
import com.example.employees.controllers.exceptionHandler.exceptions.EmployeeNotFoundException;
import com.example.employees.models.Department;
import com.example.employees.models.Employee;
import com.example.employees.models.TransferHistory;
import com.example.employees.repositories.EmployeesRepository;
import com.example.employees.repositories.TransferHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeesRepository employeesRepository;
    private final TransferHistoryRepository transferHistoryRepository;

    @Transactional
    public List<Employee> findAll() {
        return employeesRepository.findEmployeesByTerminationDateIsNull();
    }

    @Transactional
    public Employee findOne(long id) {
        Optional<Employee> foundEmployee = employeesRepository.findById(id);
        return foundEmployee.orElseThrow(() -> new EmployeeNotFoundException("Сотрудник с идентификатором " + id + " не найден"));
    }

    @Transactional
    public void save(Employee employee) {
        employee.setHireDate(new Date());
        employeesRepository.save(employee);
    }

    @Transactional
    public void update(long id, Employee updatedPerson) {
        updatedPerson.setId(id);
        employeesRepository.save(updatedPerson);
    }

    @Transactional
    public void release(long id) {
        employeesRepository.findById(id).ifPresent(
                employee -> {
                    TransferHistory transferHistory = TransferHistory.builder()
                            .employee(employee)
                            .transferDate(new Date())
                            .departmentFrom(employee.getDepartment())
                            .departmentTo(null)
                            .build();
                    transferHistoryRepository.save(transferHistory);
                    employee.setDepartment(null);
                }
        );
    }

    @Transactional
    public void assign(long id, Department selectedDepartment) {
        employeesRepository.findById(id).ifPresent(
                employee -> {
                    TransferHistory transferHistory = TransferHistory.builder()
                            .employee(employee)
                            .transferDate(new Date())
                            .departmentFrom(employee.getDepartment())
                            .departmentTo(selectedDepartment)
                            .build();
                    transferHistoryRepository.save(transferHistory);
                    employee.setDepartment(selectedDepartment);
                }
        );
    }

    @Transactional
    public void delete(long id) {
        Employee employee = employeesRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Сотрудник с id " + id + " не найден"));
        if (employee.getDepartment() != null) {
            TransferHistory transferHistory = TransferHistory.builder()
                    .employee(employee)
                    .transferDate(new Date())
                    .departmentFrom(employee.getDepartment())
                    .departmentTo(null)
                    .build();
            transferHistoryRepository.save(transferHistory);
            employee.setDepartment(null);
        }
        employee.setTerminationDate(new Date());
        employeesRepository.save(employee);
    }
}
