package com.example.employees.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    @NotNull(message = "Имя сотрудника не должно быть пустым")
    @Size(min = 2, max = 100, message = "Имя сотрудника должно быть от 2 до 100 символов длиной")
    private String firstName;

    @Column(name = "second_name")
    @NotNull(message = "Фамилия сотрудника не должна быть пустой")
    @Size(min = 2, max = 100, message = "Фамилия сотрудника должно быть от 2 до 100 символов длиной")
    private String secondName;

    @Column(name = "patronymic")
    @NotNull(message = "Отчество сотрудника не должно быть пустым")
    @Size(min = 2, max = 100, message = "Отчество сотрудника должно быть от 2 до 100 символов длиной")
    private String patronymic;

    @Column(name = "hire_date")
    private Date hireDate;

    @Column(name = "termination_date")
    private Date terminationDate;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

}
