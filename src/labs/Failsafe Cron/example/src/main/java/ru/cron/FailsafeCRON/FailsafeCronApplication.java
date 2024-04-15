package ru.cron.FailsafeCRON;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.cron.FailsafeCRON.services.impl.ScheduledServiceImpl;

@SpringBootApplication
@RequiredArgsConstructor
public class FailsafeCronApplication implements CommandLineRunner {
    private final ScheduledServiceImpl scheduledService;

    public static void main(String[] args) {
        SpringApplication.run(FailsafeCronApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        scheduledService.script();
    }
}