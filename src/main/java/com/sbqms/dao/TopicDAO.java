package com.sbqms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
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

    public int createTopic(String topicName, String description) {

        String sql = "INSERT INTO Topic (topicName, description) VALUES (?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS
            );

            statement.setString(1, topicName);
            statement.setString(2, description);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                ResultSet keys = statement.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public boolean updateTopic(int topicID, String topicName, String description) {

        String sql = "UPDATE Topic SET topicName = ?, description = ? WHERE topicID = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, topicName);
            statement.setString(2, description);
            statement.setInt(3, topicID);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean deleteTopic(int topicID) {

        String sql = "DELETE FROM Topic WHERE topicID = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, topicID);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            return false;
        }
    }
}
