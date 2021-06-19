package me.hwanse.springreststudy.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.function.Supplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {

  @Autowired
  AccountService accountService;

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Test
  @DisplayName("find user test")
  public void findByUsername() throws Exception {
    // given
    String username = "hwanse@email.com";
    String password = "hwanse";
    Account account = Account
      .builder()
      .email(username)
      .password(password)
      .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
      .build();

    accountService.saveAccount(account);

    // when
    UserDetailsService userDetailsService = (UserDetailsService) accountService;
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    // then
    assertThat(passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
  }

  @Test
  @DisplayName("username not found test")
  public void findByUsernameFail() {
    // given
    String username = "hwanse@email.com";

    // when & then
    UsernameNotFoundException exception =
      assertThrows(UsernameNotFoundException.class,
                   () -> accountService.loadUserByUsername(username));

    assertThat(exception.getMessage()).contains(username);
  }

}