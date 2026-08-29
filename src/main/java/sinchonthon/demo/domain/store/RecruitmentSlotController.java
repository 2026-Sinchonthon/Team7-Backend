package sinchonthon.demo.domain.store;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sinchonthon.demo.domain.store.dto.RecruitmentSlotCreateRequest;
import sinchonthon.demo.domain.store.dto.RecruitmentSlotResponse;
import sinchonthon.demo.global.response.ApiResponse;
import sinchonthon.demo.global.response.GeneralSuccessCode;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/owner/stores/{storeId}/slots")
public class RecruitmentSlotController {
    private final RecruitmentSlotService service;

    @PostMapping
    public ResponseEntity<ApiResponse<RecruitmentSlotResponse>> create(@RequestHeader("X-Member-Id") @Positive Long ownerId,
                                                                         @PathVariable @Positive Long storeId,
                                                                         @Valid @RequestBody RecruitmentSlotCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(GeneralSuccessCode.CREATED,
                service.create(ownerId, storeId, request)));
    }

    @GetMapping
    public ApiResponse<List<RecruitmentSlotResponse>> list(@RequestHeader("X-Member-Id") @Positive Long ownerId,
                                                            @PathVariable @Positive Long storeId) {
        return ApiResponse.success(service.list(ownerId, storeId));
    }

    @GetMapping("/{slotId}")
    public ApiResponse<RecruitmentSlotResponse> get(@RequestHeader("X-Member-Id") @Positive Long ownerId,
                                                     @PathVariable @Positive Long storeId, @PathVariable @Positive Long slotId) {
        return ApiResponse.success(service.get(ownerId, storeId, slotId));
    }

    @DeleteMapping("/{slotId}")
    public ApiResponse<Void> cancel(@RequestHeader("X-Member-Id") @Positive Long ownerId,
                                     @PathVariable @Positive Long storeId, @PathVariable @Positive Long slotId) {
        service.cancel(ownerId, storeId, slotId);
        return ApiResponse.success();
    }
}
