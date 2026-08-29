package sinchonthon.demo.domain.discovery.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruitment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(nullable = false)
    private Restaurant restaurant;
    private int participantCount;
    private int targetParticipantCount;
    private LocalDateTime orderDeadlineAt;

    public Recruitment(Restaurant restaurant, int participantCount, int targetParticipantCount, LocalDateTime orderDeadlineAt) {
        this.restaurant = restaurant;
        this.participantCount = participantCount;
        this.targetParticipantCount = targetParticipantCount;
        this.orderDeadlineAt = orderDeadlineAt;
    }
}
