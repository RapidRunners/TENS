package az.code.tensapi.service.impl;

import az.code.tensapi.entity.Notification;
import az.code.tensapi.entity.Task;
import az.code.tensapi.entity.User;
import az.code.tensapi.repository.NotificationRepository;
import az.code.tensapi.repository.TaskRepository;
import az.code.tensapi.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final EmailService emailService;
    private final NotificationRepository notificationRepository;
    private final TaskRepository taskRepository;

    @Scheduled(cron = "0 0 12 * * ?")
    public void sendTaskNotification() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Task> tasks = taskRepository.findByDeadline(tomorrow);
        for (Task task : tasks) {
            for (User user : task.getAccounts()) {
                Notification notification = new Notification();
                notification.setMessage("Task " + task.getName() + " deadline is tomorrow.");
                notification.setTimestamp(LocalDateTime.now());
                notification.setStatus("unread");
                notification.setUser(user);
                notificationRepository.save(notification);
                emailService.sentMailMessage(user.getEmail(), notification.getMessage());
            }
        }
    }
}
