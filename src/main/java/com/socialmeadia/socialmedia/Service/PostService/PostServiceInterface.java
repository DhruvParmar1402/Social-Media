package com.socialmeadia.socialmedia.Service.PostService;

import com.socialmeadia.socialmedia.DTO.DateRequestDTO;
import com.socialmeadia.socialmedia.DTO.PostDTO;
import com.socialmeadia.socialmedia.Exception.EntityNotFound;
import com.socialmeadia.socialmedia.Util.PaginationResponse;

public interface PostServiceInterface {
    void save(PostDTO post) throws Exception;
    void update(PostDTO post, String postId) throws Exception;
    void delete(String postId) throws Exception;
    PaginationResponse getAll(int pageSize, String lastEvaluatedKey, DateRequestDTO dateRequestDTO, String friendUsername) throws EntityNotFound;
    PostDTO getPostById(String postId);
}
