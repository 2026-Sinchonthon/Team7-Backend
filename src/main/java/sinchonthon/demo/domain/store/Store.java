package sinchonthon.demo.domain.store;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "stores")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long ownerId;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StoreCategory category;

    @Column(nullable = false, length = 100)
    private String shortIntroduction;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<Menu> menus = new ArrayList<>();

    private Store(
            Long ownerId,
            String name,
            StoreCategory category,
            String shortIntroduction,
            String description,
            String address,
            String phoneNumber
    ) {
        this.ownerId = ownerId;
        this.name = name;
        this.category = category;
        this.shortIntroduction = shortIntroduction;
        this.description = description;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public static Store create(
            Long ownerId,
            String name,
            StoreCategory category,
            String shortIntroduction,
            String description,
            String address,
            String phoneNumber
    ) {
        return new Store(ownerId, name, category, shortIntroduction, description, address, phoneNumber);
    }

    public void addMenu(Menu menu) {
        menus.add(menu);
        menu.assignStore(this);
    }

    public List<Menu> getMenus() {
        return Collections.unmodifiableList(menus);
    }
}
