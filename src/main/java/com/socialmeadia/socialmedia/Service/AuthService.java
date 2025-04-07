package com.socialmeadia.socialmedia.Service;

import com.socialmeadia.socialmedia.DTO.UserDTO;
import com.socialmeadia.socialmedia.Exception.EntityNotFound;
import com.socialmeadia.socialmedia.Exception.UserAlreadyExists;
import com.socialmeadia.socialmedia.Repository.AuthRepository;
import com.socialmeadia.socialmedia.Repository.UserRepository;
import com.socialmeadia.socialmedia.Util.FileStorageService;
import com.socialmeadia.socialmedia.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;



    public UserDTO save(UserDTO userDTO) throws Exception {

        if (userRepository.findUserByUserName(userDTO.getUserName()) != null ||
                userRepository.findUserByEmail(userDTO.getEmail()) != null) {
            throw new UserAlreadyExists("user.alreadyExists");
        }

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        if (userDTO.getAvatarPath() != null && userDTO.getAvatarName() != null) {
            fileStorageService.storeAvatar(userDTO.getUserName(), userDTO.getAvatarPath(), userDTO.getAvatarName());
            userDTO.setAvatarPath(userDTO.getUserName());
        }

        authRepository.save(userDTO);
        return userDTO;
    }

    public String login(UserDTO userDTO) throws EntityNotFound {
        if(userRepository.findUserByUserName(userDTO.getUserName())==null)
        {
            throw new EntityNotFound("user.notExists");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getUserName(), userDTO.getPassword()));

        return jwtUtil.generateToken(userDTO.getUserName(), userDTO.getPassword());
    }
}
