package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StatsClientApp {
    public static void main(String[] args) {
        SpringApplication.run(StatsDtoApp.class, args);
    }
}