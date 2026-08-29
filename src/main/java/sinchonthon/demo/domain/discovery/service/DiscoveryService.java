package sinchonthon.demo.domain.discovery.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sinchonthon.demo.domain.discovery.dto.CreateDraftOrderRequest;
import sinchonthon.demo.domain.discovery.dto.MenuItemRequest;
import sinchonthon.demo.domain.discovery.entity.DraftOrder;
import sinchonthon.demo.domain.discovery.entity.DraftOrderStatus;
import sinchonthon.demo.domain.discovery.repository.DraftOrderRepository;
import sinchonthon.demo.domain.member.entity.Member;
import sinchonthon.demo.domain.member.entity.MemberRole;
import sinchonthon.demo.domain.member.repository.MemberRepository;
import sinchonthon.demo.domain.store.Menu;
import sinchonthon.demo.domain.store.RecruitmentSlot;
import sinchonthon.demo.domain.store.RecruitmentSlotRepository;
import sinchonthon.demo.domain.store.RecruitmentSlotStatus;
import sinchonthon.demo.global.exception.BusinessException;
import sinchonthon.demo.global.response.GeneralErrorCode;

@Service
@RequiredArgsConstructor
public class DiscoveryService {
    private final RecruitmentSlotRepository slotRepository;
    private final DraftOrderRepository draftOrderRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<RecruitmentSlot> list() { return slotRepository.findAllByStatusOrderByPickupAtAsc(RecruitmentSlotStatus.RECRUITING); }
    @Transactional(readOnly = true)
    public RecruitmentSlot get(Long id) { return slotRepository.findById(id).filter(slot -> slot.getStatus() == RecruitmentSlotStatus.RECRUITING).orElseThrow(() -> new BusinessException(GeneralErrorCode.SLOT_NOT_FOUND)); }
    @Transactional
    public DraftOrder create(Long memberId, Long slotId, CreateDraftOrderRequest request) {
        Member student = student(memberId); RecruitmentSlot slot = get(slotId); Map<Long, Integer> quantities = quantities(request.items());
        List<Menu> menus = slot.getStore().getMenus().stream().filter(menu -> quantities.containsKey(menu.getId())).toList();
        if (menus.size() != quantities.size()) throw new BusinessException(GeneralErrorCode.INVALID_REQUEST);
        int total = menus.stream().mapToInt(menu -> Math.toIntExact(menu.getPrice()) * quantities.get(menu.getId())).sum();
        DraftOrder order = draftOrderRepository.save(new DraftOrder(student, slot, total));
        menus.forEach(menu -> order.addItem(menu, quantities.get(menu.getId()))); order.setDraftOrderNumber("DRAFT-" + order.getId()); return order;
    }
    @Transactional
    public DraftOrder pay(Long memberId, Long slotId, Long orderId) {
        Member student = student(memberId); DraftOrder order = draftOrderRepository.findByIdAndStudentId(orderId, student.getId()).orElseThrow(() -> new BusinessException(GeneralErrorCode.NOT_FOUND));
        if (!order.getRecruitmentSlot().getId().equals(slotId) || order.getStatus() != DraftOrderStatus.DRAFT) throw new BusinessException(GeneralErrorCode.INVALID_REQUEST);
        get(slotId).participate(); order.pay(); return order;
    }
    private Member student(Long id) { Member member = memberRepository.findById(id).orElseThrow(() -> new BusinessException(GeneralErrorCode.UNAUTHORIZED)); if (member.getRole() != MemberRole.STUDENT) throw new BusinessException(GeneralErrorCode.FORBIDDEN); return member; }
    private Map<Long, Integer> quantities(List<MenuItemRequest> items) { Map<Long, Integer> result = new HashMap<>(); for (MenuItemRequest item : items) if (result.putIfAbsent(item.menuId(), item.quantity()) != null) throw new BusinessException(GeneralErrorCode.INVALID_REQUEST); return result; }
}
