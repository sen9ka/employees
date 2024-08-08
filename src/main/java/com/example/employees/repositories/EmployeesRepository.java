package com.example.employees.repositories;

import com.example.employees.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EmployeesRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT e FROM Employee e JOIN TransferHistory th ON e.id = th.employee.id " +
            "WHERE th.departmentTo.id = :departmentId AND th.transferDate BETWEEN :dateFrom AND :dateTo")
    List<Employee> findEmployeesInDepartmentDuringPeriod(@Param("departmentId") Long departmentId,
                                                         @Param("dateFrom") Date dateFrom,
                                                         @Param("dateTo") Date dateTo);

    List<Employee> findEmployeesByTerminationDateIsNull();
}
