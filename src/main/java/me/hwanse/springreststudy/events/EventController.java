package me.hwanse.springreststudy.events;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

  // HATEOAS 프로젝트에서 구 버전은 ControllerLinkBuilder 클래스 안에 포함되어 있었지만
  // 현재 버전에서는 WebMvcLinkBuilder 로 사용한다.
  @PostMapping
  public ResponseEntity createEvent(@RequestBody Event event) {
    // linkTo : 컨트롤러나 핸들러 메서드로 부터 URI 정보를 읽어올 때 사용하는 메서드
    // methodOn : 인자로 들어온 Controller class를 감싸 해당 컨트롤러의 핸들러 메서드 정보를 가져오기 위한 Wrapper
    URI createdUri = linkTo(EventController.class).slash("{id}").toUri();
    event.setId(10L);
    return ResponseEntity.created(createdUri).body(event);
  }

}
