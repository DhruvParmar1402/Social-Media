package com.socialmeadia.socialmedia.Service.PostService;

import com.socialmeadia.socialmedia.DTO.DateRequestDTO;
import com.socialmeadia.socialmedia.DTO.PostDTO;
import com.socialmeadia.socialmedia.DTO.UserDTO;
import com.socialmeadia.socialmedia.Exception.EntityNotFound;
import com.socialmeadia.socialmedia.Exception.UnAuthorized;
import com.socialmeadia.socialmedia.Repository.PostRepository;
import com.socialmeadia.socialmedia.Repository.UserRepository;
import com.socialmeadia.socialmedia.Service.CommentService.CommentService;
import com.socialmeadia.socialmedia.Service.LikeService.LikeService;
import com.socialmeadia.socialmedia.Util.AuthenticatedUserProvider;
import com.socialmeadia.socialmedia.Util.FileStorageService;
import com.socialmeadia.socialmedia.Util.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService implements PostServiceInterface{

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentService commentService;


    private final LikeService likeService;

    public PostService(@Lazy LikeService likeService) {
        this.likeService = likeService;
    }

    public void save(PostDTO post) throws Exception {
        String userName = authenticatedUserProvider.getUserName();

        UserDTO user = userRepository.findUserByUserName(userName);
        if (user == null) {
            throw new EntityNotFound("user.notExists");
        }

        post.setUserName(userName);

        if (post.getImagePath() != null && post.getImageName() != null) {
            fileStorageService.storeAvatar(userName, post.getImagePath(), post.getImageName());
            post.setImagePath(userName);
        }

        postRepository.save(post);

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

        likeService.unLikePostByLikeId(postId);
        commentService.deleteCommentByPostId(postId);
    }

    public PaginationResponse getAll(int pageSize, String lastEvaluatedKey, DateRequestDTO dateRequestDTO, String friendUsername) throws EntityNotFound {
        String userName = (friendUsername == null || friendUsername.isBlank())
                ? authenticatedUserProvider.getUserName()
                : friendUsername;

        if (userRepository.findUserByUserName(userName) == null) {
            throw new EntityNotFound("user.notExists");
        }

        List<PostDTO> postList = new ArrayList<>();
        boolean hasMore;
        String nextLastEvaluatedKey = lastEvaluatedKey;

        do {
            List<PostDTO> result = postRepository.getAll(userName, nextLastEvaluatedKey, pageSize - postList.size(), dateRequestDTO);

            if (result.isEmpty()) break;

            postList.addAll(result);

            hasMore = result.size() == (pageSize - postList.size());
            nextLastEvaluatedKey = result.get(result.size() - 1).getPostId();

        } while (postList.size() < pageSize && hasMore);

        return new PaginationResponse(postList, nextLastEvaluatedKey, pageSize, postList.size() == pageSize);

    }

    public PostDTO getPostById(String postId) {
        return postRepository.findPostById(postId);
    }
}

