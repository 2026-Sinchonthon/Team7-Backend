package sinchonthon.demo.domain.store;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sinchonthon.demo.domain.store.dto.MenuCreateRequest;
import sinchonthon.demo.domain.store.dto.StoreCreateRequest;
import sinchonthon.demo.domain.store.dto.StoreResponse;
import sinchonthon.demo.global.exception.BusinessException;
import sinchonthon.demo.global.response.GeneralErrorCode;
import sinchonthon.demo.domain.store.dto.MenuResponse;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    @Transactional
    public StoreResponse create(Long ownerId, StoreCreateRequest request) {
        if (storeRepository.existsByOwnerId(ownerId)) {
            throw new BusinessException(GeneralErrorCode.STORE_ALREADY_EXISTS);
        }

        validateRepresentativeMenu(request.menus());

        Store store = Store.create(
                ownerId,
                request.name(),
                request.category(),
                request.shortIntroduction(),
                request.description(),
                request.address(),
                request.phoneNumber()
        );

        request.menus().stream()
                .map(this::toMenu)
                .forEach(store::addMenu);

        return StoreResponse.from(storeRepository.save(store));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> getMenus(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException(GeneralErrorCode.STORE_NOT_FOUND));
        return store.getMenus().stream().map(MenuResponse::from).toList();
    }

    private void validateRepresentativeMenu(List<MenuCreateRequest> menus) {
        long representativeCount = menus.stream()
                .filter(MenuCreateRequest::representative)
                .count();

        if (representativeCount != 1) {
            throw new BusinessException(GeneralErrorCode.INVALID_REPRESENTATIVE_MENU);
        }
    }

    private Menu toMenu(MenuCreateRequest request) {
        return Menu.create(
                request.name(),
                request.price(),
                request.description(),
                request.representative()
        );
    }
}
