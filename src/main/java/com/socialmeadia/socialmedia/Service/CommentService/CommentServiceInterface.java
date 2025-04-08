package com.socialmeadia.socialmedia.Service.CommentService;

import com.socialmeadia.socialmedia.DTO.CommentDTO;
import com.socialmeadia.socialmedia.Exception.EntityNotFound;
import com.socialmeadia.socialmedia.Util.PaginationResponse;

public interface CommentServiceInterface {
    CommentDTO save(CommentDTO commentDTO) throws EntityNotFound;
    PaginationResponse findCommentByPost(int pageSize, String lastEvaluatedKey, String postId) throws EntityNotFound ;
    PaginationResponse findCommentByUser(int pageSize, String lastEvaluatedKey) ;
    void deleteComment(String commentId);
    void deleteCommentByPostId(String postId);
}
