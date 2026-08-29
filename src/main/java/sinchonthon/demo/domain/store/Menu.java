package sinchonthon.demo.domain.store;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "menus")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(nullable = false)
    private long price;

    @Column(nullable = false, length = 300)
    private String description;

    @Column(nullable = false)
    private boolean representative;

    private Menu(String name, long price, String description, boolean representative) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.representative = representative;
    }

    public static Menu create(String name, long price, String description, boolean representative) {
        return new Menu(name, price, description, representative);
    }

    void assignStore(Store store) {
        this.store = store;
    }
}
