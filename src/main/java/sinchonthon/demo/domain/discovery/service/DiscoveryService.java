package sinchonthon.demo.domain.discovery.service;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sinchonthon.demo.domain.discovery.dto.*;
import sinchonthon.demo.domain.discovery.entity.*;
import sinchonthon.demo.domain.discovery.repository.DraftOrderRepository;
import sinchonthon.demo.domain.member.entity.*;
import sinchonthon.demo.domain.member.repository.MemberRepository;
import sinchonthon.demo.domain.store.*;
import sinchonthon.demo.global.exception.BusinessException;
import sinchonthon.demo.global.response.GeneralErrorCode;
@Service @RequiredArgsConstructor
public class DiscoveryService {
 private final RecruitmentSlotRepository slots; private final DraftOrderRepository drafts; private final MemberRepository members;
 @Transactional(readOnly=true) public List<RecruitmentSlot> list(){return slots.findAllByStatusOrderByPickupAtAsc(RecruitmentSlotStatus.RECRUITING);}
 @Transactional(readOnly=true) public RecruitmentSlot get(Long id){return slots.findById(id).filter(s->s.getStatus()==RecruitmentSlotStatus.RECRUITING).orElseThrow(()->new BusinessException(GeneralErrorCode.SLOT_NOT_FOUND));}
 @Transactional public DraftOrder create(Long memberId,Long slotId,CreateDraftOrderRequest req){RecruitmentSlot slot=get(slotId); Member student=student(memberId); Map<Long,Integer> q=quantities(req.items()); List<Menu> menus=slot.getStore().getMenus().stream().filter(m->q.containsKey(m.getId())).toList(); if(menus.size()!=q.size())throw new BusinessException(GeneralErrorCode.INVALID_REQUEST); int total=menus.stream().mapToInt(m->Math.toIntExact(m.getPrice())*q.get(m.getId())).sum(); DraftOrder d=drafts.save(new DraftOrder(student,slot,total));menus.forEach(m->d.addItem(m,q.get(m.getId())));d.setDraftOrderNumber(String.format("DRAFT-%08d",d.getId()));return d;}
 @Transactional public DraftOrder select(Long memberId,Long draftId,UpdateDraftSelectionRequest req){DraftOrder d=owned(memberId,draftId); try{d.select(ParticipantGroup.valueOf(req.participantGroup()),d.getRecruitmentSlot().getPickupLocation());}catch(IllegalArgumentException e){throw new BusinessException(GeneralErrorCode.INVALID_REQUEST);}return d;}
 @Transactional public DraftOrder pay(Long memberId,Long slotId,Long draftId){DraftOrder d=owned(memberId,draftId);if(!d.getRecruitmentSlot().getId().equals(slotId)||d.getStatus()!=DraftOrderStatus.DRAFT)throw new BusinessException(GeneralErrorCode.INVALID_REQUEST);RecruitmentSlot s=get(slotId);s.participate();d.pay();return d;}
 private DraftOrder owned(Long memberId,Long id){return drafts.findByIdAndStudentId(id,memberId).orElseThrow(()->new BusinessException(GeneralErrorCode.NOT_FOUND));}
 private Member student(Long id){Member m=members.findById(id).orElseThrow(()->new BusinessException(GeneralErrorCode.UNAUTHORIZED));if(m.getRole()!=MemberRole.STUDENT)throw new BusinessException(GeneralErrorCode.FORBIDDEN);return m;}
 private Map<Long,Integer> quantities(List<MenuItemRequest> items){Map<Long,Integer> r=new HashMap<>();for(MenuItemRequest i:items)if(r.putIfAbsent(i.menuId(),i.quantity())!=null)throw new BusinessException(GeneralErrorCode.INVALID_REQUEST);return r;}
}
