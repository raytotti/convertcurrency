package com.raytotti.convertcurrency.conversion.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Conversion.taxRate.notFound")
public class TaxRateNotFound extends RuntimeException {
    
}
