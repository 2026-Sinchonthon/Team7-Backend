package sinchonthon.demo.domain.discovery.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
public record MenuItemRequest(@NotNull Long menuId, @NotNull @Min(1) Integer quantity) {}
