package com.example.employees.controllers;

import com.example.employees.models.Department;
import com.example.employees.models.Employee;
import com.example.employees.sevices.DepartmentsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Tag(name = "API для работы с подразделениями")
@AllArgsConstructor
@RequestMapping("/departments")
public class DepartmentsController {

    private final DepartmentsService departmentsService;

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("departments", departmentsService.findAll());
        return "departments/index";
    }

    @GetMapping("/filter-by-date")
    public String filterDepartmentsByDate(@RequestParam("filterDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date filterDate,
                                          Model model) {
        List<Department> departments = departmentsService.findDepartmentsByDate(filterDate);
        model.addAttribute("departments", departments);
        return "departments/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model, @ModelAttribute("department") Department department) {
        Department foundDepartment = departmentsService.findOne(id);
        model.addAttribute("department", foundDepartment);

        Department ownerDepartment = departmentsService.getDepartmentOwner(foundDepartment);

        if (ownerDepartment != null)
            model.addAttribute("ownerDepartment", ownerDepartment);
        else
            model.addAttribute("departments", departmentsService.findAll().stream().filter(d -> !d.getId().equals(id)).collect(Collectors.toList()));

        return "departments/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("department") Department department) {
        return "departments/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("department") @Valid Department department,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "departments/new";
        departmentsService.save(department);
        return "redirect:/departments";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        model.addAttribute("department", departmentsService.findOne(id));
        return "departments/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("department") @Valid Department department,
                         BindingResult bindingResult,
                         @PathVariable("id") long id) {
        if (bindingResult.hasErrors())
            return "departments/edit";
        departmentsService.update(id, department);
        return "redirect:/departments";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        departmentsService.delete(id);
        return "redirect:/departments";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") long id) {
        departmentsService.release(id);
        return "redirect:/departments/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") long id, @ModelAttribute("ownerDepartment") Department selectedDepartment) {
        departmentsService.assign(id, selectedDepartment);
        return "redirect:/departments/" + id;
    }

    @GetMapping("/employees/period")
    public String getEmployeesInDepartmentDuringPeriod(@RequestParam("departmentId") Long departmentId,
                                                       @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                       @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                                       Model model) {
        List<Employee> employees = departmentsService.getEmployeesInDepartmentDuringPeriod(departmentId, startDate, endDate);
        model.addAttribute("department", departmentsService.findOne(departmentId));
        model.addAttribute("employees", employees);
        return "departments/show";
    }

    @GetMapping("/structure")
    public String showCompanyStructure(Model model) {
        model.addAttribute("departments", departmentsService.getCompanyStructure());
        return "departments/company-structure";
    }
}
