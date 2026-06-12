package com.mapocho.gasmapocho;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class GasmapochoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GasmapochoApplication.class, args);
	}

    public static class GenerarHash {
        public static void main(String[] args) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            System.out.println(encoder.encode("admin123"));
        }
    }
}
