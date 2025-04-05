package com.socialmeadia.socialmedia.Controller;


import com.socialmeadia.socialmedia.DTO.RequestDTO;
import com.socialmeadia.socialmedia.Exception.AlreadyFriend;
import com.socialmeadia.socialmedia.Exception.EntityNotFound;
import com.socialmeadia.socialmedia.Service.RequestService;
import com.socialmeadia.socialmedia.Util.MessageSourceImpl;
import com.socialmeadia.socialmedia.Util.PaginationResponse;
import com.socialmeadia.socialmedia.Util.ResponseHandler;
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
    public ResponseEntity<?> sendRequest(@RequestBody RequestDTO request) {
        ResponseHandler<Object> response;
        try {
            requestService.save(request);
            response = new ResponseHandler<>(null, messageSource.getMessage("request.sent.successfully"), HttpStatus.OK, true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (AlreadyFriend e)
        {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.OK, true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (EntityNotFound e)
        {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.NOT_FOUND, false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e) {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllRequests(@RequestParam(defaultValue = "5") int pageSize, @RequestParam(required = false) String lastEvaluatedKey) {
        ResponseHandler<PaginationResponse> response;
        try {
            PaginationResponse page = requestService.getAll(pageSize, lastEvaluatedKey);
            return ResponseEntity.status(HttpStatus.OK).body(page);
        } catch (Exception e) {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
