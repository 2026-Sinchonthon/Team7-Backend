package sinchonthon.demo.domain.store;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByOwnerId(Long ownerId);

    Optional<Store> findByOwnerId(Long ownerId);
}
