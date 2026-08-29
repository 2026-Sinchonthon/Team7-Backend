package sinchonthon.demo.domain.discovery.auth;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sinchonthon.demo.global.exception.BusinessException;
import sinchonthon.demo.global.response.GeneralErrorCode;
@Component
public class CurrentMemberProvider {
    public Long currentMemberId() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) throw new BusinessException(GeneralErrorCode.UNAUTHORIZED);
        try { return Long.valueOf(attrs.getRequest().getHeader("X-Member-Id")); }
        catch (RuntimeException exception) { throw new BusinessException(GeneralErrorCode.UNAUTHORIZED); }
    }
}
