package com.socialmeadia.socialmedia.Service;

import com.socialmeadia.socialmedia.DTO.FriendDTO;
import com.socialmeadia.socialmedia.DTO.PostDTO;
import com.socialmeadia.socialmedia.Exception.EntityNotFound;
import com.socialmeadia.socialmedia.Util.AuthenticatedUserProvider;
import com.socialmeadia.socialmedia.Util.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FeedService {

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    @Autowired
    private FriendService friendService;

    @Autowired
    private PostService postService;

    public List<PostDTO> getAllFeeds() throws EntityNotFound {
        String lastEvaluatedKey = null;
        boolean hasMore = false;
        PaginationResponse response = null;
        List<FriendDTO> friends = new ArrayList<>();

        do {
            response = friendService.getAll(5, lastEvaluatedKey);
            List<FriendDTO> list = (List<FriendDTO>) response.getData();

            friends.addAll(list);

            lastEvaluatedKey = list.getLast().getFriendId();
            hasMore = response.isHasMore();
        } while (hasMore);

        List<PostDTO> posts = new ArrayList<>();

        for (FriendDTO friend : friends) {
            String friendUserName = friend.getFriendId();
            lastEvaluatedKey = null;
            response = null;
            hasMore = false;

            do {
                response = postService.getAll(5, lastEvaluatedKey, null, friendUserName);
                List<PostDTO> list = (List<PostDTO>) response.getData();

                if (list.size() == 0) {
                    break;
                }

                posts.addAll(list);

                lastEvaluatedKey = list.getLast().getPostId();
                hasMore = response.isHasMore();
            } while (response != null && hasMore);
        }
        Collections.shuffle(posts);
        return posts;
    }
}
