package sinchonthon.demo.domain.discovery.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sinchonthon.demo.domain.discovery.entity.PickupLocation;
public interface PickupLocationRepository extends JpaRepository<PickupLocation, Long> { List<PickupLocation> findAllByRecruitmentId(Long recruitmentId); }
