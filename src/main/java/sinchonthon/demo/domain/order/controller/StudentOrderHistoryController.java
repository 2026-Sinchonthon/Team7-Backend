package sinchonthon.demo.domain.order.controller;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sinchonthon.demo.domain.discovery.auth.CurrentMemberProvider;
import sinchonthon.demo.domain.order.entity.StudentOrder;
import sinchonthon.demo.domain.order.entity.StudentOrderItem;
import sinchonthon.demo.domain.order.service.StudentOrderHistoryService;
import sinchonthon.demo.global.response.ApiResponse;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class StudentOrderHistoryController {
    private final StudentOrderHistoryService service;
    private final CurrentMemberProvider currentMemberProvider;

    @GetMapping
    public ApiResponse<List<OrderSummaryResponse>> getOrders() {
        Long studentId = currentMemberProvider.currentMemberId();
        return ApiResponse.success(service.getOrders(studentId).stream().map(OrderSummaryResponse::from).toList());
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderDetailResponse> getOrder(@PathVariable Long orderId) {
        Long studentId = currentMemberProvider.currentMemberId();
        return ApiResponse.success(OrderDetailResponse.from(service.getOrder(studentId, orderId)));
    }

    public record OrderSummaryResponse(Long orderId, String restaurantName, String orderNumber, String deliveryStatus, String deliveryStatusLabel, int totalAmount, LocalDateTime orderedAt) {
        static OrderSummaryResponse from(StudentOrder order) {
            return new OrderSummaryResponse(order.getId(), order.getRestaurant().getName(), order.getOrderNumber(), order.getDeliveryStatus().name(), order.getDeliveryStatus().getDisplayName(), order.getTotalAmount(), order.getOrderedAt());
        }
    }

    public record OrderDetailResponse(Long orderId, String restaurantName, String orderNumber, String deliveryStatus, String deliveryStatusLabel, String pickupLocation, int totalAmount, LocalDateTime orderedAt, List<OrderItemResponse> items) {
        static OrderDetailResponse from(StudentOrder order) {
            return new OrderDetailResponse(order.getId(), order.getRestaurant().getName(), order.getOrderNumber(), order.getDeliveryStatus().name(), order.getDeliveryStatus().getDisplayName(), order.getPickupLocationName(), order.getTotalAmount(), order.getOrderedAt(), order.getItems().stream().map(OrderItemResponse::from).toList());
        }
    }

    public record OrderItemResponse(Long menuId, String menuName, int unitPrice, int quantity, int lineAmount) {
        static OrderItemResponse from(StudentOrderItem item) {
            return new OrderItemResponse(item.getMenuId(), item.getMenuName(), item.getUnitPrice(), item.getQuantity(), item.getUnitPrice() * item.getQuantity());
        }
    }
}
