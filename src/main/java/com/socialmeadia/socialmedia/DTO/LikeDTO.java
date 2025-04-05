package com.socialmeadia.socialmedia.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data


public class LikeDTO {

    @NotBlank(message = "{like.postId.blank}")
    private String postId;

    @NotBlank(message = "{like.userId.blank}")
    private String userId;

    private Date likeDate=new Date();
}
