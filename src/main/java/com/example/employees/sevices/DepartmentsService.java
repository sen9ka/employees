package com.example.employees.sevices;

import com.example.employees.controllers.exceptionHandler.exceptions.DepartmentNotFoundException;
import com.example.employees.models.Department;
import com.example.employees.models.Employee;
import com.example.employees.repositories.DepartmentsRepository;
import com.example.employees.repositories.EmployeesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentsService {

    private final DepartmentsRepository departmentsRepository;
    private final EmployeesRepository employeesRepository;

    @Transactional
    public List<Department> findAll() {
        return departmentsRepository.findAllByTerminationDateIsNull();
    }

    @Transactional
    public void save(Department department) {
        department.setCreationDate(new Date());
        departmentsRepository.save(department);
    }

    @Transactional
    public Department findOne(long id) {
        Optional<Department> foundDepartment = departmentsRepository.findById(id);
        return foundDepartment.orElseThrow(() -> new DepartmentNotFoundException("Подразделение с id " + id + " не найдено"));
    }

    public Department getDepartmentOwner(Department department) {
        return department.getParentDepartment();
    }

    @Transactional
    public void update(long id, Department updatedDepartment) {
        Department departmentToBeUpdated = departmentsRepository.findById(id).get();

        updatedDepartment.setId(id);
        updatedDepartment.setParentDepartment(departmentToBeUpdated.getParentDepartment());

        departmentsRepository.save(updatedDepartment);
    }

    @Transactional
    public void delete(long id) {
        Department department = departmentsRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException("Подразделение с id " + id + " не найдено"));
        department.setTerminationDate(new Date());
        departmentsRepository.save(department);
    }

    @Transactional
    public void release(long id) {
        departmentsRepository.findById(id).ifPresent(
                department -> {
                    department.setParentDepartment(null);
                }
        );
    }

    @Transactional
    public void assign(long id, Department selectedDepartment) {
        departmentsRepository.findById(id).ifPresent(
                department -> {
                    department.setParentDepartment(selectedDepartment);
                }
        );
    }

    @Transactional
    public Department findDepartmentByEmployee(Employee employee) {
        return departmentsRepository.findDepartmentByEmployees(employee);
    }

    @Transactional
    public List<Employee> getEmployeesInDepartmentDuringPeriod(Long departmentId, Date startDate, Date endDate) {
        return employeesRepository.findEmployeesInDepartmentDuringPeriod(departmentId, startDate, endDate);
    }

    @Transactional
    public List<Department> findDepartmentsByDate(Date filterDate) {
        return departmentsRepository.findByTerminationDateAfterOrTerminationDateIsNull(filterDate);
    }

    @Transactional
    public List<Department> getCompanyStructure() {
        return departmentsRepository.findAllByTerminationDateIsNull();
    }
}
