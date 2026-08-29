package sinchonthon.demo.domain.discovery.entity;

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
public class DraftOrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(nullable = false)
    private DraftOrder draftOrder;
    private Long menuId;
    private String menuName;
    private int unitPrice;
    private int quantity;

    DraftOrderItem(DraftOrder draftOrder, sinchonthon.demo.domain.store.Menu menu, int quantity) {
        this.draftOrder = draftOrder;
        this.menuId = menu.getId(); this.menuName = menu.getName(); this.unitPrice = Math.toIntExact(menu.getPrice()); this.quantity = quantity;
    }
}
