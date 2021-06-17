package me.hwanse.springreststudy.common;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class RestDocsConfiguration {

  /**
   * RestDocsMockMvcConfigurationCustomizer 커스텀
   * 아래와 같은 방식으로 Rest Docs Snippets 문서들의 문자열 출력 형태를 이쁘게 표현하기 위한 설정이 가능하다
   */
  @Bean
  public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
    return configurer -> configurer.operationPreprocessors()
                                   .withRequestDefaults(prettyPrint())
                                   .withResponseDefaults(prettyPrint());
  }

}
