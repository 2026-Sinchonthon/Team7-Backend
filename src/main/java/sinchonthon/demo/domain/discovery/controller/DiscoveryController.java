package sinchonthon.demo.domain.discovery.controller;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sinchonthon.demo.domain.discovery.auth.CurrentMemberProvider;
import sinchonthon.demo.domain.discovery.dto.*;
import sinchonthon.demo.domain.discovery.entity.DraftOrder;
import sinchonthon.demo.domain.discovery.service.DiscoveryService;
import sinchonthon.demo.domain.store.*;
import sinchonthon.demo.global.response.ApiResponse;
@RestController @RequestMapping("/api/v1") @RequiredArgsConstructor
public class DiscoveryController {
 private final DiscoveryService service; private final CurrentMemberProvider current;
 @GetMapping("/recruitments") public ApiResponse<List<Summary>> list(){return ApiResponse.success(service.list().stream().map(Summary::from).toList());}
 @GetMapping("/recruitments/{id}") public ApiResponse<Detail> get(@PathVariable Long id){return ApiResponse.success(Detail.from(service.get(id)));}
 @PostMapping("/recruitments/{id}/draft-orders") public ApiResponse<Draft> create(@PathVariable Long id,@Valid @RequestBody CreateDraftOrderRequest r){return ApiResponse.success(Draft.from(service.create(current.currentMemberId(),id,r)));}
 @PatchMapping("/draft-orders/{id}/selection") public ApiResponse<Draft> select(@PathVariable Long id,@Valid @RequestBody UpdateDraftSelectionRequest r){return ApiResponse.success(Draft.from(service.select(current.currentMemberId(),id,r)));}
 @PostMapping("/recruitments/{slotId}/draft-orders/{draftId}/payment") public ApiResponse<Draft> pay(@PathVariable Long slotId,@PathVariable Long draftId){return ApiResponse.success(Draft.from(service.pay(current.currentMemberId(),slotId,draftId)));}
 public record Summary(Long recruitmentId,String storeName,int participantCount,int targetParticipantCount){static Summary from(RecruitmentSlot s){return new Summary(s.getId(),s.getStore().getName(),s.getCurrentParticipantCount(),s.getTargetParticipantCount());}}
 public record Detail(Long recruitmentId,String storeName,String description,List<MenuInfo> menus,List<String> participantGroups,List<String> pickupLocations){static Detail from(RecruitmentSlot s){return new Detail(s.getId(),s.getStore().getName(),s.getStore().getDescription(),s.getStore().getMenus().stream().map(MenuInfo::from).toList(),java.util.Arrays.stream(ParticipantGroup.values()).map(Enum::name).toList(),List.of(s.getPickupLocation().name()));}}
 public record MenuInfo(Long menuId,String name,long price){static MenuInfo from(Menu m){return new MenuInfo(m.getId(),m.getName(),m.getPrice());}}
 public record Draft(Long draftOrderId,String number,String status,int totalAmount,String participantGroup,String pickupLocation){static Draft from(DraftOrder d){return new Draft(d.getId(),d.getDraftOrderNumber(),d.getStatus().name(),d.getTotalAmount(),d.getParticipantGroup()==null?null:d.getParticipantGroup().name(),d.getPickupLocation()==null?null:d.getPickupLocation().name());}}
}
