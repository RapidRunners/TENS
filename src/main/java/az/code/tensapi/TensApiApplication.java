package az.code.tensapi;

import az.code.tensapi.service.impl.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TensApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TensApiApplication.class, args);
    }

}
