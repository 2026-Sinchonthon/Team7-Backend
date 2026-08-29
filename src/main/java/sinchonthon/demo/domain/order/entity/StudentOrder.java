package sinchonthon.demo.domain.order.entity;

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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sinchonthon.demo.domain.discovery.entity.Restaurant;
import sinchonthon.demo.domain.member.entity.Member;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(nullable = false)
    private Member student;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(nullable = false)
    private Restaurant restaurant;
    private String orderNumber;
    @Enumerated(EnumType.STRING) private DeliveryStatus deliveryStatus;
    private String pickupLocationName;
    private int totalAmount;
    private LocalDateTime orderedAt;
    @OneToMany(mappedBy = "studentOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<StudentOrderItem> items = new ArrayList<>();

    public StudentOrder(Member student, Restaurant restaurant, String orderNumber, String pickupLocationName, int totalAmount) {
        this.student = student;
        this.restaurant = restaurant;
        this.orderNumber = orderNumber;
        this.pickupLocationName = pickupLocationName;
        this.totalAmount = totalAmount;
        this.deliveryStatus = DeliveryStatus.PAYMENT_PENDING;
        this.orderedAt = LocalDateTime.now();
    }
    public void addItem(Long menuId, String menuName, int unitPrice, int quantity) { items.add(new StudentOrderItem(this, menuId, menuName, unitPrice, quantity)); }
}
