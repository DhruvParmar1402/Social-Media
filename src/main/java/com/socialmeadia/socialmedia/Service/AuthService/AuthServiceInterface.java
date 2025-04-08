package com.socialmeadia.socialmedia.Service.AuthService;

import com.socialmeadia.socialmedia.DTO.UserDTO;
import com.socialmeadia.socialmedia.Exception.EntityNotFound;

public interface AuthServiceInterface {
    UserDTO save(UserDTO userDTO) throws Exception;
    String login(UserDTO userDTO) throws EntityNotFound;
}
