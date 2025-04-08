package com.socialmeadia.socialmedia.Service.FeedService;

import com.socialmeadia.socialmedia.DTO.DateRequestDTO;
import com.socialmeadia.socialmedia.DTO.FriendDTO;
import com.socialmeadia.socialmedia.DTO.PostDTO;
import com.socialmeadia.socialmedia.Exception.EntityNotFound;
import com.socialmeadia.socialmedia.Service.FriendService.FriendService;
import com.socialmeadia.socialmedia.Service.PostService.PostService;
import com.socialmeadia.socialmedia.Util.AuthenticatedUserProvider;
import com.socialmeadia.socialmedia.Util.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FeedService implements FeedServiceInterface{

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    @Autowired
    private FriendService friendService;

    @Autowired
    private PostService postService;

    public List<PostDTO> getAllFeeds(DateRequestDTO date,int pageSize,String lastEvaluatedKey) throws EntityNotFound {
        List<FriendDTO> friends = new ArrayList<>();

        do {
            PaginationResponse response = friendService.getAll(5, lastEvaluatedKey);
            List<FriendDTO> list = (List<FriendDTO>) response.getData();

            if (list == null || list.isEmpty()) {
                break;
            }

            friends.addAll(list);
            lastEvaluatedKey = list.get(list.size() - 1).getFriendId();
        } while (lastEvaluatedKey != null);

        if (friends.isEmpty()) {
            return new ArrayList<>();
        }

        List<PostDTO> posts = new ArrayList<>();

        for (FriendDTO friend : friends) {
            String friendUserName = friend.getFriendId();
            lastEvaluatedKey = null;

            do {
                PaginationResponse response = postService.getAll(5, lastEvaluatedKey, date, friendUserName);
                List<PostDTO> list = (List<PostDTO>) response.getData();

                if (list == null || list.isEmpty()) {
                    break;
                }

                posts.addAll(list);
                lastEvaluatedKey = list.get(list.size() - 1).getPostId();
            } while (lastEvaluatedKey != null);
        }

        Collections.shuffle(posts);
        return posts;
    }
}
