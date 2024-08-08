package com.example.employees.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "transfer_history")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TransferHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "department_from")
    private Department departmentFrom;

    @ManyToOne
    @JoinColumn(name = "department_to")
    private Department departmentTo;

    @Column(name = "transfer_date")
    private Date transferDate;

}