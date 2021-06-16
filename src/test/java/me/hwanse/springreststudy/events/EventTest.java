package me.hwanse.springreststudy.events;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

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

  @ParameterizedTest
  @DisplayName("event 도메인 free 여부 테스트")
  @MethodSource // default로 테스트할 메서드와 동일한 이름의 팩토리 메서드를 찾아서 params로 넣어준다
  public void test_free(int basePrice, int maxPrice, boolean isFree) throws Exception {
    // given
    Event event = Event.builder()
                       .basePrice(basePrice)
                       .maxPrice(maxPrice)
                       .build();
    // when
    event.update();

    // then
    assertThat(event.isFree()).isEqualTo(isFree);
  }

  // params 테스트를 할 경우 아래와 같은 팩토리 메서드를 정의가 가능하며 반드시 static 메서드이어야 동작한다.
  private static Object[] test_free() {
    return new Object[] {
      new Object[] {0, 0, true},
      new Object[] {100, 0, false},
      new Object[] {0, 100, false},
      new Object[] {100, 200, false}
    };
  }

  @ParameterizedTest
  @DisplayName("event 도메인 offline 여부 테스트")
  @MethodSource
  public void test_offline(String location, boolean isOffline) throws Exception {
    // given
    Event event = Event.builder()
                       .location(location)
                       .build();
    // when
    event.update();

    // then
    assertThat(event.isOffline()).isEqualTo(isOffline);
  }

  private static Object[] test_offline() {
    return new Object[] {
      new Object[] {"강남", true},
      new Object[] {null, false},
      new Object[] {"     ", false}
    };
  }

}