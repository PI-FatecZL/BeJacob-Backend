package br.com.bejacob.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BeJacobBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeJacobBackendApplication.class, args);
    }

}
