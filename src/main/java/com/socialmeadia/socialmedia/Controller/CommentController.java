package com.socialmeadia.socialmedia.Controller;


import com.socialmeadia.socialmedia.DTO.CommentDTO;
import com.socialmeadia.socialmedia.Exception.EntityNotFound;
import com.socialmeadia.socialmedia.Service.CommentService;
import com.socialmeadia.socialmedia.Util.MessageSourceImpl;
import com.socialmeadia.socialmedia.Util.PaginationResponse;
import com.socialmeadia.socialmedia.Util.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final MessageSourceImpl messageSource;

    public CommentController(CommentService commentService, MessageSourceImpl messageSource) {
        this.commentService = commentService;
        this.messageSource = messageSource;
    }


    @PostMapping
    public ResponseEntity<?> addComment (@Valid @RequestBody CommentDTO commentDTO)
    {
        ResponseHandler<Object>response;
        try
        {
            CommentDTO savedComment =commentService.save(commentDTO);
            response=new ResponseHandler<>(savedComment,messageSource.getMessage("comment.post.success"), HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (EntityNotFound e)
        {
            response=new ResponseHandler<>(null,e.getMessage(), HttpStatus.NOT_FOUND,false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e)
        {
            response=new ResponseHandler<>(null,e.getMessage(), HttpStatus.BAD_REQUEST,false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/byPost/{postId}")
    public ResponseEntity<?> getCommentByPost(@RequestParam(defaultValue = "5") int pageSize, @RequestParam(required = false) String lastEvaluatedKey ,@PathVariable String postId)
    {
        ResponseHandler<PaginationResponse> response;
        try {
            PaginationResponse page=commentService.findCommentByPost(pageSize,lastEvaluatedKey,postId);
            response=new ResponseHandler<>(page,messageSource.getMessage("comment.fetched.success"),HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (EntityNotFound e)
        {
            response=new ResponseHandler<>(null,e.getMessage(), HttpStatus.NOT_FOUND,false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e)
        {
            response=new ResponseHandler<>(null,e.getMessage(), HttpStatus.BAD_REQUEST,false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/byUser")
    public ResponseEntity<?> getCommentByUser(@RequestParam(defaultValue = "5") int pageSize, @RequestParam(required = false) String lastEvaluatedKey )
    {
        ResponseHandler<PaginationResponse> response;
        try {
            PaginationResponse page=commentService.findCommentByUser(pageSize,lastEvaluatedKey);
            response=new ResponseHandler<>(page,messageSource.getMessage("comment.fetched.success"),HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (Exception e)
        {
            response=new ResponseHandler<>(null,e.getMessage(), HttpStatus.BAD_REQUEST,false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable String commentId)
    {
        ResponseHandler<Object> response;
        try
        {
            commentService.deleteComment(commentId);
            response=new ResponseHandler<>(null,messageSource.getMessage("comment.deleted.success"),HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (Exception e)
        {
            response=new ResponseHandler<>(null,e.getMessage(), HttpStatus.BAD_REQUEST,false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
