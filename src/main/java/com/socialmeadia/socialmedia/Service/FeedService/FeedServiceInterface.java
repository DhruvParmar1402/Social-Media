package com.socialmeadia.socialmedia.Service.FeedService;

import com.socialmeadia.socialmedia.DTO.DateRequestDTO;
import com.socialmeadia.socialmedia.DTO.PostDTO;
import com.socialmeadia.socialmedia.Exception.EntityNotFound;

import java.util.List;

public interface FeedServiceInterface {
    List<PostDTO> getAllFeeds(DateRequestDTO date, int pageSize, String lastEvaluatedKey) throws EntityNotFound;
}
