package com.socialmeadia.socialmedia.Controller;

import com.socialmeadia.socialmedia.DTO.DateRequestDTO;
import com.socialmeadia.socialmedia.DTO.PostDTO;
import com.socialmeadia.socialmedia.Exception.EntityNotFound;
import com.socialmeadia.socialmedia.Exception.UnAuthorized;
import com.socialmeadia.socialmedia.Service.PostService.PostService;
import com.socialmeadia.socialmedia.Util.MessageSourceImpl;
import com.socialmeadia.socialmedia.Util.PaginationResponse;
import com.socialmeadia.socialmedia.Util.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private MessageSourceImpl messageSource;

    @PostMapping
    public ResponseEntity<?> addPost(@Valid @RequestBody PostDTO post) {
        ResponseHandler<Object> response;
        try {
            postService.save(post);
            response = new ResponseHandler<>(null, messageSource.getMessage("post.save.success"), HttpStatus.OK, true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFound e) {
            response = new ResponseHandler<>(null, messageSource.getMessage(e.getMessage()), HttpStatus.NOT_FOUND, false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response = new ResponseHandler<>(null, messageSource.getMessage(e.getMessage()), HttpStatus.BAD_REQUEST, false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@Valid @RequestBody PostDTO post, @PathVariable String postId) {
        ResponseHandler<Object> response;
        try {
            postService.update(post, postId);
            response = new ResponseHandler<>(null, messageSource.getMessage("post.update.success"), HttpStatus.OK, false);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFound e) {
            response = new ResponseHandler<>(null, messageSource.getMessage(e.getMessage()), HttpStatus.NOT_FOUND, false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (UnAuthorized e) {
            response = new ResponseHandler<>(null, messageSource.getMessage(e.getMessage()), HttpStatus.UNAUTHORIZED, false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            response = new ResponseHandler<>(null, messageSource.getMessage(e.getMessage()), HttpStatus.BAD_REQUEST, false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable String postId) {
        ResponseHandler<?> response;
        try {
            postService.delete(postId);
            response = new ResponseHandler<>(null, messageSource.getMessage("post.delete.success"), HttpStatus.OK, false);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFound e) {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.NOT_FOUND, false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (UnAuthorized e) {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.UNAUTHORIZED, false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.BAD_REQUEST, false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPostByUserName(@RequestParam(defaultValue = "5") int pageSize, @RequestParam(required = false) String lastEvaluatedKey) {
        ResponseHandler<PaginationResponse> response;
        try {
            response = new ResponseHandler<>(postService.getAll(pageSize, lastEvaluatedKey, null,null), messageSource.getMessage("post.fetched.success"), HttpStatus.OK, false);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFound e) {
            response = new ResponseHandler<>(null, messageSource.getMessage(e.getMessage()), HttpStatus.NOT_FOUND, false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response = new ResponseHandler<>(null, messageSource.getMessage(e.getMessage()), HttpStatus.BAD_REQUEST, false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/betweenDates")
    public ResponseEntity<?> getAllPostBetweenDates(@RequestParam(defaultValue = "5") int pageSize, @RequestParam(required = false) String lastEvaluatedKey, @RequestBody DateRequestDTO dateRequestDTO) {
        ResponseHandler<PaginationResponse> response;
        try {
            response = new ResponseHandler<>(postService.getAll(pageSize, lastEvaluatedKey, dateRequestDTO,null), messageSource.getMessage("post.fetched.success"), HttpStatus.OK, false);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFound e) {
            response = new ResponseHandler<>(null, messageSource.getMessage(e.getMessage()), HttpStatus.NOT_FOUND, false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response = new ResponseHandler<>(null, messageSource.getMessage(e.getMessage()), HttpStatus.BAD_REQUEST, false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
