package main.wheelmaster.exception;

import lombok.Getter;

public enum ExceptionCode {
  MEMBER_NOT_FOUND(404, "Member not found"),
  MEMBER_ALREADY_EXISTS(409, "Member already exists"),
  FIELD_ERROR(400, "Field Error"),
  CONSTRAINT_VIOLATION_ERROR(400, "Constraint Violation Error"),
  COMMENT_NOT_FOUND(404,"Comment not found" ),
  VOTE_ALREADY_EXISTS(409, "Vote already exists"),
  VOTE_NOT_FOUND(404, "Vote not found"),
  FAVORITE_PLACE_ALREADY_EXISTS(409, "Favorite place already exists"),
  FAVORITE_PLACE_NOT_FOUND(404, "Favorite place not found"),
  ROLE_IS_NOT_EXISTS(403, "Role is not exists"),
  PASSWORD_INCORRECT(404, "Password Incorrect"),
  TOKEN_IS_INVALID(401, "Token is invalid"),
  REFRESH_TOKEN_IS_EXPIRED(403, "Refresh Token is expired");



  @Getter
  private int status;

  @Getter
  private String message;

  ExceptionCode(int code, String message) {
    this.status = code;
    this.message = message;
  }
}