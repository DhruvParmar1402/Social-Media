package com.socialmeadia.socialmedia.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class PostDTO {

    private String postId;

    private String userName;

    @NotBlank(message = "{post.imageName.blank}")
    private String imageName;

    @NotBlank(message = "{post.imagePath.blank}")
    private String imagePath;

    @NotBlank(message = "{post.description.blank}")
    private String description;

    private Integer numberOfLikes=0;

    private Integer numberOfComments=0;

    private Date createdAt=new Date();
}
