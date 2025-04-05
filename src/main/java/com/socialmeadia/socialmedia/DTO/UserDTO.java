package com.socialmeadia.socialmedia.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.socialmeadia.socialmedia.Groups.UserValidations;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    @NotBlank(message = "{user.username.notBlank}",groups = {UserValidations.Register.class, UserValidations.Login.class})
    private String userName;

    @NotBlank(message = "{user.email.notBlank}",groups = {UserValidations.Register.class, UserValidations.Update.class})
    private String email;

    @NotBlank(message = "{user.avatarName.blank}", groups = {UserValidations.Register.class, UserValidations.Update.class})
    private String avatarName;

    @NotBlank(message = "{user.avatarPath.blank}", groups = {UserValidations.Register.class, UserValidations.Update.class})
    private String avatarPath;

    @NotBlank(message = "{user.password.blank}", groups = {UserValidations.Register.class, UserValidations.Login.class, UserValidations.Update.class})
    private String password;

    @Null
    private Integer postCount=0;

    @Null
    private Integer friendCount=0;

    @NotBlank(message = "{user.bio.blank}",groups = {UserValidations.Register.class, UserValidations.Update.class})
    private String bio;

    @NotBlank(message = "{user.gender.blank}" , groups = {UserValidations.Register.class, UserValidations.Update.class})
    private String gender;

    @Positive(message = "{user.age.nonPositive}", groups = {UserValidations.Register.class, UserValidations.Update.class})
    private int age;

    @Null
    private Date createdAt;

}