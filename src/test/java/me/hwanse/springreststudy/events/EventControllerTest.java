package me.hwanse.springreststudy.events;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.stream.IntStream;
import me.hwanse.springreststudy.common.RestDocsConfiguration;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
public class EventControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  EventRepository eventRepository;

  @Test
  @DisplayName("Event 등록 API - 201")
  public void createEvent() throws Exception {
    // given
    EventDto event = EventDto
      .builder()
      .name("Spring")
      .description("REST API Development with Spring")
      .beginEnrollmentDateTime(LocalDateTime.of(2021, 6, 15, 22, 50))
      .closeEnrollmentDateTime(LocalDateTime.of(2021, 6, 16, 22, 50))
      .beginEventDateTime(LocalDateTime.of(2021, 6, 17, 12, 0))
      .endEventDateTime(LocalDateTime.of(2021, 6, 18, 12, 0))
      .basePrice(100)
      .maxPrice(200)
      .limitOfEnrollment(100)
      .location("강남")
      .build();

    // then
    mockMvc.perform(post("/api/events")
                      .contentType(MediaType.APPLICATION_JSON_VALUE)
                      .accept(MediaTypes.HAL_JSON)
                      .content(objectMapper.writeValueAsString(event)))
           .andDo(print())
           .andExpect(status().isCreated())
           .andExpect(jsonPath("id").exists())
           .andExpect(header().exists(HttpHeaders.LOCATION))
           .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
           .andExpect(jsonPath("id").value(Matchers.not(100L)))
           .andExpect(jsonPath("free").value(false))
           .andExpect(jsonPath("offline").value(true))
           .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
           .andExpect(jsonPath("_links.self").exists())
           .andExpect(jsonPath("_links.query-events").exists())
           .andExpect(jsonPath("_links.update-event").exists())
           .andExpect(jsonPath("_links.profile").exists())
           .andDo(document("create-event",
                           links(
                             linkWithRel("self").description("link to self"),
                             linkWithRel("query-events").description("link to query events"),
                             linkWithRel("update-event").description("link to update an existing event"),
                             linkWithRel("profile").description("link to profile")
                           ),
                           requestHeaders(
                             headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                             headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                           ),
                           requestFields(
                             fieldWithPath("name").description("name of new event"),
                             fieldWithPath("description").description("description of new event"),
                             fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new event"),
                             fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of new event"),
                             fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),
                             fieldWithPath("endEventDateTime").description("endEventDateTime of new event"),
                             fieldWithPath("location").description("location of new event"),
                             fieldWithPath("basePrice").description("basePrice of new event"),
                             fieldWithPath("maxPrice").description("maxPrice of new event"),
                             fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event")
                           ),
                           responseHeaders(
                             headerWithName(HttpHeaders.LOCATION).description("location header"),
                             headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                           ),
                           responseFields(  // 해당 메서드는 응답 본문의 모든 필드를 description 해야한다. 안하면 Test 코드 오류 출력
                             fieldWithPath("id").description("id of new event"),
                             fieldWithPath("name").description("name of new event"),
                             fieldWithPath("description").description("description of new event"),
                             fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new event"),
                             fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of new event"),
                             fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),
                             fieldWithPath("endEventDateTime").description("endEventDateTime of new event"),
                             fieldWithPath("location").description("location of new event"),
                             fieldWithPath("basePrice").description("basePrice of new event"),
                             fieldWithPath("maxPrice").description("maxPrice of new event"),
                             fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event"),
                             fieldWithPath("offline").description("it tells if this event is offline or not"),
                             fieldWithPath("free").description("it tells is this event is free or not"),
                             fieldWithPath("eventStatus").description("eventStatus of new event"),
                             fieldWithPath("_links.*").ignored(), // 이렇게 필드를 명시적으로 ignore 하는 것도 가능하다
                             fieldWithPath("_links.self.*").ignored(),
                             fieldWithPath("_links.query-events.*").ignored(),
                             fieldWithPath("_links.update-event.*").ignored(),
                             fieldWithPath("_links.profile.*").ignored()
                             /*fieldWithPath("_links.self.href").description("self resource link"),
                             fieldWithPath("_links.query-events.href").description("query-events resource link to other resources"),
                             fieldWithPath("_links.update-event.href").description("update-event resource link to other resources")*/
                           )
                           //relaxedResponseFields() : 해당 메서드는 인자로 명시된 필드만 문서화한다. (문서의 일부분만 테스트)
                  ))
    ;
  }

  @Test
  @DisplayName("Event 등록 API - bad request (Dto에 맞지 않는 입력값)")
  public void createEvent_badRequest() throws Exception {
    // given
    Event event = Event
      .builder()
      .id(100L)
      .name("Spring")
      .description("REST API Development with Spring")
      .beginEnrollmentDateTime(LocalDateTime.of(2021, 6, 15, 22, 50))
      .closeEnrollmentDateTime(LocalDateTime.of(2021, 6, 16, 22, 50))
      .beginEventDateTime(LocalDateTime.of(2021, 6, 17, 12, 0))
      .endEventDateTime(LocalDateTime.of(2021, 6, 18, 12, 0))
      .basePrice(100)
      .maxPrice(200)
      .limitOfEnrollment(100)
      .location("강남")
      .free(true)
      .offline(false)
      .eventStatus(EventStatus.PUBLISHED)
      .build();

    // then
    mockMvc.perform(post("/api/events")
                      .contentType(MediaType.APPLICATION_JSON_VALUE)
                      .accept(MediaTypes.HAL_JSON)
                      .content(objectMapper.writeValueAsString(event)))
           .andDo(print())
           .andExpect(status().isBadRequest())
    ;

  }

  @Test
  @DisplayName("Event 등록 API - bad request (비어있는 입력값)")
  public void createEvent_badRequest_emptyInput() throws Exception {
    // given
    EventDto event = EventDto.builder().build();

    // then
    mockMvc.perform(post("/api/events")
                      .contentType(MediaType.APPLICATION_JSON_VALUE)
                      .accept(MediaTypes.HAL_JSON)
                      .content(objectMapper.writeValueAsString(event)))
           .andDo(print())
           .andExpect(status().isBadRequest())
    ;

  }

  @Test
  @DisplayName("event create API - bad request (비즈니스 규칙에 맞지 않는 입력)")
  public void createEvent_badRequest_wrongValue() throws Exception {
    // given
    EventDto eventDto = EventDto
      .builder()
      .name("Spring")
      .description("REST API Development with Spring")
      .beginEnrollmentDateTime(LocalDateTime.of(2021, 6, 18, 22, 50))
      .closeEnrollmentDateTime(LocalDateTime.of(2021, 6, 16, 22, 50))
      .beginEventDateTime(LocalDateTime.of(2021, 6, 18, 12, 0))
      .endEventDateTime(LocalDateTime.of(2021, 6, 17, 12, 0))
      .basePrice(10000)
      .maxPrice(200)
      .limitOfEnrollment(100)
      .location("강남")
      .build();

    // when

    // then
    mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(eventDto)))
           .andDo(print())
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("errors[0].objectName").exists())
           .andExpect(jsonPath("errors[0].field").exists())
           .andExpect(jsonPath("errors[0].defaultMessage").exists())
           .andExpect(jsonPath("errors[0].code").exists())
           .andExpect(jsonPath("errors[0].rejectedValue").exists())
           // 홈페이지에서 에러 발생시 첫 화면으로 돌아가듯이 REST API도 Index API 링크를 같이 반환해준다
           .andExpect(jsonPath("_links.index").exists())
    ;
  }

  @Test
  @DisplayName("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
  public void query_events() throws Exception {
    // given
    IntStream.range(0, 30).forEach(i -> this.generatedEvent(i));

    // when
    mockMvc.perform(get("/api/events")
                    .param("page", "1")
                    .param("size", "10")
                    .param("sort", String.format("%s%c%s", "name", ',', "DESC")))
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("page").exists())
           .andExpect(jsonPath("_embedded.eventResourceList[0]._links.self").exists())
           .andExpect(jsonPath("_links.self").exists())
           .andExpect(jsonPath("_links.profile").exists())
           .andDo(document("query-events",
                           links(
                             linkWithRel("first").description("link to first page"),
                             linkWithRel("prev").description("link to previous page"),
                             linkWithRel("self").description("link to current page"),
                             linkWithRel("next").description("link to next page"),
                             linkWithRel("last").description("link to last page"),
                             linkWithRel("profile").description("link to events list profile")
                           ),
                           requestParameters(
                             parameterWithName("page").description("page number of query event list"),
                             parameterWithName("size").description("elements count of query event list"),
                             parameterWithName("sort").description("sort conditions of query event list")
                           ),
                           responseFields(
                             fieldWithPath("_embedded.eventResourceList").description("event resource list"),
                             fieldWithPath("_embedded.eventResourceList[].id").description("id of event"),
                             fieldWithPath("_embedded.eventResourceList[].name").description("name of event"),
                             fieldWithPath("_embedded.eventResourceList[].description").description("description of event"),
                             fieldWithPath("_embedded.eventResourceList[].beginEnrollmentDateTime").description("beginEnrollmentDateTime of event"),
                             fieldWithPath("_embedded.eventResourceList[].closeEnrollmentDateTime").description("closeEnrollmentDateTime of event"),
                             fieldWithPath("_embedded.eventResourceList[].beginEventDateTime").description("beginEventDateTime of event"),
                             fieldWithPath("_embedded.eventResourceList[].endEventDateTime").description("endEventDateTime of event"),
                             fieldWithPath("_embedded.eventResourceList[].location").description("location of event"),
                             fieldWithPath("_embedded.eventResourceList[].basePrice").description("basePrice of event"),
                             fieldWithPath("_embedded.eventResourceList[].maxPrice").description("maxPrice of event"),
                             fieldWithPath("_embedded.eventResourceList[].limitOfEnrollment").description("limitOfEnrollment of event"),
                             fieldWithPath("_embedded.eventResourceList[].offline").description("it tells if this event is offline or not"),
                             fieldWithPath("_embedded.eventResourceList[].free").description("it tells is this event is free or not"),
                             fieldWithPath("_embedded.eventResourceList[].eventStatus").description("eventStatus of event"),
                             fieldWithPath("_embedded.eventResourceList[]._links.self.href").description("link to event resource"),
                             fieldWithPath("page.size").description("element count per page"),
                             fieldWithPath("page.totalElements").description("total element count"),
                             fieldWithPath("page.totalPages").description("total page count"),
                             fieldWithPath("page.number").description("current page number"),
                             fieldWithPath("_links.first.*").ignored(),
                             fieldWithPath("_links.prev.*").ignored(),
                             fieldWithPath("_links.next.*").ignored(),
                             fieldWithPath("_links.self.*").ignored(),
                             fieldWithPath("_links.next.*").ignored(),
                             fieldWithPath("_links.last.*").ignored(),
                             fieldWithPath("_links.profile.*").ignored()
                           )
                  ))
    ;
  }

  private void generatedEvent(int i) {
    Event event = Event.builder()
                            .name("event " + i)
                            .description("test event")
                            .build();

    eventRepository.save(event);
  }

}
