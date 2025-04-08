package com.socialmeadia.socialmedia.Service.LikeService;

import com.socialmeadia.socialmedia.Util.PaginationResponse;

public interface LikeServiceInterface {
    void likePost(String postId) throws Exception;
    void unLikePost(String postId) throws Exception;
    PaginationResponse getAllByUser(int pageSize, String lastEvaluatedKey);
    PaginationResponse getAllByPostId(String postId, int pageSize, String lastEvaluatedKey);
    void unLikePostByLikeId(String likeId);
}
