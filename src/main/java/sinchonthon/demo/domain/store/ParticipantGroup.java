package sinchonthon.demo.domain.store;

public enum ParticipantGroup {
    YONSEI("연세대학교"), EWHA("이화여자대학교"), SOGANG("서강대학교"), HONGIK("홍익대학교"), MYONGJI("명지대학교");
    private final String displayName;
    ParticipantGroup(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}
