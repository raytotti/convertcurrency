package com.raytotti.convertcurrency.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User.notFound")
public class UserNotFoundException extends RuntimeException {

}
