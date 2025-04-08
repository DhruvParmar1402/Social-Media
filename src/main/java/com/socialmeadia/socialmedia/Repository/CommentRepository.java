package com.socialmeadia.socialmedia.Repository;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.socialmeadia.socialmedia.DTO.CommentDTO;
import com.socialmeadia.socialmedia.Entity.CommentEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CommentRepository {
    @Autowired
    private DynamoDBMapper repo;

    private ModelMapper mapper=new ModelMapper();


    public void save(CommentDTO commentDTO) {
        repo.save(mapper.map(commentDTO, CommentEntity.class));
    }

    public List<CommentDTO> findCommentByPost(int pageSize, String lastEvaluatedKey, String postId) {
        Map<String, AttributeValue> startKey=new HashMap<>();
        if(lastEvaluatedKey!=null)
        {
            startKey.put("postId",new AttributeValue().withS(postId));
            startKey.put("commentId",new AttributeValue().withS(lastEvaluatedKey));
        }

        Map<String , AttributeValue> eav=new HashMap<>();
        eav.put(":postId",new AttributeValue().withS(postId));

        DynamoDBQueryExpression<CommentEntity> expression=new DynamoDBQueryExpression<CommentEntity>()
                .withKeyConditionExpression("postId = :postId")
                .withExpressionAttributeValues(eav)
                .withLimit(pageSize)
                .withScanIndexForward(false)
                .withConsistentRead(false);

        if(!startKey.isEmpty())
        {
            expression.withExclusiveStartKey(startKey);
        }
        List<CommentEntity> comments=repo.queryPage(CommentEntity.class,expression).getResults();
        return comments.isEmpty() ? null : comments.stream().map((entity)->mapper.map(entity, CommentDTO.class)).toList();
    }

    public List<CommentDTO> findCommentByUser(int pageSize, String lastEvaluatedKey, String userId) {
        Map<String ,AttributeValue> startKey=new HashMap<>();

        if(lastEvaluatedKey!=null)
        {
            startKey.put("userId",new AttributeValue().withS(userId));
            startKey.put("commentId",new AttributeValue().withS(lastEvaluatedKey));
        }

        Map<String , AttributeValue> eav=new HashMap<>();
        eav.put(":userId",new AttributeValue().withS(userId));

        DynamoDBQueryExpression<CommentEntity> expression=new DynamoDBQueryExpression<CommentEntity>()
                .withIndexName("userId-commentId-index")
                .withKeyConditionExpression("userId = :userId")
                .withExpressionAttributeValues(eav)
                .withLimit(pageSize)
                .withScanIndexForward(false)
                .withConsistentRead(false);

        if(!startKey.isEmpty())
        {
            expression.withExclusiveStartKey(startKey);
        }
        List<CommentEntity> comments=repo.queryPage(CommentEntity.class,expression).getResults();
        return comments.isEmpty() ? null : comments.stream().map((entity)->mapper.map(entity, CommentDTO.class)).toList();
    }

    public String delete(String userName, String commentId) {
        Map<String, AttributeValue> eav=new HashMap<>();
        eav.put(":userId",new AttributeValue().withS(userName));
        eav.put(":commentId",new AttributeValue().withS(commentId));

        DynamoDBQueryExpression<CommentEntity> expression=new DynamoDBQueryExpression<CommentEntity>()
                .withIndexName("userId-commentId-index")
                .withKeyConditionExpression("userId = :userId AND commentId = :commentId")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false)
                .withScanIndexForward(false);

        List<CommentEntity> comments=repo.query(CommentEntity.class,expression);
        if(comments.isEmpty())
        {
            return null;
        }
        CommentEntity comment=comments.getFirst();
        repo.delete(comment);
        return comment.getPostId();
    }


    public void deleteCommentByPostId(String postId) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":postId", new AttributeValue().withS(postId));

        DynamoDBQueryExpression<CommentEntity> expression=new DynamoDBQueryExpression<CommentEntity>()
                .withKeyConditionExpression("postId = :postId")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false)
                .withScanIndexForward(false);

        List<CommentEntity> comments=repo.query(CommentEntity.class,expression);


        for (CommentEntity comment:comments)
        {
            repo.delete(comment);
        }
    }
}
