package me.hwanse.springreststudy.events;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

/**
 * EventResource 클래스와 동일한 결과를 반환하지만 다음과 같은 스타일로
 * 코딩할 수 있다는 예시
 */
public class EventResourceV2 extends EntityModel<Event> {

  public EventResourceV2(Event content, Link... links) {
    super(content, links);
    add(linkTo(EventController.class).slash(content.getId()).withSelfRel());
  }

}
