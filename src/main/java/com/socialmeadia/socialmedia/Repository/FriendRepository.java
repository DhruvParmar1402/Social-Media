package com.socialmeadia.socialmedia.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.socialmeadia.socialmedia.DTO.FriendDTO;
import com.socialmeadia.socialmedia.Entity.FriendEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FriendRepository {
    @Autowired
    private DynamoDBMapper repo;

    private ModelMapper mapper=new ModelMapper();

    public void save(FriendDTO friend) {
        repo.save(mapper.map(friend, FriendEntity.class));
    }

    public FriendDTO find(String id1, String id2) {
        FriendEntity friend=repo.load(FriendEntity.class,id1,id2);
        return friend==null?null:mapper.map(friend,FriendDTO.class);
    }

    public List<FriendDTO> getAll(String userName, int pageSize, String lastEvaluatedKey) {
        Map<String , AttributeValue> startKey=new HashMap<>();
        if(lastEvaluatedKey!=null && !lastEvaluatedKey.isBlank())
        {
            startKey.put("userId",new AttributeValue().withS(userName));
            startKey.put("friendId",new AttributeValue().withS(lastEvaluatedKey));
        }

        Map<String, AttributeValue> eav=new HashMap<>();
        eav.put(":userId",new AttributeValue().withS(userName));

        DynamoDBQueryExpression<FriendEntity> expression=new DynamoDBQueryExpression<FriendEntity>()
                .withKeyConditionExpression("userId = :userId")
                .withExpressionAttributeValues(eav)
                .withLimit(pageSize)
                .withScanIndexForward(false)
                .withConsistentRead(false);
        if (!startKey.isEmpty())
        {
               expression.withExclusiveStartKey(startKey);
        }
        return repo.queryPage(FriendEntity.class,expression).getResults().stream().map((entity)->mapper.map(entity, FriendDTO.class)).toList();
    }

    public void delete(FriendDTO friend) {
        repo.delete(mapper.map(friend,FriendEntity.class));
    }
}
