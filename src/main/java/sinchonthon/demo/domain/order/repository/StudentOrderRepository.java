package sinchonthon.demo.domain.order.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import sinchonthon.demo.domain.order.entity.StudentOrder;

public interface StudentOrderRepository extends JpaRepository<StudentOrder, Long> {
    List<StudentOrder> findAllByStudentIdOrderByOrderedAtDesc(Long studentId);
    Optional<StudentOrder> findByIdAndStudentId(Long id, Long studentId);
}
