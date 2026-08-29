package sinchonthon.demo.domain.order.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentOrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(nullable = false)
    private StudentOrder studentOrder;
    private Long menuId;
    private String menuName;
    private int unitPrice;
    private int quantity;

    StudentOrderItem(StudentOrder studentOrder, Long menuId, String menuName, int unitPrice, int quantity) {
        this.studentOrder = studentOrder;
        this.menuId = menuId; this.menuName = menuName; this.unitPrice = unitPrice; this.quantity = quantity;
    }
}
