package sinchonthon.demo.domain.store;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentSlotRepository extends JpaRepository<RecruitmentSlot, Long> {
    boolean existsByStoreIdAndStatus(Long storeId, RecruitmentSlotStatus status);
    List<RecruitmentSlot> findAllByStoreIdOrderByPickupAtDesc(Long storeId);
    Optional<RecruitmentSlot> findByIdAndStoreId(Long id, Long storeId);
    List<RecruitmentSlot> findAllByStatusAndDeadlineAtBefore(RecruitmentSlotStatus status, LocalDateTime time);
}
