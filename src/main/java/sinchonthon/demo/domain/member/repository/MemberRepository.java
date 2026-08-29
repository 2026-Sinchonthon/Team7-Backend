package sinchonthon.demo.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sinchonthon.demo.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByLoginId(String loginId);

    Optional<Member> findByLoginId(String loginId);
}
