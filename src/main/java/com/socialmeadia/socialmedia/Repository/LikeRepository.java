package com.socialmeadia.socialmedia.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.socialmeadia.socialmedia.DTO.LikeDTO;
import com.socialmeadia.socialmedia.Entity.LikeEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LikeRepository {
    @Autowired
    private DynamoDBMapper repo;

    private ModelMapper mapper=new ModelMapper();

    public void save(LikeDTO like) {
        repo.save(mapper.map(like, LikeEntity.class));
    }

    public LikeDTO getLike(String postId, String userName) {
        LikeEntity likeEntity=repo.load(LikeEntity.class,postId,userName);
        return likeEntity==null?null:mapper.map(likeEntity,LikeDTO.class);
    }

    public void delete(LikeDTO like) {
        repo.delete(mapper.map(like, LikeEntity.class));
    }

    public List<LikeDTO> getAllLikesByUserId(String userName,Map<String , AttributeValue> startKey,int pageSize) {

        Map<String , AttributeValue> eav=new HashMap<>();
        eav.put(":userId",new AttributeValue().withS(userName));

        DynamoDBQueryExpression<LikeEntity> expression=new DynamoDBQueryExpression<LikeEntity>()
                .withIndexName("userId-postId-index")
                .withKeyConditionExpression("userId = :userId")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false)
                .withScanIndexForward(false)
                .withLimit(pageSize);

        if(!startKey.isEmpty())
        {
            expression.withExclusiveStartKey(startKey);
        }

        List<LikeEntity> likes=repo.queryPage(LikeEntity.class,expression).getResults();

        return likes==null?null:likes.stream().map((entity)-> mapper.map(entity, LikeDTO.class)).toList();
    }

    public List<LikeDTO> getAllLikesByPostId(String postId,Map<String, AttributeValue> startKey,int pageSize) {
        Map<String , AttributeValue> eav=new HashMap<>();
        eav.put(":postId",new AttributeValue().withS(postId));

        DynamoDBQueryExpression<LikeEntity> expression=new DynamoDBQueryExpression<LikeEntity>()
                .withKeyConditionExpression("postId = :postId")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false)
                .withScanIndexForward(false)
                .withLimit(pageSize);

        if(!startKey.isEmpty())
        {
            expression.withExclusiveStartKey(startKey);
        }

        List<LikeEntity> likes=repo.queryPage(LikeEntity.class,expression).getResults();

        return likes==null?null:likes.stream().map((entity)-> mapper.map(entity, LikeDTO.class)).toList();
    }
}
