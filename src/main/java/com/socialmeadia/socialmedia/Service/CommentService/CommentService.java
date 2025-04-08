package com.socialmeadia.socialmedia.Service.CommentService;

import com.socialmeadia.socialmedia.DTO.CommentDTO;
import com.socialmeadia.socialmedia.DTO.PostDTO;
import com.socialmeadia.socialmedia.Exception.EntityNotFound;
import com.socialmeadia.socialmedia.Repository.CommentRepository;
import com.socialmeadia.socialmedia.Repository.PostRepository;
import com.socialmeadia.socialmedia.Util.AuthenticatedUserProvider;
import com.socialmeadia.socialmedia.Util.MessageSourceImpl;
import com.socialmeadia.socialmedia.Util.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentService implements CommentServiceInterface{

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MessageSourceImpl messageSource;




    public CommentDTO save(CommentDTO commentDTO) throws EntityNotFound {
        commentDTO.setUserId(authenticatedUserProvider.getUserName());

        PostDTO post=postRepository.findPostById(commentDTO.getPostId());
        if(post==null)
        {
            throw new EntityNotFound(messageSource.getMessage("post.notExists"));
        }

        commentRepository.save(commentDTO);

        post.setNumberOfComments(post.getNumberOfComments()+1);
        postRepository.save(post);

        return commentDTO;
    }

    public PaginationResponse findCommentByPost(int pageSize, String lastEvaluatedKey, String postId) throws EntityNotFound {
        PostDTO post=postRepository.findPostById(postId);
        if(post==null)
        {
            throw new EntityNotFound(messageSource.getMessage("post.notExists"));
        }

        List<CommentDTO> list=commentRepository.findCommentByPost(pageSize,lastEvaluatedKey,postId);
        boolean hasMore=!(list.size()<pageSize);
        lastEvaluatedKey = hasMore ? list.get(list.size() - 1).getCommentId() : null;
        return new PaginationResponse(list,lastEvaluatedKey,pageSize,hasMore);
    }

    public PaginationResponse findCommentByUser(int pageSize, String lastEvaluatedKey) {
        String userId=authenticatedUserProvider.getUserName();
        List<CommentDTO> list=commentRepository.findCommentByUser(pageSize,lastEvaluatedKey,userId);
        boolean hasMore=!(list.size()<pageSize);
        lastEvaluatedKey = hasMore ? list.get(list.size() - 1).getCommentId() : null;
        return new PaginationResponse(list,lastEvaluatedKey,pageSize,hasMore);
    }

    public void deleteComment(String commentId) {
        String userName=authenticatedUserProvider.getUserName();
        String postId=commentRepository.delete(userName,commentId);
        if(postId==null)
        {
            return;
        }
        PostDTO postDTO=postRepository.findPostById(postId);
        postDTO.setNumberOfComments(postDTO.getNumberOfComments()-1);
        postRepository.save(postDTO);
    }

    public void deleteCommentByPostId(String postId) {
        commentRepository.deleteCommentByPostId(postId);
    }
}
