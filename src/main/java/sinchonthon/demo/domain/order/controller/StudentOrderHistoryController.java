package sinchonthon.demo.domain.order.controller;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sinchonthon.demo.domain.discovery.auth.CurrentMemberProvider;
import sinchonthon.demo.domain.discovery.entity.*;
import sinchonthon.demo.domain.order.service.StudentOrderHistoryService;
import sinchonthon.demo.global.response.ApiResponse;
@RestController @RequestMapping("/api/v1/orders") @RequiredArgsConstructor public class StudentOrderHistoryController { private final StudentOrderHistoryService service; private final CurrentMemberProvider current; @GetMapping public ApiResponse<List<Summary>> list(){return ApiResponse.success(service.list(current.currentMemberId()).stream().map(Summary::from).toList());} @GetMapping("/{id}") public ApiResponse<Detail> get(@PathVariable Long id){return ApiResponse.success(Detail.from(service.get(current.currentMemberId(),id)));} public record Summary(Long orderId,String storeName,String orderNumber,String status,String pickupLocation,int totalAmount,LocalDateTime paidAt){static Summary from(DraftOrder d){return new Summary(d.getId(),d.getRecruitmentSlot().getStore().getName(),d.getDraftOrderNumber(),d.getRecruitmentSlot().getStatus().name(),d.getPickupLocation().getDisplayName(),d.getTotalAmount(),d.getPaidAt());}} public record Detail(Long orderId,String storeName,String orderNumber,String status,String pickupLocation,int totalAmount,List<Item> items){static Detail from(DraftOrder d){return new Detail(d.getId(),d.getRecruitmentSlot().getStore().getName(),d.getDraftOrderNumber(),d.getRecruitmentSlot().getStatus().name(),d.getPickupLocation().getDisplayName(),d.getTotalAmount(),d.getItems().stream().map(Item::from).toList());}} public record Item(String menuName,int unitPrice,int quantity){static Item from(DraftOrderItem i){return new Item(i.getMenuName(),i.getUnitPrice(),i.getQuantity());}} }
