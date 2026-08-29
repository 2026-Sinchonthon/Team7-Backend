package sinchonthon.demo.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sinchonthon.demo.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByLoginId(String loginId);
}
