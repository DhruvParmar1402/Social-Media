package com.socialmeadia.socialmedia.Service;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.socialmeadia.socialmedia.DTO.DateRequestDTO;
import com.socialmeadia.socialmedia.DTO.PostDTO;
import com.socialmeadia.socialmedia.DTO.UserDTO;
import com.socialmeadia.socialmedia.Exception.EntityNotFound;
import com.socialmeadia.socialmedia.Exception.UnAuthorized;
import com.socialmeadia.socialmedia.Repository.PostRepository;
import com.socialmeadia.socialmedia.Repository.UserRepository;
import com.socialmeadia.socialmedia.Util.AuthenticatedUserProvider;
import com.socialmeadia.socialmedia.Util.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    @Autowired
    private UserRepository userRepository;

    public void save(PostDTO post) throws EntityNotFound {
        String userName = authenticatedUserProvider.getUserName();
        if (userRepository.findUserByUserName(userName) == null) {
            throw new EntityNotFound("user.notExists");
        }
        post.setUserName(userName);
        postRepository.save(post);

        UserDTO user = userRepository.findUserByUserName(userName);
        user.setPostCount(user.getPostCount() + 1);
        userRepository.save(user);
    }

    public void update(PostDTO post, String postId) throws Exception {

        String userName = authenticatedUserProvider.getUserName();
        PostDTO existingPostDTO = postRepository.findPostById(postId);

        if (userRepository.findUserByUserName(userName) == null || existingPostDTO == null) {
            throw new EntityNotFound("post.userNameOrPost.notExists");
        }
        if (!existingPostDTO.getUserName().equals(userName)) {
            throw new UnAuthorized("unAuthorized");
        }

        existingPostDTO.setImageName(post.getImageName());
        existingPostDTO.setImagePath(post.getImagePath());
        existingPostDTO.setDescription(post.getDescription());

        postRepository.save(existingPostDTO);
    }

    public void delete(String postId) throws Exception {
        String userName = authenticatedUserProvider.getUserName();
        PostDTO existingPostDTO = postRepository.findPostById(postId);

        if (userRepository.findUserByUserName(userName) == null || existingPostDTO == null) {
            throw new EntityNotFound("post.userNameOrPost.notExists");
        }
        if (!existingPostDTO.getUserName().equals(userName)) {
            throw new UnAuthorized("unAuthorized");
        }
        postRepository.delete(existingPostDTO);

        UserDTO user = userRepository.findUserByUserName(userName);
        user.setPostCount(user.getPostCount() - 1);
        userRepository.save(user);
    }

    public PaginationResponse getAll(int pageSize, String lastEvaluatedKey, DateRequestDTO dateRequestDTO, String friendUsername) throws EntityNotFound {
        String userName = friendUsername == null || friendUsername.isBlank() ? authenticatedUserProvider.getUserName() : friendUsername;

        Map<String, AttributeValue> startKey = null;
        List<PostDTO> postList = new ArrayList<>();
        boolean hasMore = false;
        String nextLastEvaluatedKey = null;

        if (userRepository.findUserByUserName(userName) == null) {
            throw new EntityNotFound("user.notExists");
        }

        do {

            if (lastEvaluatedKey != null && !lastEvaluatedKey.isEmpty()) {

                startKey = new HashMap<>();
                startKey.put("userName", new AttributeValue().withS(userName));
                startKey.put("postId", new AttributeValue().withS(lastEvaluatedKey));
            }

            List<PostDTO> result = postRepository.getAll(userName, startKey, pageSize - postList.size(), dateRequestDTO);

            if (result.isEmpty()) {
                break;
            }
            if (result.getLast() != null) {
                nextLastEvaluatedKey = result.getLast().getPostId();
            }

            postList.addAll(result);
            hasMore = nextLastEvaluatedKey != null;
            lastEvaluatedKey = nextLastEvaluatedKey;

        } while (pageSize > postList.size() && hasMore);

        return new PaginationResponse(postList, lastEvaluatedKey, pageSize, hasMore);
    }

    public PostDTO getPostById(String postId) {
        return postRepository.findPostById(postId);
    }
}
