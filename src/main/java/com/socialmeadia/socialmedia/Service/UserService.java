package com.socialmeadia.socialmedia.Service;

import com.socialmeadia.socialmedia.DTO.UserDTO;
import com.socialmeadia.socialmedia.Exception.EntityNotFound;
import com.socialmeadia.socialmedia.Repository.UserRepository;
import com.socialmeadia.socialmedia.Util.AuthenticatedUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    public boolean doesUserExist (String userName)
    {
        UserDTO user=userRepository.findUserByUserName(userName);
        return user==null?false:true;
    }

    public UserDTO updateUser(UserDTO userDTO) throws EntityNotFound {
        UserDTO existingUser=userRepository.findUserByUserName(authenticatedUserProvider.getUserName());

        if (existingUser==null)
        {
            throw new EntityNotFound("user.notExists");
        }

        userDTO.setUserName(authenticatedUserProvider.getUserName());
        userDTO.setCreatedAt(new Date());

        userRepository.save(userDTO);

        return userDTO;
    }

    public void delete() throws EntityNotFound {
        UserDTO user=userRepository.findUserByUserName(authenticatedUserProvider.getUserName());
        if(user==null)
        {
            throw new EntityNotFound("user.notExists");
        }
        userRepository.delete(user);
    }
}
