package me.hwanse.springreststudy.common;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import me.hwanse.springreststudy.index.IndexController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;

public class ErrorsResource extends EntityModel<Errors> {

  public ErrorsResource(Errors content, Link... links) {
    super(content, links);
    add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
  }

}
