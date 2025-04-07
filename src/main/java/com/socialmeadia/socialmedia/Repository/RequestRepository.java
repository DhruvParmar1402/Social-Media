package com.socialmeadia.socialmedia.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.socialmeadia.socialmedia.DTO.RequestDTO;
import com.socialmeadia.socialmedia.Entity.RequestEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RequestRepository {
    @Autowired
    private DynamoDBMapper repo;

    ModelMapper mapper = new ModelMapper();

    public void save(RequestDTO request) {
        repo.save(mapper.map(request, RequestEntity.class));
    }

    public RequestDTO findRequest(String userName, String sendTo) {
        RequestEntity request = repo.load(RequestEntity.class, sendTo ,userName);
        return request==null?null:mapper.map(request, RequestDTO.class);
    }

    public void delete(RequestDTO requestDTO) {
        repo.delete(mapper.map(requestDTO, RequestEntity.class));
    }

    public List<RequestDTO> getAll(String userName, String lastEvaluatedKey, int pageSize) {

        Map<String ,AttributeValue> startKey=new HashMap<>();
        if(lastEvaluatedKey!=null)
        {
            startKey.put("sentTo",new AttributeValue().withS(userName));
            startKey.put("sentBy",new AttributeValue().withS(lastEvaluatedKey));
        }

        Map<String , AttributeValue> eav=new HashMap<>();
        eav.put(":sentTo",new AttributeValue().withS(userName));

        DynamoDBQueryExpression<RequestEntity> expression=new DynamoDBQueryExpression<RequestEntity>()
                .withKeyConditionExpression("sentTo = :sentTo")
                .withExpressionAttributeValues(eav)
                .withLimit(pageSize)
                .withConsistentRead(false)
                .withScanIndexForward(false);

        if(!startKey.isEmpty())
        {
            expression.withExclusiveStartKey(startKey);
        }

        return repo.queryPage(RequestEntity.class,expression).getResults().stream().map((entity)->mapper.map(entity, RequestDTO.class)).toList();
    }
}
