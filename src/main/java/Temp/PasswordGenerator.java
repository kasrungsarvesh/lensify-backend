package Temp;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

    public static void main(String[] args) {

        BCryptPasswordEncoder passwordEncoder =
                new BCryptPasswordEncoder();

        String encodedPassword =
                passwordEncoder.encode("password123");

        System.out.println(encodedPassword);
    }
}