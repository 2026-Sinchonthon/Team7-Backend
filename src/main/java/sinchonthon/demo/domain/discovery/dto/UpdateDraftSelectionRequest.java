package sinchonthon.demo.domain.discovery.dto;
import jakarta.validation.constraints.NotBlank;
public record UpdateDraftSelectionRequest(@NotBlank String participantGroup) {}
