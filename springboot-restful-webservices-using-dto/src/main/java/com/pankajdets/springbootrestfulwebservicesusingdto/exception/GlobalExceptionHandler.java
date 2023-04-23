package com.pankajdets.springbootrestfulwebservicesusingdto.exception;

import java.io.ObjectInputStream.GetField;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice //To handle the Exception Globally 
//Means we use his annotation to handle all the specific exception and as well as global exception in single place
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    //Custom Exception handler
    @ExceptionHandler(ResourceNotFoundException.class) // To handle Specific Exception and return custom Error Response back to client
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                       WebRequest webRequest){

       ErrorDetails errorDetails = new ErrorDetails(
               LocalDateTime.now(),
               exception.getMessage(),
               webRequest.getDescription(false),
               "USER_NOT_FOUND"
       );

       return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
   }

   //Custom Exception Handler
   @ExceptionHandler(EmailAlreadyExistsException.class) // To handle Specific Exception and return custom Error Response back to client
   public ResponseEntity<ErrorDetails> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception,
                                                                      WebRequest webRequest){

      ErrorDetails errorDetails = new ErrorDetails(
              LocalDateTime.now(),
              exception.getMessage(),
              webRequest.getDescription(false),
              "USER_EMAIL_ALREADY_EXISTS"
      );

      return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
  }
    

  //Global Exception Handler
  @ExceptionHandler(Exception.class) // To handle Specific Exception and return custom Error Response back to client
  public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception,
                                                                     WebRequest webRequest){

     ErrorDetails errorDetails = new ErrorDetails(
             LocalDateTime.now(),
             exception.getMessage(),
             webRequest.getDescription(false),
             "INTERNAL SERVER ERROR"
     );

     return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
 }

@Override
protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
        HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    
    Map<String, String> errors = new HashMap<>();
    List<ObjectError> errorList = ex.getBindingResult().getAllErrors();

    errorList.forEach((error) -> {
        String fieldName = ((FieldError) error).getField();
        String message = error.getDefaultMessage();
        errors.put(fieldName, message );

    });
    
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);


}
}