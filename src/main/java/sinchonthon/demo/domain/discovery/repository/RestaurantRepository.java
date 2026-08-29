package sinchonthon.demo.domain.discovery.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import sinchonthon.demo.domain.discovery.entity.Restaurant;
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {}
