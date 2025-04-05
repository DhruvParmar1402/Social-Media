package com.socialmeadia.socialmedia.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data

public class FriendDTO {

    @NotBlank(message = "{friend.userId.blank}")
    private String userId;

    @NotBlank(message = "{friend.friendId.blank}")
    private String friendId;

    private Date dateOfFriendship=new Date();
}
