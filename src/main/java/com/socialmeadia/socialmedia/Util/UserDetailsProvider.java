package com.socialmeadia.socialmedia.Util;

import com.socialmeadia.socialmedia.Repository.UserRepository;
import com.socialmeadia.socialmedia.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsProvider implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserDTO user = userRepo.findUserByUserName(userName);

        if (user==null) {
            throw new UsernameNotFoundException ( "User not found with Id: " + userName);
        }

        return User.withUsername(user.getUserName())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}
