package me.hwanse.springreststudy.events;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
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

  // HATEOAS 프로젝트에서 구 버전은 ControllerLinkBuilder 클래스 안에 포함되어 있었지만
  // 현재 버전에서는 WebMvcLinkBuilder 로 사용한다.
  @PostMapping
  public ResponseEntity createEvent(@Valid @RequestBody EventDto eventDto, Errors errors) {
    if (errors.hasErrors()) {
      return ResponseEntity.badRequest().build();
    }

    eventValidator.validate(eventDto, errors);

    if (errors.hasErrors()) {
      return ResponseEntity.badRequest().build();
    }

    Event event = modelMapper.map(eventDto, Event.class);
    Event newEvent = eventRepository.save(event);

    // linkTo : 컨트롤러나 핸들러 메서드로 부터 URI 정보를 읽어올 때 사용하는 메서드
    // methodOn : 인자로 들어온 Controller class를 감싸 해당 컨트롤러의 핸들러 메서드 정보를 가져오기 위한 Wrapper
    URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
    return ResponseEntity.created(createdUri).body(newEvent);
  }

}
