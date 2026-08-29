package sinchonthon.demo.domain.store;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import sinchonthon.demo.domain.store.dto.RecruitmentSlotCreateRequest;
import sinchonthon.demo.domain.store.dto.RecruitmentSlotResponse;
import sinchonthon.demo.global.exception.BusinessException;
import sinchonthon.demo.global.response.GeneralErrorCode;

@Service
@RequiredArgsConstructor
public class RecruitmentSlotService {
    private final StoreRepository storeRepository;
    private final RecruitmentSlotRepository slotRepository;

    @Transactional
    public RecruitmentSlotResponse create(Long ownerId, Long storeId, RecruitmentSlotCreateRequest request) {
        Store store = ownedStore(ownerId, storeId);
        if (slotRepository.existsByStoreIdAndStatus(storeId, RecruitmentSlotStatus.RECRUITING)) {
            throw new BusinessException(GeneralErrorCode.ACTIVE_SLOT_ALREADY_EXISTS);
        }
        LocalDateTime pickupAt = request.pickupAt();
        if (pickupAt.getMinute() != 0 && pickupAt.getMinute() != 30 || pickupAt.getSecond() != 0 || pickupAt.getNano() != 0) {
            throw new BusinessException(GeneralErrorCode.INVALID_SLOT_TIME);
        }
        if (!pickupAt.isAfter(LocalDateTime.now().plusMinutes(30))) {
            throw new BusinessException(GeneralErrorCode.INVALID_SLOT_TIME);
        }
        return RecruitmentSlotResponse.from(slotRepository.save(RecruitmentSlot.create(store, request.title(), request.content(),
                request.targetParticipantCount(), request.discountRate(), request.pickupLocation(), pickupAt)));
    }

    @Transactional(readOnly = true)
    public List<RecruitmentSlotResponse> list(Long ownerId, Long storeId) {
        ownedStore(ownerId, storeId);
        return slotRepository.findAllByStoreIdOrderByPickupAtDesc(storeId).stream().map(RecruitmentSlotResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public RecruitmentSlotResponse get(Long ownerId, Long storeId, Long slotId) {
        ownedStore(ownerId, storeId);
        return RecruitmentSlotResponse.from(slotRepository.findByIdAndStoreId(slotId, storeId)
                .orElseThrow(() -> new BusinessException(GeneralErrorCode.SLOT_NOT_FOUND)));
    }

    @Transactional
    public void cancel(Long ownerId, Long storeId, Long slotId) {
        ownedStore(ownerId, storeId);
        RecruitmentSlot slot = slotRepository.findByIdAndStoreId(slotId, storeId)
                .orElseThrow(() -> new BusinessException(GeneralErrorCode.SLOT_NOT_FOUND));
        if (slot.getStatus() != RecruitmentSlotStatus.RECRUITING || slot.getCurrentParticipantCount() != 0) {
            throw new BusinessException(GeneralErrorCode.SLOT_CANCEL_NOT_ALLOWED);
        }
        slot.cancel();
    }

    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void failExpiredSlots() {
        slotRepository.findAllByStatusAndDeadlineAtBefore(RecruitmentSlotStatus.RECRUITING, LocalDateTime.now())
                .forEach(RecruitmentSlot::fail);
    }

    private Store ownedStore(Long ownerId, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException(GeneralErrorCode.STORE_NOT_FOUND));
        if (!store.getOwnerId().equals(ownerId)) throw new BusinessException(GeneralErrorCode.FORBIDDEN);
        return store;
    }
}
