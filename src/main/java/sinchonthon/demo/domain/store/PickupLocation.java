package sinchonthon.demo.domain.store;

public enum PickupLocation {
    YONSEI_MAIN_GATE("연세대 정문"), EWHA_MAIN_GATE("이화여대 정문"), SOGANG_MAIN_GATE("서강대 정문"),
    MYONGJI_MAIN_GATE("명지대 정문"), HONGIK_MAIN_GATE("홍익대 정문");

    private final String displayName;
    PickupLocation(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}
