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


@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDTO {

    @Null
    private String postId;

    @Null
    private String userName;

    @NotBlank(message = "{post.imageName.blank}")
    private String imageName;

    @NotBlank(message = "{post.imagePath.blank}")
    private String imagePath;

    @NotBlank(message = "{post.description.blank}")
    private String description;

    @Null
    private Integer numberOfLikes=0;

    @Null
    private Integer numberOfComments=0;

    @Null
    private Date createdAt=new Date();
}
