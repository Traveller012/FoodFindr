package com.foodfindr.foodfindr.com.foodfindr.foodfindr.model;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;

/**
 * Created by nites on 12/19/2015.
 */
@DynamoDBTable(tableName="User_Recommendation")
public class UserRecommendation {

    private String userID;

    private List<String> recommendations;

    public String getUserID() {
        return userID;
    }

    @DynamoDBAttribute(attributeName = "User_ID")
    public void setUserID(String userID) {
        this.userID = userID;
    }

    @DynamoDBAttribute(attributeName = "Recommendations")
    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }
}
