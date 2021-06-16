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

  @Test
  @DisplayName("event 도메인 free 여부 테스트")
  public void test_free() throws Exception {
    // given
    Event event = Event.builder()
                       .basePrice(0)
                       .maxPrice(0)
                       .build();
    // when
    event.update();

    // then
    assertThat(event.isFree()).isTrue();

    event = Event.builder()
                 .basePrice(100)
                 .maxPrice(0)
                 .build();

    event.update();

    assertThat(event.isFree()).isFalse();

    event = Event.builder()
                 .basePrice(0)
                 .maxPrice(100)
                 .build();

    event.update();

    assertThat(event.isFree()).isFalse();
  }

  @Test
  @DisplayName("offline 여부 테스트")
  public void test_offline() throws Exception {
    // given
    Event event = Event.builder()
                       .location("강남")
                       .build();
    // when
    event.update();

    // then
    assertThat(event.isOffline()).isTrue();

    // given
    event = Event.builder()
                 .build();
    // when
    event.update();

    // then
    assertThat(event.isOffline()).isFalse();
  }

}