package me.hwanse.springreststudy.account;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {

  @Autowired
  AccountService accountService;

  @Autowired
  AccountRepository accountRepository;

  @Test
  @DisplayName("")
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

    accountRepository.save(account);

    // when
    UserDetailsService userDetailsService = (UserDetailsService) accountService;
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    // then
    Assertions.assertThat(userDetails.getPassword()).isEqualTo(password);
  }

}