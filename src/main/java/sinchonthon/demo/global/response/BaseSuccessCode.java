package sinchonthon.demo.global.response;

import org.springframework.http.HttpStatus;

public interface BaseSuccessCode {
    HttpStatus getStatus();

    String getCode();

    String getMessage();
}
