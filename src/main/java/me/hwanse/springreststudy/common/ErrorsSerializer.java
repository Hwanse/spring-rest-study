package me.hwanse.springreststudy.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

/**
 * Spring의 Errors는 ObjectMapper가 Serialize를 할 수 없다.(왜? javaBean 스펙을 따르지 않아서 ObjectMapper의 \
 * 기능을 지원 받을 수 없다)
 * 따라서, Errors 객체를 Json 으로 Serialize 처리할 커스텀한 Serializer가 필요하여 생성
 */
@JsonComponent  // 해당 클래스를 ObjectMapper에 등록하고 Serialize 과정 중에 등록된 Bean을 Serializer로 사용한다.
public class ErrorsSerializer extends JsonSerializer<Errors> {

  @Override
  public void serialize(Errors errors, JsonGenerator generator, SerializerProvider serializerProvider)
    throws IOException {
    // 스프링 부트 2.3버전으로 업데이트되면서 같이 버전업된 jackson 라이브러리가 array를 바로 만드는 것을
    // 허용하지 않아서 오류가 발생하게된다. 따라서 array의 필드 네이밍을 해줘야 한다
    generator.writeFieldName("errors");
    generator.writeStartArray();
    errors.getFieldErrors().stream().forEach(e -> {
      try {
        generator.writeStartObject();
        generator.writeStringField("field", e.getField());
        generator.writeStringField("objectName", e.getObjectName());
        generator.writeStringField("code", e.getCode());
        generator.writeStringField("defaultMessage", e.getDefaultMessage());
        Object rejectedValue = e.getRejectedValue();
        if (rejectedValue != null) {
          generator.writeStringField("rejectedValue", rejectedValue.toString());
        }
        generator.writeEndObject();
      } catch (IOException ioException) {
        ioException.printStackTrace();
      }
    });

    errors.getGlobalErrors().forEach(e -> {
      try {
        generator.writeStartObject();
        generator.writeStringField("objectName", e.getObjectName());
        generator.writeStringField("code", e.getCode());
        generator.writeStringField("defaultMessage", e.getDefaultMessage());
        generator.writeEndObject();
      } catch (IOException ioException) {
        ioException.printStackTrace();
      }
    });
    generator.writeEndArray();
  }

}
