package me.hwanse.springreststudy.config;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;
import me.hwanse.springreststudy.account.Account;
import me.hwanse.springreststudy.account.AccountRole;
import me.hwanse.springreststudy.account.AccountService;
import me.hwanse.springreststudy.common.AppProperties;
import me.hwanse.springreststudy.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthServerConfigTest extends BaseControllerTest {

  @Autowired
  AccountService accountService;

  @Autowired
  AppProperties appProperties;

  @Test
  @DisplayName("인증 토큰 발급")
  public void getAuthToken() throws Exception {
    // when & then
    mockMvc.perform(post("/oauth/token")
                    .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
                    .param("username", appProperties.getUserUsername())
                    .param("password", appProperties.getUserPassword())
                    .param("grant_type", "password"))
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("access_token").exists())
    ;
  }

}