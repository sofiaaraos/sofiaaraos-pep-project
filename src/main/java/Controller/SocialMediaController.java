package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;
    private ObjectMapper objectMapper;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
	
	//Account endpoints
	app.post("/register", this::postNewAccountHandler);
	app.post("/login", this::postLogInAccountHandler);
	
	//Messages endpoints
	app.get("/messages", this::getAllMessagesHandler);
    app.get("/messages/{message_id}", this::allMessagesByIdHandler);
	app.post("/messages", this::postNewMessageHandler);
    app.delete("/messages/{message_id}", this::deleteMessageHandler);
	app.patch("/messages/{message_id}", this::updateMessageHandler);
    app.get("/accounts/{account_id}/messages", this::getMessagesByAccountIdHandler);

    return app;
    }



 // Defined handler methods

    // Handle new account registration
    private void postNewAccountHandler(Context ctx) {
        try{
		Account newAccount = objectMapper.readValue(ctx.body(), Account.class);
		Account createdAccount = accountService.newUserAccount(newAccount);
		
		if(createdAccount == null){
			ctx.status(400);
			ctx.result("");
		}else{
			ctx.status(200);
			ctx.json(createdAccount);
		}
	}catch(JsonProcessingException e){
		ctx.status(400);
		ctx.result("");
	}
    }

    // Handle user login
    private void postLogInAccountHandler(Context ctx) {
        try{
		Account credentialsAccount = objectMapper.readValue(ctx.body(), Account.class);
		Account logInAccount = accountService.accountAccess(credentialsAccount);

		if(logInAccount == null){
			ctx.status(401);
			ctx.result("");
		}else{
			ctx.status(200);
			ctx.json(logInAccount);
		}
	}catch(JsonProcessingException e){
			ctx.status(400);
			ctx.result("");		
	}
    }

    // Handle retrieving all messages
    private void getAllMessagesHandler(Context ctx) {
	List<Message> messages = messageService.allMessages();
	ctx.status(200);
	ctx.json(messages);
    }

    // Handle retrieving a message by its ID
    private void allMessagesByIdHandler(Context ctx) {
	//Extract the message_id from the path parameter and convert to int
	int messageId = Integer.parseInt(ctx.pathParam("message_id"));
	
	Message message = messageService.allMessagesById(messageId);
	
	ctx.status(200);

	if(message == null){
		ctx.result("");
	}else{
		ctx.json(message);
	}
    }

    // Handle creating a new message
    private void postNewMessageHandler(Context ctx) {
	try{
		Message newMessage = objectMapper.readValue(ctx.body(), Message.class);
		Message createdMessage = messageService.newMessage(newMessage);

		if(createdMessage == null){
			ctx.status(400);
			ctx.result("");
		}else{
			ctx.status(200);
			ctx.json(createdMessage);
		}
	}catch(JsonProcessingException e){
		ctx.status(400);
		ctx.result("");
	}
    }
    
    // Handle deleting a message by its ID
    private void deleteMessageHandler(Context ctx) {
 	int messageId = Integer.parseInt(ctx.pathParam("message_id"));

	Message deletedMessage = messageService.deleteMessageById(messageId);

	if(deletedMessage == null){
		ctx.status(200);
		ctx.result("");
	}else{
		ctx.status(200);
		ctx.json(deletedMessage);
	}
    }


    // Handle updating a message
    private void updateMessageHandler(Context ctx) {
        try {
            Message message = objectMapper.readValue(ctx.body(), Message.class);
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            //String newText = ctx.body();
    
            Message updatedMessage = messageService.updateMessage(messageId, message);
        
            if (updatedMessage == null){
                ctx.status(400);
                ctx.result("");
            }else{
                ctx.status(200);
                ctx.json(objectMapper.writeValueAsString(updatedMessage));
            }

        }catch(JsonProcessingException e){
            ctx.status(400);
            ctx.result("");
        }

    }

    // Handle retrieving messages by account ID
    private void getMessagesByAccountIdHandler(Context ctx) {
		int account_id = Integer.parseInt(ctx.pathParam("account_id"));
		
		List <Message> messages = messageService.allMessagesByAccountId(account_id);

		ctx.status(200);

		if(messages == null){
			ctx.result("");
		}else{
			ctx.json(messages);
		}

    }
}