package com.socialmeadia.socialmedia.Controller;

import com.socialmeadia.socialmedia.DTO.UserDTO;
import com.socialmeadia.socialmedia.Exception.EntityNotFound;
import com.socialmeadia.socialmedia.Exception.UserAlreadyExists;
import com.socialmeadia.socialmedia.Service.AuthService;
import com.socialmeadia.socialmedia.Util.MessageSourceImpl;
import com.socialmeadia.socialmedia.Util.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.socialmeadia.socialmedia.Groups.UserValidations;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;
    private final MessageSourceImpl messageSource;

    public AuthController(AuthService service, MessageSourceImpl messageSource) {
        this.service = service;
        this.messageSource = messageSource;
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser (@Validated(UserValidations.Register.class) @RequestBody UserDTO userDTO)
    {
        ResponseHandler<UserDTO> response;
        try
        {
            UserDTO user=service.save(userDTO);
            response=new ResponseHandler<>(user, messageSource.getMessage("user.registration.success"), HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (UserAlreadyExists e)
        {
            response=new ResponseHandler<>(null, messageSource.getMessage(e.getMessage()), HttpStatus.CONFLICT,false);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        catch (Exception e)
        {
            response=new ResponseHandler<>(null, messageSource.getMessage(e.getMessage()), HttpStatus.BAD_REQUEST,false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Validated(UserValidations.Login.class) @RequestBody UserDTO userDTO)
    {
        ResponseHandler<String> response;
        try {

            String JWT=service.login(userDTO);
            response=new ResponseHandler<>(JWT, messageSource.getMessage("user.login.success"), HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (EntityNotFound e)
        {
            response=new ResponseHandler<>(null, messageSource.getMessage(e.getMessage()), HttpStatus.NOT_FOUND,false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response=new ResponseHandler<>(null, messageSource.getMessage(e.getMessage()), HttpStatus.BAD_REQUEST,false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }
}
