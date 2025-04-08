package com.socialmeadia.socialmedia.Repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.socialmeadia.socialmedia.DTO.DateRequestDTO;
import com.socialmeadia.socialmedia.DTO.PostDTO;
import com.socialmeadia.socialmedia.Entity.PostEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PostRepository {

    @Autowired
    private DynamoDBMapper repo;

    private ModelMapper mapper=new ModelMapper();


    public void save(PostDTO post) {
        repo.save(mapper.map(post, PostEntity.class));
    }

    public PostDTO findPostById(String postId) {
        PostEntity post=repo.load(PostEntity.class,postId);
        return post==null?null:mapper.map(post, PostDTO.class);
    }

    public void delete(PostDTO existingPostDTO) {
        repo.delete(mapper.map(existingPostDTO, PostEntity.class));
    }

    public List<PostDTO> getAll(String userName , String lastEvaluatedKey, int pageSize, DateRequestDTO dateRequestDTO) {

        Map<String, AttributeValue> startKey = null;

        if (lastEvaluatedKey != null && !lastEvaluatedKey.isEmpty()) {

            startKey = new HashMap<>();
            startKey.put("userName", new AttributeValue().withS(userName));
            startKey.put("postId", new AttributeValue().withS(lastEvaluatedKey));
        }

        Map<String , AttributeValue> eav=new HashMap<>();
        eav.put(":userName",new AttributeValue().withS(userName));

        DynamoDBQueryExpression<PostEntity> expression = new DynamoDBQueryExpression<PostEntity>()
                .withIndexName("userName-postId-index")
                .withKeyConditionExpression("userName =:userName")
                .withExclusiveStartKey(startKey)
                .withLimit(pageSize);

        if(dateRequestDTO!=null)
        {
            expression.withFilterExpression("createdAt BETWEEN :startDate AND :endDate");
            eav.put(":startDate",new AttributeValue().withS(dateRequestDTO.getStartDate()));
            eav.put(":endDate",new AttributeValue().withS(dateRequestDTO.getEndDate()));
        }

        expression.withExpressionAttributeValues(eav)
                .withConsistentRead(false)
                .withScanIndexForward(false);

        return repo.queryPage(PostEntity.class,expression).getResults().stream().map((entity)->mapper.map(entity, PostDTO.class)).toList();
    }

}
