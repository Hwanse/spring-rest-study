package me.hwanse.springreststudy.events;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class EventControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  EventRepository eventRepository;

  @Test
  public void createEvent() throws Exception {
    // given
    Event event = Event
      .builder()
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
      .build();

    // when
    event.setId(10L);
    when(eventRepository.save(event)).thenReturn(event);

    // then
    mockMvc.perform(post("/api/events")
                      .contentType(MediaType.APPLICATION_JSON_VALUE)
                      .accept(MediaTypes.HAL_JSON)
                      .content(objectMapper.writeValueAsString(event)))
           .andDo(print())
           .andExpect(status().isCreated())
           .andExpect(jsonPath("id").exists());
  }

}
