package sinchonthon.demo.domain.store;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sinchonthon.demo.domain.store.dto.StoreCreateRequest;
import sinchonthon.demo.domain.store.dto.StoreResponse;
import sinchonthon.demo.global.response.ApiResponse;
import sinchonthon.demo.global.response.GeneralSuccessCode;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/owner/stores")
public class StoreController {
    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<ApiResponse<StoreResponse>> create(
            @RequestHeader("X-Member-Id") @Positive(message = "회원 ID는 양수여야 합니다.") Long ownerId,
            @Valid @RequestBody StoreCreateRequest request
    ) {
        StoreResponse response = storeService.create(ownerId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(GeneralSuccessCode.CREATED, response));
    }

}
