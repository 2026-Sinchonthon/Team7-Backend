package sinchonthon.demo.domain.store.dto;

import sinchonthon.demo.domain.store.Menu;

public record MenuResponse(
        Long menuId,
        String name,
        long price,
        String description,
        boolean representative
) {
    public static MenuResponse from(Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getDescription(),
                menu.isRepresentative()
        );
    }
}
