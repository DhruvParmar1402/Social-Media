package com.socialmeadia.socialmedia.Controller;

import com.socialmeadia.socialmedia.DTO.UserDTO;
import com.socialmeadia.socialmedia.Exception.EntityNotFound;
import com.socialmeadia.socialmedia.Groups.UserValidations;
import com.socialmeadia.socialmedia.Service.UserService.UserService;
import com.socialmeadia.socialmedia.Util.MessageSourceImpl;
import com.socialmeadia.socialmedia.Util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSourceImpl messageSource;

    @PutMapping
    public ResponseEntity<?> updateUser(@Validated(UserValidations.Register.class) @RequestBody UserDTO userDTO) {
        ResponseHandler<UserDTO> response;
        try {
            UserDTO user=userService.updateUser(userDTO);
            response = new ResponseHandler<>(user, messageSource.getMessage("user.update.success"), HttpStatus.OK, true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (EntityNotFound e)
        {
            response=new ResponseHandler<>(null, e.getMessage(), HttpStatus.NOT_FOUND,false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e)
        {
            response=new ResponseHandler<>(null, e.getMessage(), HttpStatus.BAD_REQUEST,false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser ()
    {
        ResponseHandler<Object> response;
        try {
            userService.delete();
            response=new ResponseHandler<>(null,messageSource.getMessage("user.delete.success"),HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFound e) {
            response=new ResponseHandler<>(null, e.getMessage(), HttpStatus.NOT_FOUND,false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response=new ResponseHandler<>(null, e.getMessage(), HttpStatus.BAD_REQUEST,false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
