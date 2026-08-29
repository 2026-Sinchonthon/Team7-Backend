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
public class RestaurantMenu {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(nullable = false)
    private Restaurant restaurant;
    private String name;
    private int price;
    private String imageUrl;
    private boolean available;

    public RestaurantMenu(Restaurant restaurant, String name, int price, String imageUrl, boolean available) {
        this.restaurant = restaurant;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.available = available;
    }
}
