package Controller;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.sql.SQLException;
import java.util.List;
import org.h2.util.json.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import DAO.MessageDAO;
import DAO.AccountDAO;
import Service.MessageService;
import Model.Message;
import Model.Account;
import Service.AccountService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    MessageService messageService;
    AccountService accountService;

    public SocialMediaController(){
        messageService = new MessageService();
        accountService = new AccountService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
        app.post("/register", this::register);//endpoint to register the user        
        app.post("/login", this::userLogin); //endpoint to login the user
        app.post("/messages", this::createMessage);//endpoint to post the message or create the message
        app.get("/messages", this::getAllMessages);  //endpoint to get the message   
        app.get("/messages/{message_id}", this::getMessageById); //endpoint to get all messages by message_id        
        app.delete("/messages/{message_id}", this::deleteMessageById);//DAO//enpoint to delete all the message by message_id
        app.patch("/messages/{message_id}", this::updateMessageById);//DAO//endpoint to update message
        app.get("/accounts/{account_id}/messages", this::getAllMessagesFromUser); //endpoint tp get all messages from user
        return app;
    }
    
    private void createMessage(Context context) {//endpoint to post the message or create the message
        Message message = context.bodyAsClass(Message.class);
            if (message != null) {
                Message createdMessage = messageService.createMessage(message);
                if (createdMessage != null) {
                    context.json(createdMessage);
                    context.status(200);
                } else {
                    context.status(400);
                }
            } else {
                context.status(400);
            }
        }  

    private void deleteMessageById(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message messagetodelete = messageService.getMessageById(messageId);
            if (messagetodelete != null) {
                context.json(messagetodelete); // Return the deleted message
                messageService.deleteMessageById(messageId);
                context.status(200); // Success
             } else {
                context.json("");
                context.status(200); // Success, if the response body is empty
            }
         } 

    private void getAllMessagesFromUser(Context context){
        int accountId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesFromUser(accountId);
        context.json(messages);
        context.status(200);
    }

    private void getAllMessages(Context context) throws SQLException{
        List<Message> messages = messageService.getAllMessages();
            if(messages != null){
                context.json(messages);
                context.status(200);
              }else{
                context.status(200);
              }
    }

    private void getMessageById(Context context){
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        ObjectMapper mapper = new ObjectMapper();
        try {
            if(message != null){
            context.json(mapper.writeValueAsString(message));
            context.status(200);
        }else{
            context.status(200);
        }  
     } catch (JsonProcessingException e) {
        e.printStackTrace();
     }
    }
    //endpoint to update the message with given message_id
    private void updateMessageById(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessageById(message_id, message);
        if (updatedMessage != null&&!message.getMessage_text().isBlank() && message.getMessage_text().length() < 255) { //it checked the updated message is not null, not empty, and not longer than 255 character
            context.status(200); //successful
            context.json(mapper.writeValueAsString(updatedMessage));//return with message updated
        } else if(message.getMessage_text().isBlank() && message.getMessage_text().length() > 255) {
            context.status(400);
        }else{
            context.status(400);
        }
    }  

    private void userLogin(Context context){
        Account account = context.bodyAsClass(Account.class);
        if (account != null) {
            Account loggedInAccount = accountService.userLogin(account);
            if (loggedInAccount != null) {
                context.json(loggedInAccount);
                context.status(200);
            } else {
                context.status(401);
            }
        } else {
            context.status(401);
        }
    }

    private void register(Context context) {
        Account account = context.bodyAsClass(Account.class);
        if (account != null) {
            Account registeredAccount = accountService.registerAccount(account);
            if (registeredAccount != null) {
                context.json(registeredAccount);
                context.status(200);
            } else {
                context.status(400);
            }
        } else {
            context.status(400);
        }
    }
}

