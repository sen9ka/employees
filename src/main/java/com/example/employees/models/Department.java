package com.example.employees.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotNull(message = "Название подразделения не должно быть пустым")
    @Size(min = 2, max = 100, message = "Название подразделения должно быть от 2 до 100 символов длиной")
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Department parentDepartment;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "termination_date")
    private Date terminationDate;

}
