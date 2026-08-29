package sinchonthon.demo.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import sinchonthon.demo.domain.store.PickupLocation;

public record RecruitmentSlotCreateRequest(
        @NotBlank @Size(max = 100) String title,
        @Size(max = 500) String content,
        @Min(4) @Max(8) int targetParticipantCount,
        @Min(0) @Max(30) int discountRate,
        @NotNull PickupLocation pickupLocation,
        @NotNull @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime pickupAt
) {}
