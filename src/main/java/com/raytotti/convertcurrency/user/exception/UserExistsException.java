package com.raytotti.convertcurrency.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "User.exists")
public class UserExistsException extends RuntimeException {

}
