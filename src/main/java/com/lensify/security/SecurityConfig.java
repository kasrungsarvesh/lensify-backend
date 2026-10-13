package com.lensify.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final DaoAuthenticationProvider authenticationProvider;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            DaoAuthenticationProvider authenticationProvider) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationProvider = authenticationProvider;
    }

    // =========================================================
    // AUTHENTICATION MANAGER
    // =========================================================

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    // =========================================================
    // SECURITY FILTER CHAIN
    // =========================================================

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            // =================================================
            // CORS
            // =================================================

            .cors(cors -> {})

            // =================================================
            // CSRF
            // =================================================

            .csrf(csrf -> csrf.disable())

            // =================================================
            // STATELESS SESSION
            // =================================================

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authenticationProvider(authenticationProvider)

            // =================================================
            // AUTHORIZATION
            // =================================================

            .authorizeHttpRequests(auth -> auth

                // -------------------------------------------------
                // OPTIONS / CORS PREFLIGHT
                // -------------------------------------------------

                .requestMatchers(
                    HttpMethod.OPTIONS,
                    "/**"
                ).permitAll()

                // -------------------------------------------------
                // LOGIN
                // -------------------------------------------------

                .requestMatchers(
                    "/api/v1/auth/login"
                ).permitAll()

                // -------------------------------------------------
                // REGISTRATION
                // -------------------------------------------------

                /*
                 * Currently kept public because your existing
                 * Register page creates users through:
                 *
                 * POST /api/v1/users
                 *
                 * Once initial OWNER/ADMIN setup is complete,
                 * we should change this to OWNER + ADMIN only.
                 */

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/v1/users"
                ).permitAll()

                // =================================================
                // OWNER ONLY
                // =================================================

                .requestMatchers(
                    "/api/v1/owner/**"
                ).hasRole("OWNER")

                // =================================================
                // USER MANAGEMENT
                // OWNER + ADMIN
                // =================================================

                .requestMatchers(
                    "/api/v1/users/**"
                ).hasAnyRole(
                    "OWNER",
                    "ADMIN"
                )

                // =================================================
                // ADMIN APIs
                // OWNER + ADMIN
                // =================================================

                .requestMatchers(
                    "/api/v1/admin/**"
                ).hasAnyRole(
                    "OWNER",
                    "ADMIN"
                )

                // =================================================
                // CUSTOMERS
                // ALL ROLES
                // =================================================

                .requestMatchers(
                    "/api/v1/customers/**"
                ).hasAnyRole(
                    "OWNER",
                    "ADMIN",
                    "MANAGER",
                    "EMPLOYEE",
                    "RECEPTIONIST",
                    "OPTOMETRIST"
                )

                // Existing singular customer endpoint
                .requestMatchers(
                    "/api/v1/customer/**"
                ).hasAnyRole(
                    "OWNER",
                    "ADMIN",
                    "MANAGER",
                    "EMPLOYEE",
                    "RECEPTIONIST",
                    "OPTOMETRIST"
                )

                // =================================================
                // PRESCRIPTIONS
                // ALL ROLES
                // =================================================

                .requestMatchers(
                    "/api/v1/prescriptions/**"
                ).hasAnyRole(
                    "OWNER",
                    "ADMIN",
                    "MANAGER",
                    "EMPLOYEE",
                    "RECEPTIONIST",
                    "OPTOMETRIST"
                )

                // =================================================
                // ORDERS
                // OWNER + ADMIN + MANAGER + EMPLOYEE + RECEPTIONIST
                // =================================================

                .requestMatchers(
                    "/api/v1/orders/**"
                ).hasAnyRole(
                    "OWNER",
                    "ADMIN",
                    "MANAGER",
                    "EMPLOYEE",
                    "RECEPTIONIST"
                )

                // =================================================
                // BILLS / RECEIPTS
                // OWNER + ADMIN + MANAGER + EMPLOYEE + RECEPTIONIST
                // =================================================

                .requestMatchers(
                    "/api/v1/bills/**"
                ).hasAnyRole(
                    "OWNER",
                    "ADMIN",
                    "MANAGER",
                    "EMPLOYEE",
                    "RECEPTIONIST"
                )

                // =================================================
                // PAYMENTS
                // OWNER + ADMIN + MANAGER + EMPLOYEE + RECEPTIONIST
                // =================================================

                .requestMatchers(
                    "/api/v1/payments/**"
                ).hasAnyRole(
                    "OWNER",
                    "ADMIN",
                    "MANAGER",
                    "EMPLOYEE",
                    "RECEPTIONIST"
                )

                // =================================================
                // PRODUCTS - VIEW
                // ALL ROLES
                // =================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/v1/products/**"
                ).hasAnyRole(
                    "OWNER",
                    "ADMIN",
                    "MANAGER",
                    "EMPLOYEE",
                    "RECEPTIONIST",
                    "OPTOMETRIST"
                )

                // -------------------------------------------------
                // PRODUCTS - CREATE
                // OWNER + ADMIN + MANAGER
                // -------------------------------------------------

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/v1/products/**"
                ).hasAnyRole(
                    "OWNER",
                    "ADMIN",
                    "MANAGER"
                )

                // -------------------------------------------------
                // PRODUCTS - UPDATE
                // OWNER + ADMIN + MANAGER
                // -------------------------------------------------

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/v1/products/**"
                ).hasAnyRole(
                    "OWNER",
                    "ADMIN",
                    "MANAGER"
                )

                // -------------------------------------------------
                // PRODUCTS - DELETE
                // OWNER + ADMIN + MANAGER
                // -------------------------------------------------

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/v1/products/**"
                ).hasAnyRole(
                    "OWNER",
                    "ADMIN",
                    "MANAGER"
                )

                // =================================================
                // CATEGORIES - VIEW
                // ALL ROLES
                // =================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/v1/categories/**"
                ).hasAnyRole(
                    "OWNER",
                    "ADMIN",
                    "MANAGER",
                    "EMPLOYEE",
                    "RECEPTIONIST",
                    "OPTOMETRIST"
                )

                // -------------------------------------------------
                // CATEGORIES - CREATE
                // OWNER + ADMIN + MANAGER
                // -------------------------------------------------

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/v1/categories/**"
                ).hasAnyRole(
                    "OWNER",
                    "ADMIN",
                    "MANAGER"
                )

                // -------------------------------------------------
                // CATEGORIES - UPDATE
                // -------------------------------------------------

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/v1/categories/**"
                ).hasAnyRole(
                    "OWNER",
                    "ADMIN",
                    "MANAGER"
                )

                // -------------------------------------------------
                // CATEGORIES - DELETE
                // -------------------------------------------------

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/v1/categories/**"
                ).hasAnyRole(
                    "OWNER",
                    "ADMIN",
                    "MANAGER"
                )

                // =================================================
                // LENSES - VIEW
                // ALL ROLES
                // =================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/v1/lenses/**"
                ).hasAnyRole(
                    "OWNER",
                    "ADMIN",
                    "MANAGER",
                    "EMPLOYEE",
                    "RECEPTIONIST",
                    "OPTOMETRIST"
                )

                // -------------------------------------------------
                // LENSES - CREATE
                // -------------------------------------------------

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/v1/lenses/**"
                ).hasAnyRole(
                    "OWNER",
                    "ADMIN",
                    "MANAGER"
                )

                // -------------------------------------------------
                // LENSES - UPDATE
                // -------------------------------------------------

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/v1/lenses/**"
                ).hasAnyRole(
                    "OWNER",
                    "ADMIN",
                    "MANAGER"
                )

                // -------------------------------------------------
                // LENSES - DELETE
                // -------------------------------------------------

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/v1/lenses/**"
                ).hasAnyRole(
                    "OWNER",
                    "ADMIN",
                    "MANAGER"
                )

                // =================================================
                // APPOINTMENTS
                // ALL ROLES
                // =================================================

                .requestMatchers(
                    "/api/v1/appointments/**"
                ).hasAnyRole(
                    "OWNER",
                    "ADMIN",
                    "MANAGER",
                    "EMPLOYEE",
                    "RECEPTIONIST",
                    "OPTOMETRIST"
                )

                // =================================================
                // REPORTS
                // OWNER + ADMIN + MANAGER
                // =================================================

                .requestMatchers(
                    "/api/v1/reports/**"
                ).hasAnyRole(
                    "OWNER",
                    "ADMIN",
                    "MANAGER"
                )

                // =================================================
                // SETTINGS
                // OWNER + ADMIN
                // =================================================

                .requestMatchers(
                    "/api/v1/settings/**"
                ).hasAnyRole(
                    "OWNER",
                    "ADMIN"
                )

                // =================================================
                // ACTIVITY LOGS
                // OWNER + ADMIN
                // =================================================

                .requestMatchers(
                    "/api/v1/activity/**"
                ).hasAnyRole(
                    "OWNER",
                    "ADMIN"
                )

                // =================================================
                // CHANGE PASSWORD
                // ANY AUTHENTICATED USER
                // =================================================

                .requestMatchers(
                    "/api/v1/auth/change-password"
                ).authenticated()

                // =================================================
                // EVERYTHING ELSE
                // AUTHENTICATED USERS ONLY
                // =================================================

                .anyRequest().authenticated()
            )

            // =================================================
            // JWT AUTHENTICATION FILTER
            // =================================================

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    // =========================================================
    // CORS CONFIGURATION
    // =========================================================

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
            new CorsConfiguration();

  
        configuration.setAllowedOrigins(
            List.of(
                "http://localhost:5173",
                "https://lensify-7ylz.vercel.app",
                "https://lensify-ecru.vercel.app",
                "https://lensify-7ylz-git-main-kasrungsarveshs-projects.vercel.app"
            )
        );
      

        configuration.setAllowedMethods(
            List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
            )
        );

        configuration.setAllowedHeaders(
            List.of("*")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
            "/**",
            configuration
        );

        return source;
    }
}