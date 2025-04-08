package com.socialmeadia.socialmedia.Service.UserService;

import com.socialmeadia.socialmedia.DTO.UserDTO;
import com.socialmeadia.socialmedia.Exception.EntityNotFound;

public interface UserServiceInterface {
    boolean doesUserExist (String userName);
    UserDTO updateUser(UserDTO userDTO) throws EntityNotFound;
    void delete() throws EntityNotFound;
}
