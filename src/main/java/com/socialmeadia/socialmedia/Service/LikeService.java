package com.socialmeadia.socialmedia.Service;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.socialmeadia.socialmedia.DTO.LikeDTO;
import com.socialmeadia.socialmedia.DTO.PostDTO;
import com.socialmeadia.socialmedia.DTO.RequestDTO;
import com.socialmeadia.socialmedia.Exception.EntityNotFound;
import com.socialmeadia.socialmedia.Repository.LikeRepository;
import com.socialmeadia.socialmedia.Util.AuthenticatedUserProvider;
import com.socialmeadia.socialmedia.Util.MessageSourceImpl;
import com.socialmeadia.socialmedia.Util.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private MessageSourceImpl messageSource;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    public void likePost(String postId) throws EntityNotFound {
        PostDTO post=postService.getPostById(postId);

        if(post==null)
        {
            throw new EntityNotFound(messageSource.getMessage("post.notExists"));
        }

        LikeDTO like=new LikeDTO();
        like.setUserId(authenticatedUserProvider.getUserName());
        like.setPostId(postId);
        likeRepository.save(like);

        post.setNumberOfLikes(post.getNumberOfLikes()+1);
        postService.save(post);
    }


    public void unLikePost(String postId) throws EntityNotFound {
        PostDTO post=postService.getPostById(postId);

        if(post==null)
        {
            throw new EntityNotFound(messageSource.getMessage("post.notExists"));
        }

        LikeDTO like=likeRepository.getLike(postId,authenticatedUserProvider.getUserName());

        likeRepository.delete(like);

        post.setNumberOfLikes(post.getNumberOfLikes()-1);
        postService.save(post);
    }

    public PaginationResponse getAllByUser(int pageSize,String lastEvaluatedKey) {
        String userName=authenticatedUserProvider.getUserName();

        Map<String , AttributeValue> startKey=new HashMap<>();
        if(lastEvaluatedKey!=null)
        {
            startKey.put("userId",new AttributeValue().withS(userName));
            startKey.put("postId",new AttributeValue().withS(lastEvaluatedKey));
        }

        List<LikeDTO> list=likeRepository.getAllLikesByUserId(userName,startKey,pageSize);

        boolean hasMore=!(list.size()<pageSize);
        lastEvaluatedKey=hasMore?list.getLast().getPostId():null;

        return new PaginationResponse(list,lastEvaluatedKey,pageSize,hasMore);
    }

    public PaginationResponse getAllByPostId(String postId,int pageSize,String lastEvaluatedKey) {
        String userName=authenticatedUserProvider.getUserName();

        Map<String , AttributeValue> startKey=new HashMap<>();

        if(lastEvaluatedKey!=null)
        {
            startKey.put("postId",new AttributeValue().withS(postId));
            startKey.put("userId",new AttributeValue().withS(lastEvaluatedKey));
        }

        List<LikeDTO> list=likeRepository.getAllLikesByPostId(postId,startKey,pageSize);

        boolean hasMore=!(list.size()<pageSize);
        lastEvaluatedKey=hasMore?list.getLast().getUserId():null;

        return new PaginationResponse(list,lastEvaluatedKey,pageSize,hasMore);
    }
}
