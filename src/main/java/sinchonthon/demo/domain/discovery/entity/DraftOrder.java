package sinchonthon.demo.domain.discovery.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sinchonthon.demo.domain.member.entity.Member;
import sinchonthon.demo.domain.store.RecruitmentSlot;
import sinchonthon.demo.domain.store.PickupLocation;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DraftOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(nullable = false)
    private Member student;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(nullable = false)
    private RecruitmentSlot recruitmentSlot;
    private String draftOrderNumber;
    @Enumerated(EnumType.STRING) private DraftOrderStatus status;
    private int totalAmount;
    @Enumerated(EnumType.STRING) private PickupLocation pickupLocation;
    private LocalDateTime paidAt;
    @OneToMany(mappedBy = "draftOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<DraftOrderItem> items = new ArrayList<>();

    public DraftOrder(Member student, RecruitmentSlot recruitmentSlot, int totalAmount) {
        this.student = student;
        this.recruitmentSlot = recruitmentSlot;
        this.pickupLocation = recruitmentSlot.getPickupLocation();
        this.totalAmount = totalAmount;
        this.status = DraftOrderStatus.DRAFT;
    }
    public void setDraftOrderNumber(String value) { this.draftOrderNumber = value; }
    public void pay() { this.status = DraftOrderStatus.PAID; this.paidAt = LocalDateTime.now(); }
    public void addItem(sinchonthon.demo.domain.store.Menu menu, int quantity) { items.add(new DraftOrderItem(this, menu, quantity)); }
}
