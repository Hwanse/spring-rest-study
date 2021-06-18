package me.hwanse.springreststudy.account;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface AccountRepository extends JpaRepository<Account, Long> {

  Optional<Account> findByEmail(String username);

}
