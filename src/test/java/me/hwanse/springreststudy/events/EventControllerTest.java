package me.hwanse.springreststudy.events;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.IntStream;
import me.hwanse.springreststudy.account.Account;
import me.hwanse.springreststudy.account.AccountAdapter;
import me.hwanse.springreststudy.account.AccountRepository;
import me.hwanse.springreststudy.account.AccountRole;
import me.hwanse.springreststudy.account.AccountService;
import me.hwanse.springreststudy.common.AppProperties;
import me.hwanse.springreststudy.common.BaseControllerTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

public class EventControllerTest extends BaseControllerTest {

  @Autowired
  EventRepository eventRepository;

  @Autowired
  AccountService accountService;

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  AppProperties appProperties;

  @Test
  @DisplayName("Event ?????? - 201")
  public void createEvent() throws Exception {
    // given
    EventDto event = getEventDto();

    // then
    mockMvc.perform(post("/api/events")
                      .header(HttpHeaders.AUTHORIZATION, getUserBearerToken())
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
           .andExpect(jsonPath("manager").value(Matchers.notNullValue()))
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
                           responseFields(
                             // ?????? ???????????? ?????? ????????? ?????? ????????? description ????????????. ????????? Test ?????? ?????? ??????
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
                             fieldWithPath("manager.*").description("manager information of new event"),
                             fieldWithPath("_links.*").ignored(), // ????????? ????????? ??????????????? ignore ?????? ?????? ????????????
                             fieldWithPath("_links.self.*").ignored(),
                             fieldWithPath("_links.query-events.*").ignored(),
                             fieldWithPath("_links.update-event.*").ignored(),
                             fieldWithPath("_links.profile.*").ignored()
                             /*fieldWithPath("_links.self.href").description("self resource link"),
                             fieldWithPath("_links.query-events.href").description("query-events resource link to other resources"),
                             fieldWithPath("_links.update-event.href").description("update-event resource link to other resources")*/
                           )
                           //relaxedResponseFields() : ?????? ???????????? ????????? ????????? ????????? ???????????????. (????????? ???????????? ?????????)
           ))
    ;
  }

  @Test
  @DisplayName("Event ?????? - bad request (Dto??? ?????? ?????? ?????????)")
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
      .location("??????")
      .free(true)
      .offline(false)
      .eventStatus(EventStatus.PUBLISHED)
      .build();

    // then
    mockMvc.perform(post("/api/events")
                      .header(HttpHeaders.AUTHORIZATION, getUserBearerToken())
                      .contentType(MediaType.APPLICATION_JSON_VALUE)
                      .accept(MediaTypes.HAL_JSON)
                      .content(objectMapper.writeValueAsString(event)))
           .andDo(print())
           .andExpect(status().isBadRequest())
    ;

  }

  @Test
  @DisplayName("Event ?????? - bad request (???????????? ?????????)")
  public void createEvent_badRequest_emptyInput() throws Exception {
    // given
    EventDto event = EventDto.builder().build();

    // then
    mockMvc.perform(post("/api/events")
                      .header(HttpHeaders.AUTHORIZATION, getUserBearerToken())
                      .contentType(MediaType.APPLICATION_JSON_VALUE)
                      .accept(MediaTypes.HAL_JSON)
                      .content(objectMapper.writeValueAsString(event)))
           .andDo(print())
           .andExpect(status().isBadRequest())
    ;

  }

  @Test
  @DisplayName("Event ?????? - bad request (???????????? ????????? ?????? ?????? ??????)")
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
      .location("??????")
      .build();

    // when

    // then
    mockMvc.perform(post("/api/events")
                      .header(HttpHeaders.AUTHORIZATION, getUserBearerToken())
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
           // ?????????????????? ?????? ????????? ??? ???????????? ??????????????? REST API??? Index API ????????? ?????? ???????????????
           .andExpect(jsonPath("_links.index").exists())
    ;
  }

  @Test
  @DisplayName("30?????? ???????????? 10?????? ????????? ????????? ????????????")
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
                             fieldWithPath("_embedded.eventResourceList[].manager.*").description("manager information of event"),
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

  @Test
  @DisplayName("30?????? ???????????? 10?????? ????????? ????????? ??????(????????? ?????????)")
  public void query_events_with_authentication() throws Exception {
    // given
    IntStream.range(0, 30).forEach(i -> this.generatedEvent(i));

    // when
    mockMvc.perform(get("/api/events")
                      .header(HttpHeaders.AUTHORIZATION, getUserBearerToken())
                      .param("page", "1")
                      .param("size", "10")
                      .param("sort", String.format("%s%c%s", "name", ',', "DESC")))
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("page").exists())
           .andExpect(jsonPath("_embedded.eventResourceList[0]._links.self").exists())
           .andExpect(jsonPath("_links.self").exists())
           .andExpect(jsonPath("_links.profile").exists())
           .andExpect(jsonPath("_links.create-event").exists())
           .andDo(document("query-events",
                           links(
                             linkWithRel("first").description("link to first page"),
                             linkWithRel("prev").description("link to previous page"),
                             linkWithRel("self").description("link to current page"),
                             linkWithRel("next").description("link to next page"),
                             linkWithRel("last").description("link to last page"),
                             linkWithRel("profile").description("link to events list profile"),
                             linkWithRel("create-event").description("link to create event")
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
                             fieldWithPath("_embedded.eventResourceList[].manager.*").description("manager information of event"),
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
                             fieldWithPath("_links.profile.*").ignored(),
                             fieldWithPath("_links.create-event.*").ignored()
                           )
           ))
    ;
  }

  @Test
  @DisplayName("Event ?????? - success")
  public void get_event() throws Exception {
    // given
    Event event = generatedEvent(100);

    // when && then
    /*
     * Rest Docs??? PathParameter??? ???????????? ???????????? mockMvcRequestBuilder ??? ???????????? ?????????
     * RestDocumentationRequestBuilders ????????? ????????????.
     * ??? ????????? ????????? ????????? ????????? ?????? ?????? ??? ????????? ????????????
     */
    mockMvc.perform(RestDocumentationRequestBuilders.get("/api/events/{id}", event.getId()))
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("id").exists())
           .andExpect(jsonPath("name").exists())
           .andExpect(jsonPath("_links.self").exists())
           .andExpect(jsonPath("_links.profile").exists())
           .andDo(document("get-an-event",
                           pathParameters(
                             parameterWithName("id").description("event id to query")
                           ),
                           links(
                             linkWithRel("self").description("link to self"),
                             linkWithRel("profile").description("link to get event profile")
                           ),
                           responseFields(
                             fieldWithPath("id").description("id of event"),
                             fieldWithPath("name").description("name of event"),
                             fieldWithPath("description").description("description of event"),
                             fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of event"),
                             fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of event"),
                             fieldWithPath("beginEventDateTime").description("beginEventDateTime of event"),
                             fieldWithPath("endEventDateTime").description("endEventDateTime of event"),
                             fieldWithPath("location").description("location of event"),
                             fieldWithPath("basePrice").description("basePrice of event"),
                             fieldWithPath("maxPrice").description("maxPrice of event"),
                             fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of event"),
                             fieldWithPath("offline").description("it tells if this event is offline or not"),
                             fieldWithPath("free").description("it tells is this event is free or not"),
                             fieldWithPath("eventStatus").description("eventStatus of event"),
                             fieldWithPath("manager.*").description("manager information of event"),
                             fieldWithPath("_links.self.*").ignored(),
                             fieldWithPath("_links.profile.*").ignored()
                           )
           ))
    ;
  }

  @Test
  @DisplayName("Event ?????? - success (????????? ?????????)")
  public void get_event_with_authentication() throws Exception {
    // given
    Event event = generatedEvent(100);

    // when && then
    /*
     * Rest Docs??? PathParameter??? ???????????? ???????????? mockMvcRequestBuilder ??? ???????????? ?????????
     * RestDocumentationRequestBuilders ????????? ????????????.
     * ??? ????????? ????????? ????????? ????????? ?????? ?????? ??? ????????? ????????????
     */
    mockMvc.perform(RestDocumentationRequestBuilders.get("/api/events/{id}", event.getId())
                                                    .header(HttpHeaders.AUTHORIZATION, getUserBearerToken()))
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("id").exists())
           .andExpect(jsonPath("name").exists())
           .andExpect(jsonPath("_links.self").exists())
           .andExpect(jsonPath("_links.profile").exists())
           .andDo(document("get-an-event",
                           pathParameters(
                             parameterWithName("id").description("event id to query")
                           ),
                           links(
                             linkWithRel("self").description("link to self"),
                             linkWithRel("profile").description("link to get event profile"),
                             linkWithRel("update-event").description("link to update event")
                           ),
                           responseFields(
                             fieldWithPath("id").description("id of event"),
                             fieldWithPath("name").description("name of event"),
                             fieldWithPath("description").description("description of event"),
                             fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of event"),
                             fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of event"),
                             fieldWithPath("beginEventDateTime").description("beginEventDateTime of event"),
                             fieldWithPath("endEventDateTime").description("endEventDateTime of event"),
                             fieldWithPath("location").description("location of event"),
                             fieldWithPath("basePrice").description("basePrice of event"),
                             fieldWithPath("maxPrice").description("maxPrice of event"),
                             fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of event"),
                             fieldWithPath("offline").description("it tells if this event is offline or not"),
                             fieldWithPath("free").description("it tells is this event is free or not"),
                             fieldWithPath("eventStatus").description("eventStatus of event"),
                             fieldWithPath("manager.*").description("manager information of event"),
                             fieldWithPath("_links.self.*").ignored(),
                             fieldWithPath("_links.profile.*").ignored(),
                             fieldWithPath("_links.update-event.*").ignored()
                           )
           ))
    ;
  }

  @Test
  @DisplayName("Event ?????? - not found")
  public void get_event_404() throws Exception {
    mockMvc.perform(get("/api/events/9999"))
           .andDo(print())
           .andExpect(status().isNotFound())
    ;
  }

  @Test
  @DisplayName("Event ?????? - success")
  public void event_update() throws Exception {
    // given
    Event event = generatedEvent(1);
    EventDto eventDto = EventDto
      .builder()
      .name("Spring")
      .description("REST API Development with Spring")
      .beginEnrollmentDateTime(LocalDateTime.of(2021, 6, 18, 22, 50))
      .closeEnrollmentDateTime(LocalDateTime.of(2021, 6, 19, 22, 50))
      .beginEventDateTime(LocalDateTime.of(2021, 6, 20, 12, 0))
      .endEventDateTime(LocalDateTime.of(2021, 6, 21, 12, 0))
      .basePrice(0)
      .maxPrice(0)
      .limitOfEnrollment(100)
      .location(null)
      .build();

    // when
    mockMvc.perform(RestDocumentationRequestBuilders.put("/api/events/{id}", event.getId())
                                                    .header(HttpHeaders.AUTHORIZATION, getUserBearerToken())
                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                    .accept(MediaTypes.HAL_JSON_VALUE)
                                                    .content(objectMapper.writeValueAsString(eventDto)))
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("id").exists())
           .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
           .andExpect(jsonPath("id").value(Matchers.not(100L)))
           .andExpect(jsonPath("free").value(true))
           .andExpect(jsonPath("offline").value(false))
           .andExpect(jsonPath("_links.self").exists())
           .andExpect(jsonPath("_links.profile").exists())
           .andDo(document("update-event",
                           links(
                             linkWithRel("self").description("link to self"),
                             linkWithRel("profile").description("link to update event profile")
                           ),
                           requestHeaders(
                             headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header"),
                             headerWithName(HttpHeaders.ACCEPT).description("accept header")
                           ),
                           pathParameters(
                             parameterWithName("id").description("event id to query")
                           ),
                           requestFields(
                             fieldWithPath("name").description("name of event to update"),
                             fieldWithPath("description").description("description of event to update"),
                             fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of event to update"),
                             fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of event to update"),
                             fieldWithPath("beginEventDateTime").description("beginEventDateTime of event to update"),
                             fieldWithPath("endEventDateTime").description("endEventDateTime of event to update"),
                             fieldWithPath("location").description("location of event to update"),
                             fieldWithPath("basePrice").description("basePrice of event to update"),
                             fieldWithPath("maxPrice").description("maxPrice of event to update"),
                             fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of event to update")
                           ),
                           responseHeaders(
                             headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                           ),
                           responseFields(
                             fieldWithPath("id").description("id of updated event"),
                             fieldWithPath("name").description("name of updated event"),
                             fieldWithPath("description").description("description of updated event"),
                             fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of updated event"),
                             fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of updated event"),
                             fieldWithPath("beginEventDateTime").description("beginEventDateTime of updated event"),
                             fieldWithPath("endEventDateTime").description("endEventDateTime of updated event"),
                             fieldWithPath("location").description("location of updated event"),
                             fieldWithPath("basePrice").description("basePrice of updated event"),
                             fieldWithPath("maxPrice").description("maxPrice of updated event"),
                             fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of updated event"),
                             fieldWithPath("offline").description("it tells if this event is offline or not"),
                             fieldWithPath("free").description("it tells is this event is free or not"),
                             fieldWithPath("eventStatus").description("eventStatus of updated event"),
                             fieldWithPath("manager.*").description("manager information of event"),
                             fieldWithPath("_links.self.*").ignored(),
                             fieldWithPath("_links.profile.*").ignored()
                           )
                  ))
    ;
  }

  @Test
  @DisplayName("Event ?????? - ?????? ????????? ?????? ???")
  public void event_update_unAuthenticated() throws Exception {
    // given
    Event event = generatedEvent(1);
    EventDto eventDto = EventDto
      .builder()
      .name("Spring")
      .description("REST API Development with Spring")
      .beginEnrollmentDateTime(LocalDateTime.of(2021, 6, 18, 22, 50))
      .closeEnrollmentDateTime(LocalDateTime.of(2021, 6, 19, 22, 50))
      .beginEventDateTime(LocalDateTime.of(2021, 6, 20, 12, 0))
      .endEventDateTime(LocalDateTime.of(2021, 6, 21, 12, 0))
      .basePrice(0)
      .maxPrice(0)
      .limitOfEnrollment(100)
      .location(null)
      .build();

    // when
    mockMvc.perform(RestDocumentationRequestBuilders.put("/api/events/{id}", event.getId())
                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                    .accept(MediaTypes.HAL_JSON_VALUE)
                                                    .content(objectMapper.writeValueAsString(eventDto)))
           .andDo(print())
           .andExpect(status().isUnauthorized())
    ;
  }

  @Test
  @DisplayName("Event ?????? - event manager??? ?????? ???")
  public void event_update_not_match_authentication() throws Exception {
    // given
    Event event = generatedEvent(1);
    EventDto eventDto = EventDto
      .builder()
      .name("Spring")
      .description("REST API Development with Spring")
      .beginEnrollmentDateTime(LocalDateTime.of(2021, 6, 18, 22, 50))
      .closeEnrollmentDateTime(LocalDateTime.of(2021, 6, 19, 22, 50))
      .beginEventDateTime(LocalDateTime.of(2021, 6, 20, 12, 0))
      .endEventDateTime(LocalDateTime.of(2021, 6, 21, 12, 0))
      .basePrice(0)
      .maxPrice(0)
      .limitOfEnrollment(100)
      .location(null)
      .build();

    // when
    mockMvc.perform(RestDocumentationRequestBuilders.put("/api/events/{id}", event.getId())
                                                    .header(HttpHeaders.AUTHORIZATION, getAdminBearerToken())
                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                    .accept(MediaTypes.HAL_JSON_VALUE)
                                                    .content(objectMapper.writeValueAsString(eventDto)))
           .andDo(print())
           .andExpect(status().isUnauthorized())
    ;
  }

  @Test
  @DisplayName("Event ?????? - bad request(empty input)")
  public void event_update_emptyInput() throws Exception {
    // given
    Event event = generatedEvent(1);
    EventDto eventDto = EventDto
      .builder()
      .build();

    // when
    mockMvc.perform(put("/api/events/{id}", event.getId())
                    .header(HttpHeaders.AUTHORIZATION, getUserBearerToken())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaTypes.HAL_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(eventDto)))
           .andDo(print())
           .andExpect(status().isBadRequest())
    ;
  }

  @Test
  @DisplayName("Event ?????? - not found")
  public void event_update_notFound() throws Exception {
    // given
    Event event = generatedEvent(1);
    EventDto eventDto = EventDto
      .builder()
      .build();

    // when
    mockMvc.perform(put("/api/events/9999")
                    .header(HttpHeaders.AUTHORIZATION, getUserBearerToken())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaTypes.HAL_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(eventDto)))
           .andDo(print())
           .andExpect(status().isNotFound())
    ;
  }

  @Test
  @DisplayName("Event ?????? - bad request (???????????? ????????? ?????? ?????? ??????)")
  public void event_update_wrongValue() throws Exception {
    // given
    Event event = generatedEvent(1);
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
      .location("??????")
      .build();

    // when & then
    mockMvc.perform(put("/api/events/{id}", event.getId())
                      .header(HttpHeaders.AUTHORIZATION, getUserBearerToken())
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
           // ?????????????????? ?????? ????????? ??? ???????????? ??????????????? REST API??? Index API ????????? ?????? ???????????????
           .andExpect(jsonPath("_links.index").exists())
    ;
  }

  private String getAdminBearerToken() throws Exception {
    return "Bearer" + getAccessToken(appProperties.getAdminUsername(), appProperties.getAdminPassword());
  }

  private String getUserBearerToken() throws Exception {
    return "Bearer" + getAccessToken(appProperties.getUserUsername(), appProperties.getUserPassword());
  }

  private String getAccessToken(String username, String password) throws Exception {
    // when & then
    ResultActions perform = mockMvc
      .perform(post("/oauth/token")
              .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
              .param("username", username)
              .param("password", password)
              .param("grant_type", "password"))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("access_token").exists());

    String responseBody = perform.andReturn().getResponse().getContentAsString();
    Jackson2JsonParser parser = new Jackson2JsonParser();
    return parser.parseMap(responseBody).get("access_token").toString();
  }

  private EventDto getEventDto() {
    return EventDto
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
      .location("??????")
      .build();
  }

  private Event generatedEvent(int i) {
    Account account = accountRepository.findByEmail(appProperties.getUserUsername()).orElseThrow();

    Event event = Event.builder()
                       .name("event " + i)
                       .description("test event")
                       .eventStatus(EventStatus.DRAFT)
                       .manager(account)
                       .build();

    return eventRepository.save(event);
  }

}
