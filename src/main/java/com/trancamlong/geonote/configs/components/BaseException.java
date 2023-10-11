package com.trancamlong.geonote.configs.components;

import com.trancamlong.geonote.enums.ErrorEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
  private final String message;
  private final Long errorCode;
  private final transient HttpStatus httpStatus;
  private final transient Object[] arguments;

  public BaseException(ErrorEnum errorEnum, HttpStatus httpStatus) {
    this.message = errorEnum.getMessage();
    this.errorCode = errorEnum.getErrorCode();
    this.httpStatus = httpStatus;
    this.arguments = new Object[0];
  }
}
