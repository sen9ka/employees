package com.example.employees.controllers;

import com.example.employees.models.Department;
import com.example.employees.models.Employee;
import com.example.employees.sevices.DepartmentsService;
import com.example.employees.sevices.EmployeeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
@Tag(name = "API для работы с сотрудниками")
@AllArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentsService departmentsService;

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("employees", employeeService.findAll());
        return "employees/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model, @ModelAttribute("department") Department department) {
        Employee employee = employeeService.findOne(id);
        model.addAttribute("employee", employee);
        model.addAttribute("departments", departmentsService.findAll());
        return "employees/show";
    }

    @GetMapping("/new")
    public String newEmployee(@ModelAttribute("employee") Employee employee) {
        return "employees/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("employee") @Valid Employee employee, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "employees/new";
        employeeService.save(employee);
        return "redirect:/employees";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        model.addAttribute("employee", employeeService.findOne(id));
        return "employees/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("employee") @Valid Employee employee,
                         BindingResult bindingResult,
                         @PathVariable("id") long id) {
        if (bindingResult.hasErrors())
            return "employees/edit";
        employeeService.update(id, employee);
        return "redirect:/employees";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") long id) {
        employeeService.release(id);
        return "redirect:/employees/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") long id, @ModelAttribute("department") Department selectedDepartment) {
        employeeService.assign(id, selectedDepartment);
        return "redirect:/employees/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        employeeService.delete(id);
        return "redirect:/employees";
    }

}
