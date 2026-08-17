package com.sbqms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sbqms.model.Topic;

public class TopicDAO {

    private Connection connection;

    public TopicDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Topic> getAllTopics() {

        List<Topic> topics = new ArrayList<>();
        String sql = "SELECT * FROM Topic ORDER BY topicID";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                topics.add(new Topic(
                        result.getInt("topicID"),
                        result.getString("topicName"),
                        result.getString("description")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return topics;
    }

    public Topic getTopicById(int topicID) {

        String sql = "SELECT * FROM Topic WHERE topicID = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, topicID);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return new Topic(
                        result.getInt("topicID"),
                        result.getString("topicName"),
                        result.getString("description")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
