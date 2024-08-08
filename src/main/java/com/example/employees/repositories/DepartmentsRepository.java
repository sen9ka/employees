package com.example.employees.repositories;

import com.example.employees.models.Department;
import com.example.employees.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DepartmentsRepository extends JpaRepository<Department, Long> {
    Department findDepartmentByEmployees(Employee employee);

    List<Department> findAllByTerminationDateIsNull();

    List<Department> findByTerminationDateAfterOrTerminationDateIsNull(Date filterDate);
}
