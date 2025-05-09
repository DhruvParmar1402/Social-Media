package com.socialmeadia.socialmedia.Entity;


import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.socialmeadia.socialmedia.Util.DateConverter;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data

@DynamoDBTable(tableName = "post")
public class PostEntity {

    @DynamoDBHashKey(attributeName = "postId")
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "userName-postId-index",attributeName = "postId")
    @DynamoDBAutoGeneratedKey
    private String postId;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "userName-postId-index",attributeName = "userName")
    @DynamoDBAttribute(attributeName = "userName")
    private String userName;

    @DynamoDBAttribute(attributeName = "imageName")
    private String imageName;

    @DynamoDBAttribute(attributeName = "imagePath")
    private String imagePath;

    @DynamoDBAttribute(attributeName = "description")
    private String description;

    @DynamoDBAttribute(attributeName = "numberOfLikes")
    private Integer numberOfLikes=0;

    @DynamoDBAttribute(attributeName = "numberOfComments")
    private Integer numberOfComments=0;

    @DynamoDBAttribute(attributeName = "createdAt")
    @DynamoDBTypeConverted(converter = DateConverter.class)
    private Date createdAt;

}
