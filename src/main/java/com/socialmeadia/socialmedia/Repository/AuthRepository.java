package com.socialmeadia.socialmedia.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.socialmeadia.socialmedia.DTO.UserDTO;
import com.socialmeadia.socialmedia.Entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AuthRepository {

    @Autowired
    private DynamoDBMapper repo;

    private ModelMapper modelMapper=new ModelMapper();

    public void save(UserDTO userDTO) {
        repo.save(modelMapper.map(userDTO, UserEntity.class));
    }
}
