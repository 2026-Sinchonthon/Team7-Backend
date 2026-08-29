package sinchonthon.demo.domain.order.controller;

import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sinchonthon.demo.domain.discovery.entity.DraftOrderItem;
import sinchonthon.demo.domain.discovery.entity.DraftOrderStatus;
import sinchonthon.demo.domain.discovery.repository.DraftOrderRepository;
import sinchonthon.demo.domain.store.RecruitmentSlot;
import sinchonthon.demo.domain.store.RecruitmentSlotRepository;
import sinchonthon.demo.domain.store.RecruitmentSlotStatus;
import sinchonthon.demo.domain.store.StoreRepository;
import sinchonthon.demo.global.exception.BusinessException;
import sinchonthon.demo.global.response.ApiResponse;
import sinchonthon.demo.global.response.GeneralErrorCode;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/owner/stores/{storeId}/slots/{slotId}/order-sheet")
public class OwnerOrderSheetController {
    private final StoreRepository storeRepository;
    private final RecruitmentSlotRepository slotRepository;
    private final DraftOrderRepository draftOrderRepository;

    @GetMapping
    @Transactional(readOnly = true)
    public ApiResponse<OrderSheetResponse> get(
            @RequestHeader("X-Member-Id") @Positive Long ownerId,
            @PathVariable @Positive Long storeId,
            @PathVariable @Positive Long slotId
    ) {
        var store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException(GeneralErrorCode.STORE_NOT_FOUND));
        if (!store.getOwnerId().equals(ownerId)) {
            throw new BusinessException(GeneralErrorCode.FORBIDDEN);
        }
        RecruitmentSlot slot = slotRepository.findByIdAndStoreId(slotId, storeId)
                .orElseThrow(() -> new BusinessException(GeneralErrorCode.SLOT_NOT_FOUND));
        if (slot.getStatus() != RecruitmentSlotStatus.CONFIRMED) {
            throw new BusinessException(GeneralErrorCode.ORDER_SHEET_NOT_READY);
        }

        List<OrderSummary> orders = draftOrderRepository
                .findAllByRecruitmentSlotIdAndStatusOrderByIdAsc(slotId, DraftOrderStatus.PAID)
                .stream().map(OrderSummary::from).toList();
        int total = orders.stream().mapToInt(OrderSummary::totalAmount).sum();
        return ApiResponse.success(new OrderSheetResponse(slotId, slot.getStatus().name(), orders, total));
    }

    public record OrderSheetResponse(Long slotId, String status, List<OrderSummary> orders, int totalOrderAmount) {}

    public record OrderSummary(Long orderId, String orderNumber, Long studentId, int totalAmount, List<Item> items) {
        static OrderSummary from(sinchonthon.demo.domain.discovery.entity.DraftOrder order) {
            return new OrderSummary(order.getId(), order.getDraftOrderNumber(), order.getStudent().getId(),
                    order.getTotalAmount(), order.getItems().stream().map(Item::from).toList());
        }
    }

    public record Item(Long menuId, String menuName, int unitPrice, int quantity, int lineAmount) {
        static Item from(DraftOrderItem item) {
            return new Item(item.getMenuId(), item.getMenuName(), item.getUnitPrice(), item.getQuantity(),
                    item.getUnitPrice() * item.getQuantity());
        }
    }
}
