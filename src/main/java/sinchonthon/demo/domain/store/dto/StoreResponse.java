package sinchonthon.demo.domain.store.dto;

import java.util.List;
import sinchonthon.demo.domain.store.Store;
import sinchonthon.demo.domain.store.StoreCategory;

public record StoreResponse(
        Long storeId,
        Long ownerId,
        String name,
        StoreCategory category,
        String shortIntroduction,
        String description,
        String address,
        String phoneNumber,
        List<MenuResponse> menus
) {
    public static StoreResponse from(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getOwnerId(),
                store.getName(),
                store.getCategory(),
                store.getShortIntroduction(),
                store.getDescription(),
                store.getAddress(),
                store.getPhoneNumber(),
                store.getMenus().stream().map(MenuResponse::from).toList()
        );
    }
}
