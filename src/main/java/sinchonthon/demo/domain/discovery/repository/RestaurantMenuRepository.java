package sinchonthon.demo.domain.discovery.repository;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sinchonthon.demo.domain.discovery.entity.RestaurantMenu;
public interface RestaurantMenuRepository extends JpaRepository<RestaurantMenu, Long> {
    List<RestaurantMenu> findAllByRestaurantId(Long restaurantId);
    List<RestaurantMenu> findAllByIdIn(Collection<Long> ids);
}
