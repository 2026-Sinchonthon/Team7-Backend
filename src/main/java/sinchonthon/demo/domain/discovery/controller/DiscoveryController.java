package sinchonthon.demo.domain.discovery.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sinchonthon.demo.domain.discovery.auth.CurrentMemberProvider;
import sinchonthon.demo.domain.discovery.dto.CreateDraftOrderRequest;
import sinchonthon.demo.domain.discovery.entity.*;
import sinchonthon.demo.domain.discovery.service.DiscoveryService;
import sinchonthon.demo.domain.store.*;
import sinchonthon.demo.global.response.ApiResponse;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DiscoveryController {
    private final DiscoveryService discoveryService;
    private final CurrentMemberProvider currentMemberProvider;

    @GetMapping("/recruitments")
    public ApiResponse<List<Summary>> getRecruitments() {
        return ApiResponse.success(discoveryService.list().stream().map(Summary::from).toList());
    }

    @GetMapping("/recruitments/{recruitmentId}")
    public ApiResponse<Detail> getRecruitment(@PathVariable Long recruitmentId) {
        return ApiResponse.success(Detail.from(discoveryService.get(recruitmentId)));
    }

    @PostMapping("/recruitments/{recruitmentId}/draft-orders")
    public ApiResponse<Draft> createDraftOrder(@PathVariable Long recruitmentId, @RequestBody CreateDraftOrderRequest request) {
        return ApiResponse.success(Draft.from(discoveryService.create(currentMemberProvider.currentMemberId(), recruitmentId, request)));
    }

    @PostMapping("/recruitments/{recruitmentId}/draft-orders/{draftOrderId}/payment")
    public ApiResponse<Draft> pay(@PathVariable Long recruitmentId, @PathVariable Long draftOrderId) {
        return ApiResponse.success(Draft.from(discoveryService.pay(currentMemberProvider.currentMemberId(), recruitmentId, draftOrderId)));
    }

    public record Summary(Long recruitmentId, String storeName) {
        static Summary from(RecruitmentSlot slot) { return new Summary(slot.getId(), slot.getStore().getName()); }
    }
    public record Detail(Long recruitmentId, String storeName, String description, List<MenuResponse> menus) {
        static Detail from(RecruitmentSlot slot) { return new Detail(slot.getId(), slot.getStore().getName(), slot.getStore().getDescription(), slot.getStore().getMenus().stream().map(MenuResponse::from).toList()); }
    }
    public record MenuResponse(Long menuId, String name, long price) {
        static MenuResponse from(Menu menu) { return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice()); }
    }
    public record Draft(Long draftOrderId, String storeName, String orderNumber, String status, int totalAmount, List<Item> items) {
        static Draft from(DraftOrder order) { return new Draft(order.getId(), order.getRecruitmentSlot().getStore().getName(), order.getDraftOrderNumber(), order.getStatus().name(), order.getTotalAmount(), order.getItems().stream().map(Item::from).toList()); }
    }
    public record Item(String menuName, int unitPrice, int quantity) {
        static Item from(DraftOrderItem item) { return new Item(item.getMenuName(), item.getUnitPrice(), item.getQuantity()); }
    }
}
