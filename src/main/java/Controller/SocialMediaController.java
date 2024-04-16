package Controller;

import java.util.List;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.*;
import Service.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::register);
        app.post("/login", this::login);
        app.post("/messages", this::createMessage);

        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.get("/accounts/{account_id}/messages", this::getMessageByUser);

        app.delete("/messages/{message_id}", this::deleteMessageById);

        app.patch("/messages/{message_id}", this::updateMessageById);

        return app;
    }

    /**
     * Register account endpoint
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void register(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        if (account.getUsername().equals("") || account.getPassword().length() < 4) {
            context.status(400);
        } else {
            Account addedAccount = accountService.addAccount(account);
            if(addedAccount!=null){
                context.json(mapper.writeValueAsString(addedAccount));
                context.status(200);
            }else{
                context.status(400);
            }
        }
    }

    /**
     * Login endpoint
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void login(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account verifiedAccount = accountService.login(account);
        if (verifiedAccount != null) {
            context.json(mapper.writeValueAsString(verifiedAccount));
            context.status(200);
        } else {
            context.status(401);
        }
    }

    /**
     * New message endpoint
     * @param context
     * @throws JsonProcessingException
     */
    private void createMessage(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        
        if (message.getMessage_text().equals("") || message.getMessage_text().length() > 255) {
            context.status(400);
        } else {
            Message addedMessage = messageService.addMessage(message);
            if(addedMessage!=null){
                context.json(mapper.writeValueAsString(addedMessage));
                context.status(200);
            }else{
                context.status(400);
            }
        }
    }

    /**
     * Get all message endpoint
     * @param context
     * @throws JsonProcessingException
     */
    private void getAllMessages(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        List<Message> messages = messageService.getAllMessages();
        context.json(mapper.writeValueAsString(messages));
        context.status(200);
    }

    /**
     * Get specific message by message id endpoint
     * @param context
     * @throws JsonProcessingException
     */
    private void getMessageById(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        int message_id = Integer.valueOf(context.pathParam("message_id"));
        Message message = messageService.getMessageById(message_id);
        if (message != null) {
            context.json(mapper.writeValueAsString(message));
        }
        context.status(200);
    }

    /**
     * Delete specific message by message id endpoint.
     * @param context
     * @throws JsonProcessingException
     */
    private void deleteMessageById(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        int message_id = Integer.valueOf(context.pathParam("message_id"));
        Message message = messageService.deleteMessageById(message_id);
        if (message != null) {
            context.json(mapper.writeValueAsString(message));
        } 
        context.status(200);
    }

    /**
     *  Update message text endpoint.
     * @param context
     * @throws JsonProcessingException
     */
    private void updateMessageById(Context context) throws JsonProcessingException {
        if (context.body() == null || context.body().isEmpty()) {
            context.status(400);
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);

        int message_id = Integer.valueOf(context.pathParam("message_id"));
        if (message.getMessage_text().equals("") || message.getMessage_text().length() > 255) {
            context.status(400);
        } else {
            Message newMessage = messageService.updateMessageById(message_id, message);
            if (newMessage == null) {
                context.status(400);
            } else {
                context.json(mapper.writeValueAsString(newMessage));
                context.status(200);
            }
        }
    }

    /**
     * Get all messages by an user endpoint.
     * @param context
     * @throws JsonProcessingException
     */
    private void getMessageByUser(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        int account_id = Integer.valueOf(context.pathParam("account_id"));

        List<Message> messages = messageService.getAllMessagesByAccount(account_id);

        context.json(mapper.writeValueAsString(messages));
        context.status(200);
    }
}