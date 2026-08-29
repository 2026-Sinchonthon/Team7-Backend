package sinchonthon.demo.domain.order.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sinchonthon.demo.domain.discovery.entity.DraftOrder;
import sinchonthon.demo.domain.discovery.entity.DraftOrderStatus;
import sinchonthon.demo.domain.discovery.repository.DraftOrderRepository;
import sinchonthon.demo.global.exception.BusinessException;
import sinchonthon.demo.global.response.GeneralErrorCode;

@Service
@RequiredArgsConstructor
public class StudentOrderHistoryService {
    private final DraftOrderRepository draftOrderRepository;

    @Transactional(readOnly = true)
    public List<DraftOrder> list(Long studentId) {
        return draftOrderRepository.findAllByStudentIdAndStatusOrderByPaidAtDesc(studentId, DraftOrderStatus.PAID);
    }

    @Transactional(readOnly = true)
    public DraftOrder get(Long studentId, Long orderId) {
        return draftOrderRepository.findByIdAndStudentId(orderId, studentId)
                .filter(order -> order.getStatus() == DraftOrderStatus.PAID)
                .orElseThrow(() -> new BusinessException(GeneralErrorCode.NOT_FOUND));
    }
}
