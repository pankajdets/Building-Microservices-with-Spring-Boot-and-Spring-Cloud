package com.pankajdets.springbootrestfulwebservicesusingdto.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice //To handle the Exception Globally 
//Means we use his annotation to handle all the specific exception and as well as global exception in single place
public class GlobalExceptionHandler {

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
}
