package me.hwanse.springreststudy.account;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
/**
 * SpEL 로 현재 인증된 사용자의 Principal 을 참조한다. 즉 Authentication 의 principal 객체를 참조함
 * - 인증된 사용자가 있을 경우엔  AccountAdapter(User 를 상속받은)를 정보를 가져올 수 있음
 * - 인증되지 않는 사용자일 경우 'anonymousUser' 라는 이름의 문자열이 default 값이다.
 */
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")
public @interface CurrentAccount {

}
