package me.hwanse.springreststudy.config;

import lombok.RequiredArgsConstructor;
import me.hwanse.springreststudy.account.AccountService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final AccountService accountService;

  private final PasswordEncoder passwordEncoder;

  @Bean
  public TokenStore tokenStore() {
    return new InMemoryTokenStore();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(accountService)
        .passwordEncoder(passwordEncoder);
  }

  /**
   * HttpSecurity 검증 단계로 넘어가기 전에 Security Filter Chain 을 거치지 않고 싶다면
   * WebSecurity configure 를 통해 적용할지 말지에 대한 여부를 설정 필요
   */
  @Override
  public void configure(WebSecurity web) throws Exception {
    // Rest Docs 에는 Security 를 적용하지 않는다
    // Spring Boot 의 정적 리소스에 Security 를 적용하지 않는다.
    web.ignoring()
       .mvcMatchers("/docs/index.html")
       .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .anonymous()
      .and()
      .formLogin()
      .and()
      .authorizeRequests()
        .mvcMatchers(HttpMethod.GET, "/api/**").anonymous()
        .anyRequest().authenticated()
    ;

  }

}
