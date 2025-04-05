package com.socialmeadia.socialmedia.Entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.socialmeadia.socialmedia.Util.DateConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@DynamoDBTable(tableName = "request")
public class RequestEntity {

    @DynamoDBHashKey(attributeName = "sentTo")
    private String sentTo;

    @DynamoDBAttribute(attributeName = "sentBy")
    @DynamoDBRangeKey(attributeName = "sentBy")
//    @DynamoDBIndexHashKey(globalSecondaryIndexName = "sentBy-index",attributeName = "sentBy")
    private String sentBy;

    @DynamoDBAttribute(attributeName = "status")
    private String status;

    @DynamoDBTypeConverted(converter = DateConverter.class)
    @DynamoDBAttribute(attributeName = "sentDate")
    private Date sentDate=new Date();

}
