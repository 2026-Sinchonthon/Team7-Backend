package sinchonthon.demo.domain.discovery.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sinchonthon.demo.domain.discovery.dto.CreateDraftOrderRequest;
import sinchonthon.demo.domain.discovery.dto.MenuItemRequest;
import sinchonthon.demo.domain.discovery.dto.UpdateDraftSelectionRequest;
import sinchonthon.demo.domain.discovery.entity.DraftOrder;
import sinchonthon.demo.domain.discovery.entity.ParticipantGroup;
import sinchonthon.demo.domain.discovery.entity.PickupLocation;
import sinchonthon.demo.domain.discovery.entity.Recruitment;
import sinchonthon.demo.domain.discovery.entity.RestaurantMenu;
import sinchonthon.demo.domain.discovery.repository.DraftOrderRepository;
import sinchonthon.demo.domain.discovery.repository.ParticipantGroupRepository;
import sinchonthon.demo.domain.discovery.repository.PickupLocationRepository;
import sinchonthon.demo.domain.discovery.repository.RecruitmentRepository;
import sinchonthon.demo.domain.discovery.repository.RestaurantMenuRepository;
import sinchonthon.demo.domain.member.entity.Member;
import sinchonthon.demo.domain.member.entity.MemberRole;
import sinchonthon.demo.domain.member.repository.MemberRepository;
import sinchonthon.demo.global.exception.BusinessException;
import sinchonthon.demo.global.response.GeneralErrorCode;

@Service
@RequiredArgsConstructor
public class DiscoveryService {
    private final RecruitmentRepository recruitmentRepository;
    private final RestaurantMenuRepository menuRepository;
    private final ParticipantGroupRepository groupRepository;
    private final PickupLocationRepository pickupLocationRepository;
    private final DraftOrderRepository draftOrderRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<Recruitment> getRecruitments() { return recruitmentRepository.findAll(); }

    @Transactional(readOnly = true)
    public Recruitment getRecruitment(Long id) { return recruitmentRepository.findById(id).orElseThrow(() -> new BusinessException(GeneralErrorCode.NOT_FOUND)); }

    @Transactional(readOnly = true)
    public List<RestaurantMenu> getMenus(Long restaurantId) { return menuRepository.findAllByRestaurantId(restaurantId); }

    @Transactional(readOnly = true)
    public List<ParticipantGroup> getGroups(Long recruitmentId) { return groupRepository.findAllByRecruitmentId(recruitmentId); }

    @Transactional(readOnly = true)
    public List<PickupLocation> getPickupLocations(Long recruitmentId) { return pickupLocationRepository.findAllByRecruitmentId(recruitmentId); }

    @Transactional
    public DraftOrder createDraft(Long memberId, Long recruitmentId, CreateDraftOrderRequest request) {
        Member student = student(memberId);
        Recruitment recruitment = getRecruitment(recruitmentId);
        Map<Long, Integer> quantities = quantities(request.items());
        List<RestaurantMenu> menus = menuRepository.findAllByIdIn(quantities.keySet());
        if (menus.size() != quantities.size() || menus.stream().anyMatch(menu -> !menu.isAvailable() || !menu.getRestaurant().getId().equals(recruitment.getRestaurant().getId()))) {
            throw new BusinessException(GeneralErrorCode.INVALID_REQUEST);
        }
        int total = menus.stream().mapToInt(menu -> menu.getPrice() * quantities.get(menu.getId())).sum();
        DraftOrder draft = draftOrderRepository.save(new DraftOrder(student, recruitment, total));
        menus.forEach(menu -> draft.addItem(menu, quantities.get(menu.getId())));
        draft.setDraftOrderNumber(String.format("DRAFT-%08d", draft.getId()));
        return draft;
    }

    @Transactional
    public DraftOrder updateSelection(Long memberId, Long draftId, UpdateDraftSelectionRequest request) {
        DraftOrder draft = draftOrderRepository.findByIdAndStudentId(draftId, memberId).orElseThrow(() -> new BusinessException(GeneralErrorCode.NOT_FOUND));
        ParticipantGroup group = groupRepository.findById(request.participantGroupId()).orElseThrow(() -> new BusinessException(GeneralErrorCode.NOT_FOUND));
        PickupLocation location = pickupLocationRepository.findById(request.pickupLocationId()).orElseThrow(() -> new BusinessException(GeneralErrorCode.NOT_FOUND));
        if (!group.getRecruitment().getId().equals(draft.getRecruitment().getId()) || !location.getRecruitment().getId().equals(draft.getRecruitment().getId())) {
            throw new BusinessException(GeneralErrorCode.INVALID_REQUEST);
        }
        draft.select(group, location);
        return draft;
    }

    private Member student(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(GeneralErrorCode.UNAUTHORIZED));
        if (member.getRole() != MemberRole.STUDENT) throw new BusinessException(GeneralErrorCode.FORBIDDEN);
        return member;
    }
    private Map<Long, Integer> quantities(List<MenuItemRequest> items) {
        Map<Long, Integer> quantities = new HashMap<>();
        for (MenuItemRequest item : items) if (quantities.putIfAbsent(item.menuId(), item.quantity()) != null) throw new BusinessException(GeneralErrorCode.INVALID_REQUEST);
        return quantities;
    }
}
