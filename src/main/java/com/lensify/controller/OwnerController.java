package com.lensify.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OwnerController {

    @GetMapping("/api/v1/owner/dashboard")
    public String ownerDashboard() {
        return "Welcome OWNER";
    }
}