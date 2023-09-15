package Service;
import java.sql.SQLException;
import java.util.List;

import DAO.MessageDAO;
//import DAO.AccountDAO;
import Model.Message;
//import Model.Account;

public class MessageService {
    MessageDAO messageDAO;
    public MessageService(){
        messageDAO = new MessageDAO();
    }
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message createMessage(Message message) {
        if (isValidMessageCreated(message)) {
            return messageDAO.createMessage(message);
            }
            return null;
    }

    private boolean isValidMessageCreated(Message message) {
        return !message.getMessage_text().isBlank() &&
        message.getMessage_text().length() <= 254 &&
        isUserExists(message.getPosted_by());
    } 
    
    private boolean isUserExists(int message_id) {
        Message message = messageDAO.getMessageById(message_id);
        if(message!=null){
            int posted_by = message.getPosted_by();
            return messageDAO.isUserExists(posted_by);
        }
        return false;
    }  

    public boolean deleteMessageById(int message_id) {
        Message messagetodelete = messageDAO.getMessageById(message_id);
        if (messagetodelete != null) {
            boolean success = messageDAO.deleteMessageById(message_id);
            if (success) {
                return false;
            }
        }return true;
    }  

    public List<Message> getAllMessages() throws SQLException {
        return messageDAO.getAllMessages();//return the list of the all messages from message table
    }
    
    public Message getMessageById(int message_id) {
        return messageDAO.getMessageById(message_id);//return the message with given message_id
    }

    public List<Message> getAllMessagesFromUser(int accountId){ //return all messages created by the given user with posted_by(account_id)
        try {
            return messageDAO.getAllMessagesFromUser(accountId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Message updateMessageById(int message_id, Message message) {
        Message existMessage = messageDAO.getMessageById(message_id);
        if (existMessage != null) {
            if (!message.getMessage_text().isEmpty() && message.getMessage_text().length() <= 255) {
                existMessage.setMessage_text(message.getMessage_text());
                messageDAO.updateMessageById(message_id, existMessage);
                return existMessage;
            } else {
                System.out.println("Invalid message text");
            }
        } else {
            System.out.println("Message not found");
        }
        return existMessage;
    }
}
