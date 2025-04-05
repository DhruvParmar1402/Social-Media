package com.socialmeadia.socialmedia.Util;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter implements DynamoDBTypeConverter<String, Date> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Override
    public String convert(Date date) {
        return dateFormat.format(date);
    }

    @Override
    public Date unconvert(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing date: " + dateString, e);
        }
    }
}
