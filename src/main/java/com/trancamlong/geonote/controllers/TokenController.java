package com.trancamlong.geonote.controllers;

import static com.trancamlong.geonote.enums.ErrorEnum.INTERNAL_SERVER_ERROR;

import com.trancamlong.geonote.api.TokenApi;
import com.trancamlong.geonote.configs.components.BaseException;
import com.trancamlong.geonote.model.JWTRequest;
import com.trancamlong.geonote.model.JWTResponse;
import com.trancamlong.geonote.model.OAuthRequest;
import com.trancamlong.geonote.model.OAuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TokenController implements TokenApi {
  @Override
  public ResponseEntity<JWTResponse> generateJwt(final JWTRequest jwTRequest) {
    log.info("Generate jwt called");
    throw new BaseException(INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    // return null;
  }

  @Override
  public ResponseEntity<OAuthResponse> generateOauthToken(final OAuthRequest oauthRequest) {
    return TokenApi.super.generateOauthToken(oauthRequest);
  }
}
