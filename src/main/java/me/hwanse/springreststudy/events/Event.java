package me.hwanse.springreststudy.events;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "id")
public class Event {

  private Long id;
  private String name;
  private String description;
  private LocalDateTime beginEnrollmentDateTime;
  private LocalDateTime closeEnrollmentDateTime;
  private LocalDateTime beginEventDateTime;
  private LocalDateTime endEventDateTime;
  private String location;  // Optional - 이게 없으면 온라인 모임
  private int basePrice;  // Optional - 기본 등록비
  private int maxPrice;   // Optional - 최대 등록비
  private int limitOfEnrollment;
  private boolean offline;
  private boolean free;
  private EventStatus eventStatus;

}
