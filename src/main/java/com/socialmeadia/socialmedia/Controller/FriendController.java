package com.socialmeadia.socialmedia.Controller;

import com.socialmeadia.socialmedia.Service.FriendService;
import com.socialmeadia.socialmedia.Util.MessageSourceImpl;
import com.socialmeadia.socialmedia.Util.PaginationResponse;
import com.socialmeadia.socialmedia.Util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friends")
public class FriendController {

    @Autowired
    private MessageSourceImpl messageSource;

    @Autowired
    private FriendService friendService;

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "5") int pageSize, @RequestParam(required = false) String lastEvaluatedKey)
    {
        ResponseHandler<PaginationResponse> response;
        try {
            response=new ResponseHandler<>(friendService.getAll(pageSize,lastEvaluatedKey),messageSource.getMessage("friends.fetched.successfully"),HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (Exception e)
        {
            response=new ResponseHandler<>(null, messageSource.getMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/delete/{userName}")
    public ResponseEntity<?> deleteFriend (@PathVariable String userName)
    {
        ResponseHandler<Object> response;
        try
        {
            friendService.delete(userName);
            response=new ResponseHandler<>(null,messageSource.getMessage("friend.deleted.success"),HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (Exception e)
        {
            response=new ResponseHandler<>(null, messageSource.getMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
