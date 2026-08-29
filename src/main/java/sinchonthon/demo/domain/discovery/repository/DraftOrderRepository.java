package sinchonthon.demo.domain.discovery.repository;
import java.util.Optional;
import java.util.List;
import sinchonthon.demo.domain.discovery.entity.DraftOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import sinchonthon.demo.domain.discovery.entity.DraftOrder;
public interface DraftOrderRepository extends JpaRepository<DraftOrder, Long> { Optional<DraftOrder> findByIdAndStudentId(Long id, Long studentId); List<DraftOrder> findAllByStudentIdAndStatusOrderByPaidAtDesc(Long studentId, DraftOrderStatus status); }
