package com.socialmeadia.socialmedia.Controller;


import com.socialmeadia.socialmedia.DTO.RequestDTO;
import com.socialmeadia.socialmedia.Exception.AlreadyFriend;
import com.socialmeadia.socialmedia.Exception.EntityNotFound;
import com.socialmeadia.socialmedia.Service.RequestService;
import com.socialmeadia.socialmedia.Util.MessageSourceImpl;
import com.socialmeadia.socialmedia.Util.PaginationResponse;
import com.socialmeadia.socialmedia.Util.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/request")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private MessageSourceImpl messageSource;

    @PostMapping
    public ResponseEntity<?> sendRequest(@Valid @RequestBody RequestDTO request) {
        ResponseHandler<Object> response;
        try {
            requestService.save(request);
            response = new ResponseHandler<>(null, messageSource.getMessage("request.sent.successfully"), HttpStatus.OK, true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (AlreadyFriend e)
        {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.OK, false);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (EntityNotFound e)
        {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.NOT_FOUND, false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e) {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.BAD_REQUEST, false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{sendTo}")
    public ResponseEntity<?> deleteRequest(@PathVariable String sendTo) {
        ResponseHandler<Object> response;
        try {
            requestService.delete(sendTo);
            response = new ResponseHandler<>(null, messageSource.getMessage("request.deleted.successfully"), HttpStatus.OK, true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.BAD_REQUEST, false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllRequests(@RequestParam(defaultValue = "5") int pageSize, @RequestParam(required = false) String lastEvaluatedKey) {
        ResponseHandler<PaginationResponse> response;
        try {
            PaginationResponse page = requestService.getAll(pageSize, lastEvaluatedKey);
            response = new ResponseHandler<>(page, messageSource.getMessage("request.fetched.successfully"), HttpStatus.OK, true);
            return ResponseEntity.status(HttpStatus.OK).body(page);
        } catch (Exception e) {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.BAD_REQUEST, false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/accept/{sendBy}")
    public ResponseEntity<?> acceptRequest(@PathVariable String sendBy) {
        ResponseHandler<?> response;
        try {
            requestService.acceptRequest(sendBy);
            response = new ResponseHandler<>(null, messageSource.getMessage("request.accepted.successfully"), HttpStatus.OK, true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.BAD_REQUEST, false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/reject/{sendBy}")
    public ResponseEntity<?> rejectRequest(@PathVariable String sendBy) {
        ResponseHandler<?> response;
        try {
            requestService.rejectRequest(sendBy);
            response = new ResponseHandler<>(null, messageSource.getMessage("request.rejected.successfully"), HttpStatus.OK, true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.BAD_REQUEST, false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
