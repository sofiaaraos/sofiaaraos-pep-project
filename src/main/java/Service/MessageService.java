package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;


public class MessageService{
	public MessageDAO messageDAO;
	
	public MessageService(){
		messageDAO = new MessageDAO();
	}	
	
	public MessageService(MessageDAO messageDAO){
		this.messageDAO = messageDAO;
	}

	//This method retrieves all messages
	public List<Message> allMessages(){
		return messageDAO.getAllMessages();
	}

	//This method retrieve all messages by messageID
	public Message allMessagesById(int messageId){
		return messageDAO.getAllMessagesById(messageId);
	}

	//this method retrieves all messages by AccountID
	public List<Message> allMessagesByAccountId(int account_id){
		return messageDAO.getAllMessagesByAccountId(account_id);
	}

	//This method creates a new message
	public Message newMessage(Message message){
		if (message.getMessage_text() == null || 
		    message.getMessage_text().isEmpty() || 
		    message.getMessage_text().length() > 255 || 
		     message.getPosted_by() <= 0 ) {
			return null;
		}else{
			return messageDAO.insertMessage(message);
		}
	}

	//This method deletes message by message id
	public Message deleteMessageById(int messageId){
		return messageDAO.deleteMessageByMessageId(messageId);
	}

	//This method updates a message
	public Message updateMessage(int messageId, Message message){
		return messageDAO.updatedMessage(messageId, message);
	}

}