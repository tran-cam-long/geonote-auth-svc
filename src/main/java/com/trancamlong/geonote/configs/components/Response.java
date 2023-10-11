package com.trancamlong.geonote.configs.components;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private T data;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String traceId;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Error error;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String path;

  private final Instant timestamp = Instant.now();

  public static <T> Response<T> fail(String message, Long errorCode) {
    return Response.<T>builder()
        .error(Error.builder().message(message).code(errorCode).build())
        .build();
  }
}
