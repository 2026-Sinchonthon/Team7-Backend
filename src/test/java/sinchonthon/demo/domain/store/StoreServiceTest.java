package sinchonthon.demo.domain.store;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sinchonthon.demo.domain.store.dto.MenuCreateRequest;
import sinchonthon.demo.domain.store.dto.StoreCreateRequest;
import sinchonthon.demo.domain.store.dto.StoreResponse;
import sinchonthon.demo.global.exception.BusinessException;
import sinchonthon.demo.global.response.GeneralErrorCode;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class StoreServiceTest {
    @Autowired
    private StoreService storeService;

    @Test
    void createsStoreWithMenus() {
        StoreResponse response = storeService.create(1L, validRequest());

        assertNotNull(response.storeId());
        assertEquals(1L, response.ownerId());
        assertEquals("신촌마라탕", response.name());
        assertEquals(StoreCategory.CHINESE, response.category());
        assertEquals(2, response.menus().size());
        assertEquals(1, response.menus().stream().filter(menu -> menu.representative()).count());
    }

    @Test
    void rejectsSecondStoreForSameOwner() {
        storeService.create(1L, validRequest());

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> storeService.create(1L, validRequest())
        );

        assertEquals(GeneralErrorCode.STORE_ALREADY_EXISTS, exception.getErrorCode());
    }

    @Test
    void requiresExactlyOneRepresentativeMenu() {
        StoreCreateRequest request = new StoreCreateRequest(
                "신촌마라탕",
                StoreCategory.CHINESE,
                "매일 직접 끓이는 마라탕",
                "신촌 대학생을 위한 마라탕 전문점입니다.",
                "서울 서대문구 연세로 1",
                "02-1234-5678",
                List.of(
                        new MenuCreateRequest("마라탕", 10_000, "기본 마라탕", false),
                        new MenuCreateRequest("꿔바로우", 15_000, "바삭한 꿔바로우", false)
                )
        );

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> storeService.create(2L, request)
        );

        assertEquals(GeneralErrorCode.INVALID_REPRESENTATIVE_MENU, exception.getErrorCode());
    }

    private StoreCreateRequest validRequest() {
        return new StoreCreateRequest(
                "신촌마라탕",
                StoreCategory.CHINESE,
                "매일 직접 끓이는 마라탕",
                "신촌 대학생을 위한 마라탕 전문점입니다.",
                "서울 서대문구 연세로 1",
                "02-1234-5678",
                List.of(
                        new MenuCreateRequest("마라탕", 10_000, "기본 마라탕", true),
                        new MenuCreateRequest("꿔바로우", 15_000, "바삭한 꿔바로우", false)
                )
        );
    }
}
