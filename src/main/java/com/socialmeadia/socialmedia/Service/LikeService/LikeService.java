package com.socialmeadia.socialmedia.Service.LikeService;

import com.socialmeadia.socialmedia.DTO.LikeDTO;
import com.socialmeadia.socialmedia.DTO.PostDTO;
import com.socialmeadia.socialmedia.Exception.EntityNotFound;
import com.socialmeadia.socialmedia.Repository.LikeRepository;
import com.socialmeadia.socialmedia.Repository.PostRepository;
import com.socialmeadia.socialmedia.Service.PostService.PostService;
import com.socialmeadia.socialmedia.Util.AuthenticatedUserProvider;
import com.socialmeadia.socialmedia.Util.MessageSourceImpl;
import com.socialmeadia.socialmedia.Util.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService implements LikeServiceInterface {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MessageSourceImpl messageSource;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    public void likePost(String postId) throws Exception {
        PostDTO post = postService.getPostById(postId);
        if (post == null) {
            throw new EntityNotFound(messageSource.getMessage("post.notExists"));
        }

        String userId = authenticatedUserProvider.getUserName();
        LikeDTO existingLike = likeRepository.getLike(postId, userId);
        if (existingLike != null) {
            return;
        }

        LikeDTO like = new LikeDTO();
        like.setUserId(userId);
        like.setPostId(postId);

        post.setNumberOfLikes(post.getNumberOfLikes() + 1);
        postRepository.save(post);

        likeRepository.save(like);
    }

    public void unLikePost(String postId) throws Exception {
        PostDTO post = postService.getPostById(postId);
        if (post == null) {
            throw new EntityNotFound(messageSource.getMessage("post.notExists"));
        }

        String userId = authenticatedUserProvider.getUserName();
        LikeDTO like = likeRepository.getLike(postId, userId);

        if (like == null) {
            return;
        }

        likeRepository.delete(like);

        int currentLikes = post.getNumberOfLikes();
        if (currentLikes > 0) {
            post.setNumberOfLikes(currentLikes - 1);
        }
        postRepository.save(post);
    }

    public PaginationResponse getAllByUser(int pageSize, String lastEvaluatedKey) {
        String userName = authenticatedUserProvider.getUserName();
        List<LikeDTO> list = likeRepository.getAllLikesByUserId(userName, lastEvaluatedKey, pageSize);

        boolean hasMore = list.size() == pageSize;
        lastEvaluatedKey = hasMore ? list.get(list.size() - 1).getPostId() : null;

        return new PaginationResponse(list, lastEvaluatedKey, pageSize, hasMore);
    }

    public PaginationResponse getAllByPostId(String postId, int pageSize, String lastEvaluatedKey) {
        List<LikeDTO> list = likeRepository.getAllLikesByPostId(postId, lastEvaluatedKey, pageSize);

        boolean hasMore = list.size() == pageSize;
        lastEvaluatedKey = hasMore ? list.get(list.size() - 1).getUserId() : null;

        return new PaginationResponse(list, lastEvaluatedKey, pageSize, hasMore);
    }

    public void unLikePostByLikeId(String likeId){
        likeRepository.unlikePostById(likeId);
    }
}
