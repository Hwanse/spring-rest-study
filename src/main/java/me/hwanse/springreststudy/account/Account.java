package me.hwanse.springreststudy.account;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class Account {

  @Id
  @GeneratedValue
  private Long id;

  @Column(unique = true)
  private String email;

  private String password;

  @ElementCollection(fetch = FetchType.EAGER) // account 의 key를 외래키로 포함하는 account_rols (sub table)를 만든다
  @Enumerated(EnumType.STRING)
  private Set<AccountRole> roles;

}
