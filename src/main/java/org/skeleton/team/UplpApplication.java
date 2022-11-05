package org.skeleton.team;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * Класс приложения для работы с ГПЗУ
 */
@EntityScan("org.skeleton.team.entity")
@SpringBootApplication
public class UplpApplication {

    public static void main(String[] args) {
        SpringApplication.run(UplpApplication.class, args);
    }
}
