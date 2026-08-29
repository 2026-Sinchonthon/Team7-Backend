package sinchonthon.demo.domain.store;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sinchonthon.demo.domain.store.dto.MenuResponse;
import sinchonthon.demo.global.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores")
public class StoreMenuController {
    private final StoreService storeService;

    @GetMapping("/{storeId}/menus")
    public ApiResponse<List<MenuResponse>> getMenus(@PathVariable Long storeId) {
        return ApiResponse.success(storeService.getMenus(storeId));
    }
}
