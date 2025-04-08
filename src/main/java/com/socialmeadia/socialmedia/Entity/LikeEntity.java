package com.socialmeadia.socialmedia.Entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@DynamoDBTable(tableName = "Like")
public class LikeEntity {

    @DynamoDBHashKey(attributeName = "postId")
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "userId-postId-index",attributeName = "postId")
    @DynamoDBAttribute(attributeName = "postId")
    private String postId;

    @DynamoDBRangeKey(attributeName = "userId")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "userId-postId-index", attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    private String userId;

    private Date likeDate;
}
