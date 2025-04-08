package com.socialmeadia.socialmedia.Controller;

import com.socialmeadia.socialmedia.DTO.DateRequestDTO;
import com.socialmeadia.socialmedia.DTO.PostDTO;
import com.socialmeadia.socialmedia.Service.FeedService.FeedService;
import com.socialmeadia.socialmedia.Util.MessageSourceImpl;
import com.socialmeadia.socialmedia.Util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feed")
public class FeedController {

    @Autowired
    private FeedService feedService;

    @Autowired
    private MessageSourceImpl messageSource;

    @PostMapping
    public ResponseEntity<?> getFeed(@RequestBody DateRequestDTO date, @RequestParam(defaultValue = "5") int pageSize, @RequestParam(required = false) String lastEvaluatedKey) {
        ResponseHandler<List<PostDTO>> response;
        try {
            List<PostDTO> feeds = feedService.getAllFeeds(date,pageSize,lastEvaluatedKey);
            response=new ResponseHandler<>(feeds,messageSource.getMessage("feeds.fetched.success"),HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.BAD_REQUEST, false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllFeed( @RequestParam(defaultValue = "5") int pageSize, @RequestParam(required = false) String lastEvaluatedKey) {
        ResponseHandler<List<PostDTO>> response;
        try {
            List<PostDTO> feeds = feedService.getAllFeeds(null,pageSize,lastEvaluatedKey);
            response=new ResponseHandler<>(feeds,messageSource.getMessage("feeds.fetched.success"),HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.BAD_REQUEST, false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
