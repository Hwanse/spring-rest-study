package me.hwanse.springreststudy.events;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;
import java.util.Optional;
import javax.validation.Valid;
import me.hwanse.springreststudy.account.Account;
import me.hwanse.springreststudy.account.AccountAdapter;
import me.hwanse.springreststudy.account.CurrentAccount;
import me.hwanse.springreststudy.common.ErrorsResource;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
  public ResponseEntity createEvent(@Valid @RequestBody EventDto eventDto, Errors errors,
    @CurrentAccount Account currentUser) {
    if (errors.hasErrors()) {
      return badRequest(errors);
    }

    eventValidator.validate(eventDto, errors);

    if (errors.hasErrors()) {
      return badRequest(errors);
    }

    Event event = modelMapper.map(eventDto, Event.class);
    event.update();
    event.setManager(currentUser);
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

  @GetMapping
  public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler,
    @CurrentAccount Account currentUser) {
    Page<Event> page = eventRepository.findAll(pageable);
    var pagedModel = assembler.toModel(page, e -> new EventResource(e));
    pagedModel.add(Link.of("/docs/index.html#resources-events-list").withRel("profile"));
    if (currentUser != null) {
      pagedModel.add(linkTo(EventController.class).withRel("create-event"));
    }
    return ResponseEntity.ok(pagedModel);
  }

  @GetMapping("/{id}")
  public ResponseEntity getEvent(@PathVariable Long id, @CurrentAccount Account currentUser) {
    Optional<Event> optionalEvent = eventRepository.findById(id);
    if (optionalEvent.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    Event event = optionalEvent.get();
    EventResource eventResource = new EventResource(event);
    eventResource.add(Link.of("/docs/index.html#resources-events-get").withRel("profile"));

    if (event.getManager().equals(currentUser)) {
      eventResource.add(linkTo(EventController.class).slash(event.getId()).withRel("update-event"));
    }

    return ResponseEntity.ok(eventResource);
  }

  @PutMapping("{id}")
  public ResponseEntity updateEvent(@PathVariable Long id, @Valid @RequestBody EventDto eventDto,
    Errors errors, @CurrentAccount Account currentUser) {
    Optional<Event> optionalEvent = eventRepository.findById(id);
    if (optionalEvent.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    if (errors.hasErrors()) {
      return badRequest(errors);
    }

    eventValidator.validate(eventDto, errors);

    if (errors.hasErrors()) {
      return badRequest(errors);
    }

    Event event = optionalEvent.get();
    if (!event.getManager().equals(currentUser)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    event.update(eventDto);
    EventResource eventResource = new EventResource(eventRepository.save(event));
    eventResource.add(Link.of("/docs/index.html#resources-events-update").withRel("profile"));

    return ResponseEntity.ok(eventResource);
  }


  private ResponseEntity badRequest(Errors errors) {
    return ResponseEntity.badRequest().body(new ErrorsResource(errors));
  }

}
