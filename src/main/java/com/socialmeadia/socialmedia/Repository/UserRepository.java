package com.socialmeadia.socialmedia.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.socialmeadia.socialmedia.DTO.UserDTO;
import com.socialmeadia.socialmedia.Entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {

    @Autowired
    private DynamoDBMapper repo;

    private ModelMapper modelMapper=new ModelMapper();

    public UserDTO findUserByUserName(String name)
    {
        UserEntity user=repo.load(UserEntity.class,name);
        return user==null?null:modelMapper.map(user,UserDTO.class);
    }

    public UserDTO findUserByEmail(String email)
    {
        Map<String , AttributeValue> eav=new HashMap<>();
        eav.put(":email",new AttributeValue().withS(email));

        DynamoDBQueryExpression<UserEntity> expression=new DynamoDBQueryExpression<UserEntity>()
                .withIndexName("email-index")
                .withKeyConditionExpression("email = :email")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false)
                .withScanIndexForward(false);

        List<UserEntity> user=repo.query(UserEntity.class,expression);

        return user.isEmpty()?null:modelMapper.map(user.getFirst(), UserDTO.class);
    }

    public void save(UserDTO userDTO) {
        repo.save(modelMapper.map(userDTO,UserEntity.class));
    }

    public void delete(UserDTO user) {
        repo.delete(modelMapper.map(user,UserEntity.class));
    }
}
