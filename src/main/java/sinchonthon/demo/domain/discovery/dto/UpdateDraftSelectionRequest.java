package sinchonthon.demo.domain.discovery.dto;
import jakarta.validation.constraints.NotNull;
public record UpdateDraftSelectionRequest(@NotNull Long participantGroupId, @NotNull Long pickupLocationId) {}
