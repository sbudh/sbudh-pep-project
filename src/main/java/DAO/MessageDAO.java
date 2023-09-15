package DAO;
import DAO.MessageDAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class MessageDAO {
    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            if (isUserExists(message.getPosted_by())) {
                String sql = "INSERT INTO message (message_text, posted_by) VALUES (?, ?);";
                PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, message.getMessage_text());
                ps.setInt(2, message.getPosted_by());
                int rowsInserted = ps.executeUpdate();
                if (rowsInserted > 0) {
                    ResultSet generatedKeys = ps.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        message.setMessage_id(generatedKeys.getInt(1));
                        return message; 
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
        }
        return null; 
    }    

    public boolean isUserExists(int posted_by) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT COUNT(*) FROM message WHERE message_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, posted_by);//posted_by give the account id of the user
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; //if find any message with the given message_id than return that message
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());  
        }
        return false; 
    }

    public boolean deleteMessageById(int message_id) {
        try {
            Message message = getMessageById(message_id);
            if (message != null) {
                Connection connection = ConnectionUtil.getConnection();
                String sql = "DELETE FROM MESSAGE WHERE message_id = ?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, message_id);
                int rowsDeleted = ps.executeUpdate();
                if (rowsDeleted != 0) {
                    return true;
                }
             }
             } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }
            
    public List<Message> getAllMessagesFromUser(int accountId) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String selectQuery = "SELECT * FROM Message WHERE posted_by = ?";
        PreparedStatement ps = connection.prepareStatement(selectQuery);
        ps.setInt(1, accountId);
        ResultSet rs = ps.executeQuery();
        List<Message> messages = new ArrayList<>();
        while (rs.next()) {
            Message message = new Message();
            message.setMessage_id(rs.getInt("message_id"));
            message.setMessage_text(rs.getString("message_text"));
            message.setPosted_by(rs.getInt("posted_by"));
            message.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
            messages.add(message);
        }

        return messages;
    }

    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM MESSAGE;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message message = new Message();
                message.setMessage_id(rs.getInt("message_id"));
                message.setMessage_text(rs.getString("message_text"));
                message.setPosted_by(rs.getInt("posted_by"));
                message.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
            return messages;
        }

    public Message getMessageById(int message_id){
        try {Connection connection = ConnectionUtil.getConnection();
            String selectQuery = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(selectQuery);
            ps.setInt(1, message_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message message = new Message();
                message.setMessage_id(rs.getInt("message_id"));
                message.setMessage_text(rs.getString("message_text"));
                message.setPosted_by(rs.getInt("posted_by"));
                message.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
                return message;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    } 

    public void updateMessageById(int message_id, Message message){
        try {
            Message mess = getMessageById(message_id);
            if (mess != null) {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "UPDATE message SET message_text = ? WHERE message_id =?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,message.getMessage_text());
            ps.setInt(2,message_id);
            ps.executeUpdate();
        }
    }catch(SQLException e){  
            e.printStackTrace();
        }
    }
}



