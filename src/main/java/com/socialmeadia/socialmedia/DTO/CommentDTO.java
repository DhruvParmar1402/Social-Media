package com.socialmeadia.socialmedia.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data

public class CommentDTO {
    private String commentId;

    @NotBlank(message = "{comment.postId.blank}")
    private String postId;

    private String userId;

    @NotBlank(message = "{comment.description.blank}")
    private String description;

    private Date createdAt=new Date();
}
