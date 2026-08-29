package sinchonthon.demo.domain.order.controller;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sinchonthon.demo.domain.discovery.auth.CurrentMemberProvider;
import sinchonthon.demo.domain.discovery.entity.DraftOrder;
import sinchonthon.demo.domain.discovery.entity.DraftOrderItem;
import sinchonthon.demo.domain.order.service.StudentOrderHistoryService;
import sinchonthon.demo.global.response.ApiResponse;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class StudentOrderHistoryController {
    private final StudentOrderHistoryService orderHistoryService;
    private final CurrentMemberProvider currentMemberProvider;

    @GetMapping
    public ApiResponse<List<OrderSummaryResponse>> getOrders() {
        Long memberId = currentMemberProvider.currentMemberId();
        List<OrderSummaryResponse> response = orderHistoryService.list(memberId).stream()
                .map(OrderSummaryResponse::from)
                .toList();
        return ApiResponse.success(response);
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderDetailResponse> getOrder(@PathVariable Long orderId) {
        Long memberId = currentMemberProvider.currentMemberId();
        return ApiResponse.success(OrderDetailResponse.from(orderHistoryService.get(memberId, orderId)));
    }

    public record OrderSummaryResponse(
            Long orderId,
            String storeName,
            String orderNumber,
            String status,
            String pickupLocation,
            int totalAmount,
            LocalDateTime paidAt
    ) {
        static OrderSummaryResponse from(DraftOrder order) {
            return new OrderSummaryResponse(
                    order.getId(),
                    order.getRecruitmentSlot().getStore().getName(),
                    order.getDraftOrderNumber(),
                    order.getRecruitmentSlot().getStatus().name(),
                    order.getPickupLocation().getDisplayName(),
                    order.getTotalAmount(),
                    order.getPaidAt()
            );
        }
    }

    public record OrderDetailResponse(
            Long orderId,
            String storeName,
            String orderNumber,
            String status,
            String pickupLocation,
            int totalAmount,
            List<OrderItemResponse> items
    ) {
        static OrderDetailResponse from(DraftOrder order) {
            return new OrderDetailResponse(
                    order.getId(),
                    order.getRecruitmentSlot().getStore().getName(),
                    order.getDraftOrderNumber(),
                    order.getRecruitmentSlot().getStatus().name(),
                    order.getPickupLocation().getDisplayName(),
                    order.getTotalAmount(),
                    order.getItems().stream().map(OrderItemResponse::from).toList()
            );
        }
    }

    public record OrderItemResponse(String menuName, int unitPrice, int quantity) {
        static OrderItemResponse from(DraftOrderItem item) {
            return new OrderItemResponse(item.getMenuName(), item.getUnitPrice(), item.getQuantity());
        }
    }
}
