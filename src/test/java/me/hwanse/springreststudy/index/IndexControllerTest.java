package me.hwanse.springreststudy.index;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import me.hwanse.springreststudy.common.RestDocsConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 모든 홈페이지에는 index 페이지 즉 첫 home 화면이 존재하듯이
 * Rest API도 진입점이라는 것을 알리는 Index API가 필요하다
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
public class IndexControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  @DisplayName("index API 테스트")
  public void index() throws Exception {
    mockMvc.perform(get("/api/"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("_links.events").exists())
           .andDo(document("index",
                           links(
                             linkWithRel("events").description("link to events query")
                           ),
                           responseFields(
                             fieldWithPath("_links.events.*").ignored()
                           )
                  ))
    ;
  }

}
