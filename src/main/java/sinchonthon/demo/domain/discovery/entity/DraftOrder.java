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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sinchonthon.demo.domain.member.entity.Member;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DraftOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(nullable = false)
    private Member student;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(nullable = false)
    private Recruitment recruitment;
    private String draftOrderNumber;
    @Enumerated(EnumType.STRING) private DraftOrderStatus status;
    private int totalAmount;
    @ManyToOne(fetch = FetchType.LAZY) private ParticipantGroup participantGroup;
    @ManyToOne(fetch = FetchType.LAZY) private PickupLocation pickupLocation;
    @OneToMany(mappedBy = "draftOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<DraftOrderItem> items = new ArrayList<>();

    public DraftOrder(Member student, Recruitment recruitment, int totalAmount) {
        this.student = student;
        this.recruitment = recruitment;
        this.totalAmount = totalAmount;
        this.status = DraftOrderStatus.DRAFT;
    }
    public void setDraftOrderNumber(String value) { this.draftOrderNumber = value; }
    public void select(ParticipantGroup group, PickupLocation location) { this.participantGroup = group; this.pickupLocation = location; }
    public void addItem(RestaurantMenu menu, int quantity) { items.add(new DraftOrderItem(this, menu, quantity)); }
}
