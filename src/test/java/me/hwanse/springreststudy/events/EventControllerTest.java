package me.hwanse.springreststudy.events;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Test
  public void createEvent() throws Exception {
    // given
    Event event = Event
      .builder()
      .id(100L)
      .name("Spring")
      .description("REST API Development with Spring")
      .beginEnrollmentDateTime(LocalDateTime.of(2021, 6, 15, 22, 50))
      .closeEnrollmentDateTime(LocalDateTime.of(2021, 6, 16, 22, 50))
      .beginEventDateTime(LocalDateTime.of(2021, 6, 17, 12, 0))
      .endEventDateTime(LocalDateTime.of(2021, 6, 18, 12, 0))
      .basePrice(100)
      .maxPrice(200)
      .limitOfEnrollment(100)
      .location("강남")
      .free(true)
      .offline(false)
      .eventStatus(EventStatus.PUBLISHED)
      .build();

    // then
    mockMvc.perform(post("/api/events")
                      .contentType(MediaType.APPLICATION_JSON_VALUE)
                      .accept(MediaTypes.HAL_JSON)
                      .content(objectMapper.writeValueAsString(event)))
           .andDo(print())
           .andExpect(status().isCreated())
           .andExpect(jsonPath("id").exists())
           .andExpect(header().exists(HttpHeaders.LOCATION))
           .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
           .andExpect(jsonPath("id").value(Matchers.not(100L)))
           .andExpect(jsonPath("free").value(Matchers.not(true)))
           .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
    ;

  }

}
