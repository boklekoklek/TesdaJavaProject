package com.sbqms.model;

public class Topic {

    private int topicID;
    private String topicName;
    private String description;

    public Topic(int topicID, String topicName, String description) {
        this.topicID = topicID;
        this.topicName = topicName;
        this.description = description;
    }

    public int getTopicID() {
        return topicID;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getDescription() {
        return description;
    }

    public void setTopicID(int topicID) {
        this.topicID = topicID;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
