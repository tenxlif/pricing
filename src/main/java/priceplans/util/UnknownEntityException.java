package priceplans.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such Entity exists at this Location")
public class UnknownEntityException extends RuntimeException{
    public UnknownEntityException(String message)
    {
        super(message);
    }
}
