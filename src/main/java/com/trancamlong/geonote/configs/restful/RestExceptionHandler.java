package com.trancamlong.geonote.configs.restful;

import static com.trancamlong.geonote.enums.ErrorEnum.INTERNAL_SERVER_ERROR;

import com.trancamlong.geonote.configs.components.BaseException;
import com.trancamlong.geonote.configs.components.Response;
import io.opentelemetry.api.trace.Span;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
  @ResponseBody
  @ExceptionHandler(BaseException.class)
  public ResponseEntity<Response<Object>> handleException(
      final HttpServletRequest request, final BaseException baseException) {
    log.error(baseException.getMessage());
    var response =
        Response.fail(
            MessageFormat.format(baseException.getMessage(), baseException.getArguments()),
            baseException.getErrorCode());
    response.setTraceId("");
    response.setPath(request.getRequestURI());
    return ResponseEntity.status(baseException.getHttpStatus()).body(response);
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(value = Throwable.class)
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Response.class))
            })
      })
  public ResponseEntity<Response<Object>> handleException(
      final HttpServletRequest request, final Throwable exception) {
    log.error(exception.getMessage());
    Response<Object> response =
        Response.fail(exception.getMessage(), INTERNAL_SERVER_ERROR.getErrorCode());
    response.setTraceId(Span.current().getSpanContext().getTraceId());
    response.setPath(request.getRequestURI());
    return ResponseEntity.internalServerError().body(response);
  }
}
