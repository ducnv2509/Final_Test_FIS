package ducnv.com.ex;

import ducnv.com.constant.Constant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static ducnv.com.constant.Constant.INTERNAL_SERVER_ERROR;
import static ducnv.com.constant.Constant.QUANTITY_ERROR;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<ErrorMessage> handleSystemError(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.builder().code(INTERNAL_SERVER_ERROR).message(exception.getMessage()).build());
    }

    @ExceptionHandler(value = {CustomerNotFoundException.class})
    protected ResponseEntity<ErrorMessage> handlerCustomerNotFoundException(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.builder().code(Constant.CUSTOMER_NOT_FOUND).message(exception.getMessage()).build());
    }

    @ExceptionHandler(value = {OrderNotFoundException.class})
    protected ResponseEntity<ErrorMessage> handlerOrderNotFoundException(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.builder().code(Constant.ORDER_NOT_FOUND).message(exception.getMessage()).build());
    }

    @ExceptionHandler(value = {
            QuantityProductException.class
    })
    protected ResponseEntity<ErrorMessage> handlerQuantityFoundException(Exception exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorMessage.builder().code(QUANTITY_ERROR).message(exception.getMessage()).build());
    }


//    @ExceptionHandler(value = {
//            QuantityProductException.class
//    })
//    protected ResponseEntity<ErrorMessage> handlerOrderStatusFoundException(Exception exception){
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(ErrorMessage.builder().code(QUANTITY_ERROR).message(exception.getMessage()).build());
//    }


}
