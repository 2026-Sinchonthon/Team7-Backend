package sinchonthon.demo.domain.store.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MenuCreateRequest(
        @NotBlank(message = "메뉴명은 필수입니다.")
        @Size(max = 60, message = "메뉴명은 60자 이하여야 합니다.")
        String name,

        @Min(value = 1, message = "메뉴 가격은 1원 이상이어야 합니다.")
        @Max(value = 1_000_000, message = "메뉴 가격은 1,000,000원 이하여야 합니다.")
        long price,

        @NotBlank(message = "메뉴 설명은 필수입니다.")
        @Size(max = 300, message = "메뉴 설명은 300자 이하여야 합니다.")
        String description,

        boolean representative
) {
}
