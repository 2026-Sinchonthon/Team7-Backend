package sinchonthon.demo.global.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sinchonthon.demo.global.response.ApiResponse;

@RestController
public class HealthController {

    @GetMapping("/")
    public ApiResponse<String> root() {
        return ApiResponse.success("Team7 Backend is running.");
    }

    @GetMapping("/health")
    public ApiResponse<String> health() {
        return ApiResponse.success("OK");
    }
}
