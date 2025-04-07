package com.socialmeadia.socialmedia.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
@Data

public class RequestDTO {

    @NotBlank(message = "{request.sentTo.blank}")
    private String sentTo;

    private String sentBy;

    private Date sentDate=new Date();
}
