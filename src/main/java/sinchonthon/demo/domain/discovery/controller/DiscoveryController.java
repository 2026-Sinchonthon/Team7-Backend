package sinchonthon.demo.domain.discovery.controller;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sinchonthon.demo.domain.discovery.auth.CurrentMemberProvider;
import sinchonthon.demo.domain.discovery.dto.CreateDraftOrderRequest;
import sinchonthon.demo.domain.discovery.dto.UpdateDraftSelectionRequest;
import sinchonthon.demo.domain.discovery.entity.DraftOrder;
import sinchonthon.demo.domain.discovery.entity.DraftOrderItem;
import sinchonthon.demo.domain.discovery.entity.ParticipantGroup;
import sinchonthon.demo.domain.discovery.entity.PickupLocation;
import sinchonthon.demo.domain.discovery.entity.Recruitment;
import sinchonthon.demo.domain.discovery.entity.RestaurantMenu;
import sinchonthon.demo.domain.discovery.service.DiscoveryService;
import sinchonthon.demo.global.response.ApiResponse;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DiscoveryController {
    private final DiscoveryService service;
    private final CurrentMemberProvider currentMemberProvider;

    @GetMapping("/recruitments")
    public ApiResponse<List<RecruitmentSummary>> recruitments() {
        return ApiResponse.success(service.getRecruitments().stream().map(RecruitmentSummary::from).toList());
    }

    @GetMapping("/recruitments/{id}")
    public ApiResponse<RecruitmentDetailResponse> recruitment(@PathVariable Long id) {
        Recruitment recruitment = service.getRecruitment(id);
        return ApiResponse.success(RecruitmentDetailResponse.from(recruitment, service.getMenus(recruitment.getRestaurant().getId()), service.getGroups(id), service.getPickupLocations(id)));
    }

    @PostMapping("/recruitments/{id}/draft-orders")
    public ApiResponse<DraftOrderResponse> createDraft(@PathVariable Long id, @Valid @RequestBody CreateDraftOrderRequest request) {
        return ApiResponse.success(DraftOrderResponse.from(service.createDraft(currentMemberProvider.currentMemberId(), id, request)));
    }

    @PatchMapping("/draft-orders/{id}/selection")
    public ApiResponse<DraftOrderResponse> selection(@PathVariable Long id, @Valid @RequestBody UpdateDraftSelectionRequest request) {
        return ApiResponse.success(DraftOrderResponse.from(service.updateSelection(currentMemberProvider.currentMemberId(), id, request)));
    }

    public record RecruitmentSummary(Long recruitmentId, Long restaurantId, String restaurantName, String imageUrl, int participantCount, int targetParticipantCount, LocalDateTime orderDeadlineAt) {
        static RecruitmentSummary from(Recruitment value) { return new RecruitmentSummary(value.getId(), value.getRestaurant().getId(), value.getRestaurant().getName(), value.getRestaurant().getImageUrl(), value.getParticipantCount(), value.getTargetParticipantCount(), value.getOrderDeadlineAt()); }
    }
    public record RecruitmentDetailResponse(Long recruitmentId, RestaurantResponse restaurant, List<MenuResponse> menus, List<OptionResponse> participantGroups, List<OptionResponse> pickupLocations) {
        static RecruitmentDetailResponse from(Recruitment r, List<RestaurantMenu> menus, List<ParticipantGroup> groups, List<PickupLocation> locations) { return new RecruitmentDetailResponse(r.getId(), RestaurantResponse.from(r), menus.stream().map(MenuResponse::from).toList(), groups.stream().map(group -> new OptionResponse(group.getId(), group.getName())).toList(), locations.stream().map(location -> new OptionResponse(location.getId(), location.getName())).toList()); }
    }
    public record RestaurantResponse(Long restaurantId, String name, String description, String imageUrl) {
        static RestaurantResponse from(Recruitment r) { return new RestaurantResponse(r.getRestaurant().getId(), r.getRestaurant().getName(), r.getRestaurant().getDescription(), r.getRestaurant().getImageUrl()); }
    }
    public record MenuResponse(Long menuId, String name, int price, String imageUrl, boolean isAvailable) {
        static MenuResponse from(RestaurantMenu menu) { return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getImageUrl(), menu.isAvailable()); }
    }
    public record OptionResponse(Long id, String name) {}
    public record DraftOrderResponse(Long draftOrderId, String draftOrderNumber, String status, int totalAmount, List<DraftItemResponse> items, OptionResponse participantGroup, OptionResponse pickupLocation) {
        static DraftOrderResponse from(DraftOrder draft) { return new DraftOrderResponse(draft.getId(), draft.getDraftOrderNumber(), draft.getStatus().name(), draft.getTotalAmount(), draft.getItems().stream().map(DraftItemResponse::from).toList(), draft.getParticipantGroup() == null ? null : new OptionResponse(draft.getParticipantGroup().getId(), draft.getParticipantGroup().getName()), draft.getPickupLocation() == null ? null : new OptionResponse(draft.getPickupLocation().getId(), draft.getPickupLocation().getName())); }
    }
    public record DraftItemResponse(Long menuId, String menuName, int unitPrice, int quantity, int lineAmount) {
        static DraftItemResponse from(DraftOrderItem item) { return new DraftItemResponse(item.getMenuId(), item.getMenuName(), item.getUnitPrice(), item.getQuantity(), item.getUnitPrice() * item.getQuantity()); }
    }
}
