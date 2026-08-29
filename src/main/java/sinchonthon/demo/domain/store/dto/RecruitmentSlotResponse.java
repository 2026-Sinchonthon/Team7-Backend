package sinchonthon.demo.domain.store.dto;

import java.time.LocalDateTime;
import sinchonthon.demo.domain.store.RecruitmentSlot;
import sinchonthon.demo.domain.store.RecruitmentSlotStatus;

public record RecruitmentSlotResponse(Long slotId, Long storeId, String title, String content,
                                      int targetParticipantCount, int currentParticipantCount, int discountRate,
                                      String pickupLocation, LocalDateTime pickupAt, LocalDateTime deadlineAt,
                                      RecruitmentSlotStatus status) {
    public static RecruitmentSlotResponse from(RecruitmentSlot slot) {
        return new RecruitmentSlotResponse(slot.getId(), slot.getStore().getId(), slot.getTitle(), slot.getContent(),
                slot.getTargetParticipantCount(), slot.getCurrentParticipantCount(), slot.getDiscountRate(),
                slot.getPickupLocation().name(), slot.getPickupAt(), slot.getDeadlineAt(), slot.getStatus());
    }
}
