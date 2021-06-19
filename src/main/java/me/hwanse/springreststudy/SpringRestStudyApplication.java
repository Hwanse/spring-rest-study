package me.hwanse.springreststudy;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringRestStudyApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringRestStudyApplication.class, args);
  }

}
