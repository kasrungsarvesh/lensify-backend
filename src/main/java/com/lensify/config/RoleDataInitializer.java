package com.lensify.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.lensify.constants.RoleConstants;
import com.lensify.entity.Role;
import com.lensify.repository.RoleRepository;

@Component
public class RoleDataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleDataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {

        createRole(RoleConstants.OWNER,
                "System Owner with full access");

        createRole(RoleConstants.MANAGER,
                "Manage daily shop operations");

        createRole(RoleConstants.RECEPTIONIST,
                "Manage appointments, customers and billing");

        createRole(RoleConstants.OPTOMETRIST,
                "Manage eye tests and prescriptions");

    }

    private void createRole(String roleName, String description) {

        if (!roleRepository.existsByRoleName(roleName)) {

            Role role = new Role();

            role.setRoleName(roleName);

            role.setDescription(description);

            roleRepository.save(role);

            System.out.println(roleName + " Role Created.");

        }

    }

}