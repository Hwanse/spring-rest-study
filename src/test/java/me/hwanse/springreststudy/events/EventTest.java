package me.hwanse.springreststudy.events;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EventTest {

  @Test
  public void builder() throws Exception {
    Event event = Event.builder()
      .name("Inflearn Spring Rest API")
      .description("REST API development with Spring")
      .build();
    assertThat(event).isNotNull();
  }

  @Test
  public void javaBean() throws Exception {
    // given
    String name = "Event";
    String description = "Spring";

    // when
    Event event = new Event();
    event.setName(name);
    event.setDescription(description);

    // then
    assertThat(event.getName()).isEqualTo(name);
    assertThat(event.getDescription()).isEqualTo(description);
  }

}