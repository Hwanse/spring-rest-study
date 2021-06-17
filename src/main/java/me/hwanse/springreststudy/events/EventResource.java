package me.hwanse.springreststudy.events;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.RepresentationModel;

public class EventResource extends RepresentationModel<EventResource> {

  /**
   * ObjectMapper로 Json 형식으로 Serialize할 땐 원래는 아래 event라는 항목으로 Object 형태로 감싸져서 반환된다.
   * 그러나 JsonUnwrapper 애노테이션을 사용하면 아래 필드가 event로 감싸지지 않고 event 객체의 필드들을 그대로
   * 노출할 수 있다.
   */
  @JsonUnwrapped
  private Event event;

  public EventResource(Event event) {
    this.event = event;
    // self 정보는 event Resource에서 자주 사용되는 link이므로 다음과 같이 생성자에 공통으로 넣어주는게 용이
    add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
  }

  public Event getEvent() {
    return event;
  }

}
