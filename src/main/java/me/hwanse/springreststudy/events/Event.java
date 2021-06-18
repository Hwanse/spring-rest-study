package me.hwanse.springreststudy.events;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "id")
public class Event {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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

  @Enumerated(EnumType.STRING)
  private EventStatus eventStatus = EventStatus.DRAFT;

  public void update() {
    // update free
    if (basePrice == 0 && maxPrice == 0) {
      free = true;
    } else {
      free = false;
    }

    // update offline
    if (location == null || location.isBlank()) {
      offline = false;
    } else {
      offline = true;
    }
  }

  public void update(EventDto updateDto) {
    this.name = updateDto.getName();
    this.description = updateDto.getDescription();
    this.beginEnrollmentDateTime = updateDto.getBeginEnrollmentDateTime();
    this.closeEnrollmentDateTime = updateDto.getCloseEnrollmentDateTime();
    this.beginEventDateTime = updateDto.getBeginEventDateTime();
    this.endEventDateTime = updateDto.getEndEventDateTime();
    this.location = updateDto.getLocation();
    this.basePrice = updateDto.getBasePrice();
    this.maxPrice = updateDto.getMaxPrice();
    this.limitOfEnrollment = updateDto.getLimitOfEnrollment();
    this.update();
  }

}
