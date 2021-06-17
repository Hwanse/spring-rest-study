package me.hwanse.springreststudy.index;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import me.hwanse.springreststudy.events.EventController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

  @GetMapping("/api/")
  public RepresentationModel index() {
    var index = new RepresentationModel<>();
    index.add(linkTo(EventController.class).withRel("events"));
    return index;
  }

}
