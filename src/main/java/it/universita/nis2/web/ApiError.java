package it.universita.nis2.web;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.*;
@RestControllerAdvice public class ApiError {
@ExceptionHandler(ResponseStatusException.class) public ResponseEntity<Map<String,String>> handle(ResponseStatusException e){return ResponseEntity.status(e.getStatusCode()).body(Map.of("error",Objects.toString(e.getReason(),"Errore")));}
@ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<Map<String,String>> validation(MethodArgumentNotValidException e){return ResponseEntity.badRequest().body(Map.of("error","Verificare i campi obbligatori"));}
}
