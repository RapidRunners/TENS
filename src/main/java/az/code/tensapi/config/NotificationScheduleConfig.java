package az.code.tensapi.config;

import az.code.tensapi.repository.NotificationRepository;
import az.code.tensapi.repository.TaskRepository;
import az.code.tensapi.service.EmailService;
import az.code.tensapi.service.impl.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class NotificationScheduleConfig {
    private final EmailService emailService;
    private final NotificationRepository notificationRepository;
    private final TaskRepository taskRepository;

    @Bean
    public ScheduleService scheduleService() {
        return new ScheduleService(emailService, notificationRepository, taskRepository);
    }
}
