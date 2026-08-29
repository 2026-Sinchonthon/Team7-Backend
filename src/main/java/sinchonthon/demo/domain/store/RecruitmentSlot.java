package sinchonthon.demo.domain.store;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sinchonthon.demo.global.exception.BusinessException;
import sinchonthon.demo.global.response.GeneralErrorCode;

@Getter
@Entity
@Table(name = "recruitment_slots")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentSlot {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(nullable = false)
    private Store store;
    @Column(nullable = false, length = 100) private String title;
    @Column(length = 500) private String content;
    @Column(nullable = false) private int targetParticipantCount;
    @Column(nullable = false) private int currentParticipantCount;
    @Column(nullable = false) private int discountRate;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30) private PickupLocation pickupLocation;
    @Column(nullable = false) private LocalDateTime pickupAt;
    @Column(nullable = false) private LocalDateTime deadlineAt;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private RecruitmentSlotStatus status;

    private RecruitmentSlot(Store store, String title, String content, int targetParticipantCount,
                             int discountRate, PickupLocation pickupLocation, LocalDateTime pickupAt) {
        this.store = store; this.title = title; this.content = content;
        this.targetParticipantCount = targetParticipantCount; this.currentParticipantCount = 0;
        this.discountRate = discountRate; this.pickupLocation = pickupLocation; this.pickupAt = pickupAt;
        this.deadlineAt = pickupAt.minusMinutes(30); this.status = RecruitmentSlotStatus.RECRUITING;
    }

    public static RecruitmentSlot create(Store store, String title, String content, int targetParticipantCount,
                                         int discountRate, PickupLocation pickupLocation, LocalDateTime pickupAt) {
        return new RecruitmentSlot(store, title, content, targetParticipantCount, discountRate, pickupLocation, pickupAt);
    }

    public void cancel() { this.status = RecruitmentSlotStatus.CANCELED; }
    public void fail() { this.status = RecruitmentSlotStatus.FAILED; }
    public void participate() {
        if (this.status != RecruitmentSlotStatus.RECRUITING) throw new BusinessException(GeneralErrorCode.SLOT_NOT_RECRUITING);
        if (this.currentParticipantCount >= this.targetParticipantCount) throw new BusinessException(GeneralErrorCode.SLOT_ALREADY_FULL);
        this.currentParticipantCount++;
        if (this.currentParticipantCount >= this.targetParticipantCount) this.status = RecruitmentSlotStatus.CONFIRMED;
    }
}
