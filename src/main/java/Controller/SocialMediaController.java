package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.*;
import Service.AccountService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;

    public SocialMediaController() {
        this.accountService = new AccountService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::register);
        

        return app;
    }

    /**
     * Register account endpoint
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void register(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        boolean invalid = false;
        if (account.getUsername() == "" || account.getPassword().length() < 4) {
            invalid = true;
        } else {
            Account addedAccount = accountService.addAccount(account);
            if(addedAccount!=null){
                context.json(mapper.writeValueAsString(addedAccount));
                context.status(200);
            }else{
                context.status(400);
            }
        }
        if (invalid) {
            context.status(400);
        }
    }

}