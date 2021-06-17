package me.hwanse.springreststudy.events;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;
import javax.validation.Valid;
import me.hwanse.springreststudy.common.ErrorsResource;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

  private final EventRepository eventRepository;
  private final ModelMapper modelMapper;
  private final EventValidator eventValidator;

  public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
    this.eventRepository = eventRepository;
    this.modelMapper = modelMapper;
    this.eventValidator = eventValidator;
  }

  // Spring HATEOAS 프로젝트에서 구 버전은 ControllerLinkBuilder 클래스 안에 포함되어 있었지만
  // 현재 버전에서는 WebMvcLinkBuilder 로 사용한다.
  @PostMapping
  public ResponseEntity createEvent(@Valid @RequestBody EventDto eventDto, Errors errors) {
    if (errors.hasErrors()) {
      return badRequest(errors);
    }

    eventValidator.validate(eventDto, errors);

    if (errors.hasErrors()) {
      return badRequest(errors);
    }

    Event event = modelMapper.map(eventDto, Event.class);
    event.update();
    Event newEvent = eventRepository.save(event);

    // linkTo : 컨트롤러나 핸들러 메서드로 부터 URI 정보를 읽어올 때 사용하는 메서드
    // methodOn : 인자로 들어온 Controller class를 감싸 해당 컨트롤러의 핸들러 메서드 정보를 가져오기 위한 Wrapper
    WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
    URI createdUri = selfLinkBuilder.toUri();
    EventResource eventResource = new EventResource(newEvent);
//    EventResourceV2 eventResource = new EventResourceV2(newEvent);
    eventResource.add(linkTo(EventController.class).withRel("query-events"));
    // 현재 Link와 동일하지만 관계(행위)가 다를 때에 이런식으로 코딩이 가능하다
    // 같은 URI를 가지지만 update-event 업데이트 요청은 PUT Method로 구분되어 있기 때문
    eventResource.add(selfLinkBuilder.withRel("update-event"));
    eventResource.add(Link.of("/docs/index.html#resources-events-create").withRel("profile"));
    return ResponseEntity.created(createdUri).body(eventResource);
  }

  private ResponseEntity badRequest(Errors errors) {
    return ResponseEntity.badRequest().body(new ErrorsResource(errors));
  }

}
