package sinchonthon.demo.domain.discovery.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
public record CreateDraftOrderRequest(@NotEmpty List<@Valid MenuItemRequest> items) {}
