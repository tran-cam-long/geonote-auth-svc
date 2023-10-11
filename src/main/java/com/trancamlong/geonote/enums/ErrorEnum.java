package com.trancamlong.geonote.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorEnum {
  // RULE: ALL ERRORS FROM THIS SERVICE MUST START WITH '12'xxx
  INTERNAL_SERVER_ERROR(12500, "Internal server error");

  private final long errorCode;
  private final String message;
}
