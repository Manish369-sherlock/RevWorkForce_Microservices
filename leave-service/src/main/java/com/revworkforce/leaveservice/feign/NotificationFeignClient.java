package com.revworkforce.leaveservice.feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@FeignClient(name = "NOTIFICATION-SERVICE", path = "/api/notifications")
public interface NotificationFeignClient {
    @PostMapping("/send")
    void sendNotification(@RequestBody Map<String, Object> notification);
}
