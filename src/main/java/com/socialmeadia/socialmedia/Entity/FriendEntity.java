package com.socialmeadia.socialmedia.Entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@DynamoDBTable(tableName = "friend")
public class FriendEntity {

    @DynamoDBHashKey(attributeName = "userId")
    private String userId;

    @DynamoDBRangeKey(attributeName = "friendId")
//    @DynamoDBIndexHashKey(globalSecondaryIndexName = "friendId-index",attributeName = "friendId")
    private String friendId;

    private Date dateOfFriendship=new Date();
}
