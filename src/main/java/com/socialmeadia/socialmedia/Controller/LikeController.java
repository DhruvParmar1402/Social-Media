package com.socialmeadia.socialmedia.Controller;

import com.socialmeadia.socialmedia.Exception.EntityNotFound;
import com.socialmeadia.socialmedia.Service.LikeService;
import com.socialmeadia.socialmedia.Util.MessageSourceImpl;
import com.socialmeadia.socialmedia.Util.PaginationResponse;
import com.socialmeadia.socialmedia.Util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private MessageSourceImpl messageSource;

    @GetMapping("/{postId}")
    public ResponseEntity<?> addLike(@PathVariable String postId)
    {
        ResponseHandler<Object> response;
        try {
            likeService.likePost(postId);
            response=new ResponseHandler<>(null,messageSource.getMessage("like.success"), HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (EntityNotFound e)
        {
            response=new ResponseHandler<>(null,e.getMessage(), HttpStatus.NOT_FOUND,false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e)
        {
            response=new ResponseHandler<>(null,e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deleteLike (@PathVariable String postId)
    {
        ResponseHandler<Object> response;
        try {
            likeService.unLikePost(postId);
            response=new ResponseHandler<>(null,messageSource.getMessage("unLike.success"), HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (EntityNotFound e)
        {
            response=new ResponseHandler<>(null,e.getMessage(), HttpStatus.NOT_FOUND,false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e)
        {
            response=new ResponseHandler<>(null,e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/byUserName")
    public ResponseEntity<?> getAllLikesByUser(@RequestParam(defaultValue = "5") int pageSize, @RequestParam(required = false) String lastEvaluatedKey)
    {
        ResponseHandler<PaginationResponse> response;
        try {
            PaginationResponse likes=likeService.getAllByUser(pageSize,lastEvaluatedKey);
            response=new ResponseHandler<>(likes,messageSource.getMessage("likes.fetched.success"),HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (Exception e)
        {
            response=new ResponseHandler<>(null,e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/byPostId/{postId}")
    public ResponseEntity<?> getAllLikeByPost(@RequestParam(defaultValue = "5") int pageSize, @RequestParam(required = false) String lastEvaluatedKey,@PathVariable String postId)
    {
        ResponseHandler<PaginationResponse> response;
        try {
            PaginationResponse likes=likeService.getAllByPostId(postId,pageSize,lastEvaluatedKey);
            response=new ResponseHandler<>(likes,messageSource.getMessage("likes.fetched.success"),HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (Exception e)
        {
            response=new ResponseHandler<>(null,e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
