package sinchonthon.demo.domain.discovery.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sinchonthon.demo.domain.discovery.entity.ParticipantGroup;
public interface ParticipantGroupRepository extends JpaRepository<ParticipantGroup, Long> { List<ParticipantGroup> findAllByRecruitmentId(Long recruitmentId); }
