package sinchonthon.demo.domain.order.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sinchonthon.demo.domain.order.entity.StudentOrder;
import sinchonthon.demo.domain.order.repository.StudentOrderRepository;
import sinchonthon.demo.global.exception.BusinessException;
import sinchonthon.demo.global.response.GeneralErrorCode;

@Service
@RequiredArgsConstructor
public class StudentOrderHistoryService {
    private final StudentOrderRepository studentOrderRepository;

    @Transactional(readOnly = true)
    public List<StudentOrder> getOrders(Long studentId) {
        return studentOrderRepository.findAllByStudentIdOrderByOrderedAtDesc(studentId);
    }

    @Transactional(readOnly = true)
    public StudentOrder getOrder(Long studentId, Long orderId) {
        return studentOrderRepository.findByIdAndStudentId(orderId, studentId)
                .orElseThrow(() -> new BusinessException(GeneralErrorCode.NOT_FOUND));
    }
}
