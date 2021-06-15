package me.hwanse.springreststudy.events;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class EventControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  public void createEvent() throws Exception {
    mockMvc.perform(post("/api/events/")
                  .contentType(MediaType.APPLICATION_JSON_VALUE)
                  .accept(MediaTypes.HAL_JSON))
          .andExpect(status().isCreated());
  }

}
