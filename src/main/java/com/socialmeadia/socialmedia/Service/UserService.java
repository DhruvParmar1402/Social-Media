package com.socialmeadia.socialmedia.Service;

import com.socialmeadia.socialmedia.DTO.UserDTO;
import com.socialmeadia.socialmedia.Exception.EntityNotFound;
import com.socialmeadia.socialmedia.Repository.UserRepository;
import com.socialmeadia.socialmedia.Util.AuthenticatedUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    @Autowired
    private PasswordEncoder encoder;

    public boolean doesUserExist (String userName)
    {
        UserDTO user=userRepository.findUserByUserName(userName);
        return user != null;
    }

    public UserDTO updateUser(UserDTO userDTO) throws EntityNotFound {
        UserDTO existingUser=userRepository.findUserByUserName(authenticatedUserProvider.getUserName());

        if (existingUser==null)
        {
            throw new EntityNotFound("user.notExists");
        }

        userDTO.setUserName(authenticatedUserProvider.getUserName());
        userDTO.setCreatedAt(existingUser.getCreatedAt());
        userDTO.setPassword(encoder.encode(userDTO.getPassword()));
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
