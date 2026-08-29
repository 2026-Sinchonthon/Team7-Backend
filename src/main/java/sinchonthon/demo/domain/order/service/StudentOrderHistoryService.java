package sinchonthon.demo.domain.order.service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sinchonthon.demo.domain.discovery.entity.*;
import sinchonthon.demo.domain.discovery.repository.DraftOrderRepository;
import sinchonthon.demo.global.exception.*;
import sinchonthon.demo.global.response.GeneralErrorCode;
@Service @RequiredArgsConstructor public class StudentOrderHistoryService { private final DraftOrderRepository drafts; @Transactional(readOnly=true) public List<DraftOrder> list(Long studentId){return drafts.findAllByStudentIdAndStatusOrderByPaidAtDesc(studentId,DraftOrderStatus.PAID);} @Transactional(readOnly=true) public DraftOrder get(Long studentId,Long id){return drafts.findByIdAndStudentId(id,studentId).filter(d->d.getStatus()==DraftOrderStatus.PAID).orElseThrow(()->new BusinessException(GeneralErrorCode.NOT_FOUND));} }
