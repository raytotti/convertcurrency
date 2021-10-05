package com.raytotti.convertcurrency.transaction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Transaction.notFound")
public class TransactionNotFound extends RuntimeException {
}
