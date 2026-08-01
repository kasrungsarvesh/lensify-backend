package com.lensify.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    @GetMapping("/api/v1/employee/dashboard")
    public String employeeDashboard() {
        return "Employee Module";
    }
}