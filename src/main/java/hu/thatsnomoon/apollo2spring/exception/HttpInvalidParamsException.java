package hu.thatsnomoon.apollo2spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author David
 */
@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid params on request.")
public class HttpInvalidParamsException extends RuntimeException{

}
