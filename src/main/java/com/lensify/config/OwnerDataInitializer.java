package com.lensify.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.lensify.constants.RoleConstants;
import com.lensify.entity.Role;
import com.lensify.entity.User;
import com.lensify.repository.RoleRepository;
import com.lensify.repository.UserRepository;

@Component
public class OwnerDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public OwnerDataInitializer(UserRepository userRepository,
                                RoleRepository roleRepository,
                                PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        createOwner();

    }

    private void createOwner() {

        Role ownerRole = roleRepository
                .findByRoleName(RoleConstants.OWNER)
                .orElseThrow(() ->
                        new RuntimeException("OWNER role not found."));

        if (userRepository.findByRole(ownerRole).isPresent()) {
            return;
        }

        User owner = new User();

        owner.setFullName("Lensify Owner");
        owner.setUsername("owner");
        owner.setEmail("owner@lensify.com");
        owner.setPhoneNumber("9999999999");

        owner.setPassword(passwordEncoder.encode("owner123"));

        owner.setStatus(true);

        owner.setRole(ownerRole);

        userRepository.save(owner);

        System.out.println("Default OWNER account created.");

    }

}