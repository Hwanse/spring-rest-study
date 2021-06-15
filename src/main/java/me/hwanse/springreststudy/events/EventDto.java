package me.hwanse.springreststudy.events;

import java.time.LocalDateTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventDto {

  @NotEmpty
  private String name;
  @NotEmpty
  private String description;
  @NotNull
  private LocalDateTime beginEnrollmentDateTime;
  @NotNull
  private LocalDateTime closeEnrollmentDateTime;
  @NotNull
  private LocalDateTime beginEventDateTime;
  @NotNull
  private LocalDateTime endEventDateTime;

  private String location;  // Optional - 이게 없으면 온라인 모임
  @Min(0)
  private int basePrice;  // Optional - 기본 등록비
  @Min(0)
  private int maxPrice;   // Optional - 최대 등록비
  @Min(0)
  private int limitOfEnrollment;

}
