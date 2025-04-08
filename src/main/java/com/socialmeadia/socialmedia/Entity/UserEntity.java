package com.socialmeadia.socialmedia.Entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data

@DynamoDBTable(tableName = "user")
public class UserEntity {

    @DynamoDBHashKey(attributeName = "userName")
    private String userName;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "email-index",attributeName = "email")
    @DynamoDBAttribute(attributeName = "email")
    private String email;

    @DynamoDBAttribute(attributeName = "avatarName")
    private String avatarName;

    @DynamoDBAttribute(attributeName = "avatarPath")
    private String avatarPath;

    @DynamoDBAttribute(attributeName = "password")
    private String password;

    @DynamoDBAttribute(attributeName = "postCount")
    private Integer postCount;

    @DynamoDBAttribute(attributeName = "friendCount")
    private Integer friendCount;

    @DynamoDBAttribute(attributeName = "bio")
    private String bio;

    @DynamoDBAttribute(attributeName = "gender")
    private String gender;

    @DynamoDBAttribute(attributeName = "age")
    private int age;

    private Date createdAt;

}
