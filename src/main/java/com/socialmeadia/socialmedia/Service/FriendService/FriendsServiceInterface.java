package com.socialmeadia.socialmedia.Service.FriendService;

import com.socialmeadia.socialmedia.Util.PaginationResponse;

public interface FriendsServiceInterface {
    void add(String userName,String sentBy);
    boolean isFriend(String id1, String id2);
    PaginationResponse getAll(int pageSize, String lastEvaluatedKey);
    void delete(String id2);
}
