package com.socialmeadia.socialmedia.Entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data

@DynamoDBTable(tableName = "comment")
public class CommentEntity {

    @DynamoDBAttribute(attributeName = "postId")
    @DynamoDBHashKey(attributeName = "postId")
    private String postId;

    @DynamoDBRangeKey(attributeName = "commentId")
    @DynamoDBIndexRangeKey(globalSecondaryIndexName ="userId-commentId-index" ,attributeName = "commentId")
    @DynamoDBAutoGeneratedKey
    private String commentId;

    @DynamoDBAttribute(attributeName = "userId")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "userId-commentId-index",attributeName = "userId")
    private String userId;

    @DynamoDBAttribute(attributeName = "description")
    private String description;

    private Date createdAt;
}
