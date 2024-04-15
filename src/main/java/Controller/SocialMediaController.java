package Controller;

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
}