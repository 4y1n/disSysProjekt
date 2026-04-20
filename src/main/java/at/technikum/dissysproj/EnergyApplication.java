package at.technikum.dissysproj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javafx.application.Application;

@SpringBootApplication
public class EnergyApiApplication extends Application{
    public static void main(String[] args) {
        SpringApplication.run(EnergyApiApplication.class, args);
    }
}