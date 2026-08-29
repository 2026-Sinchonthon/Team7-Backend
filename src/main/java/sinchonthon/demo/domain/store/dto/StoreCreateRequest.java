package sinchonthon.demo.domain.store.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import sinchonthon.demo.domain.store.StoreCategory;

public record StoreCreateRequest(
        @NotBlank(message = "상점명은 필수입니다.")
        @Size(max = 50, message = "상점명은 50자 이하여야 합니다.")
        String name,

        @NotNull(message = "업종은 필수입니다.")
        StoreCategory category,

        @NotBlank(message = "한 줄 소개는 필수입니다.")
        @Size(max = 100, message = "한 줄 소개는 100자 이하여야 합니다.")
        String shortIntroduction,

        @NotBlank(message = "상세 소개는 필수입니다.")
        @Size(max = 2_000, message = "상세 소개는 2,000자 이하여야 합니다.")
        String description,

        @NotBlank(message = "주소는 필수입니다.")
        @Size(max = 255, message = "주소는 255자 이하여야 합니다.")
        String address,

        @NotBlank(message = "전화번호는 필수입니다.")
        @Pattern(regexp = "^[0-9-]{9,20}$", message = "전화번호는 숫자와 하이픈만 사용할 수 있습니다.")
        String phoneNumber,

        @NotNull(message = "메뉴 목록은 필수입니다.")
        @Size(min = 1, max = 5, message = "메뉴는 1개 이상 5개 이하로 등록해야 합니다.")
        List<@Valid MenuCreateRequest> menus
) {
}
