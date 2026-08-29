package sinchonthon.demo.domain.order.entity;

public enum DeliveryStatus {
    PAYMENT_PENDING("결제 대기중"),
    DELIVERY_PENDING("배달 대기중"),
    DELIVERING("배달 중"),
    DELIVERED("배달 완료"),
    CANCELLED("주문 취소");

    private final String displayName;
    DeliveryStatus(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}
