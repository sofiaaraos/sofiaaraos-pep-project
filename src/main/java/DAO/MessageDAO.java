package DAO;

import Util.ConnectionUtil;
import Model.Message;
//import Model.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

public class MessageDAO {
	

	//The method below retrieves all messages
	public List<Message> getAllMessages(){
   		Connection connection = ConnectionUtil.getConnection();
		List<Message> messagesList = new ArrayList<>();

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{
		    //SQL LOGIC
		    String sql = "SELECT * FROM message";
		    preparedStatement = connection.prepareStatement(sql);
		    rs = preparedStatement.executeQuery();
		    //Places messageId and messageText into a message obj and then adds into list
		    while(rs.next()){
			Message message = new Message(rs.getInt("message_id"),
							rs.getInt("posted_by"), 
							rs.getString("message_text"), 
							rs.getLong("time_posted_epoch") );
			messagesList.add(message);
		    }
	  }catch(SQLException e){
		System.out.println(e.getMessage());
	  }finally{
	  	if(rs != null){
			try{
			   rs.close();
			}catch(SQLException e){
			   System.out.println("Result Set will not close" + e.getMessage());
			}
		}
		if(preparedStatement != null){
			try{
			   preparedStatement.close();
			}catch(SQLException e){
			   System.out.println("PreparedStatement will not close" + e.getMessage());
			}
		}

	  }
	   return messagesList;
       }

	//The method below retrieves all messages by MessageId
	public Message getAllMessagesById(int messageId){
   		Connection connection = ConnectionUtil.getConnection();
		Message message = null;

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{
		    //SQL LOGIC
		    String sql = "SELECT * FROM message WHERE message_id = ?";
		    preparedStatement = connection.prepareStatement(sql);
		    //Prepared Statement
		    preparedStatement.setInt(1,messageId);
		    rs = preparedStatement.executeQuery();
		    //Places messageId and messageText into a message obj and then adds into list
		    if(rs.next()){
			message = new Message(rs.getInt("message_id"),
							rs.getInt("posted_by"), 
							rs.getString("message_text"), 
							rs.getLong("time_posted_epoch") );
		    }
	  }catch(SQLException e){
		System.out.println(e.getMessage());
	  }finally{
	  	if(rs != null){
			try{
			   rs.close();
			}catch(SQLException e){
			   System.out.println("Result Set will not close" + e.getMessage());
			}
		}
		if(preparedStatement != null){
			try{
			   preparedStatement.close();
			}catch(SQLException e){
			   System.out.println("PreparedStatement will not close" + e.getMessage());
			}
		}

	  }
	   return message;
       }
	//The method below retrieves all messages by AccountId 
	public List<Message> getAllMessagesByAccountId(int account_id){
   		Connection connection = ConnectionUtil.getConnection();
		List <Message> messagesList = new ArrayList<>();

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{
		    //SQL LOGIC
		    String sql = "SELECT * FROM message WHERE posted_by = ?";
		    preparedStatement = connection.prepareStatement(sql);
		    //Prepared Statement
		    preparedStatement.setInt(1,account_id);
		    rs = preparedStatement.executeQuery();
		    
		    //Places messageId and messageText into a message obj and then adds into list
		    while(rs.next()){
			Message message = new Message(rs.getInt("message_id"),
							rs.getInt("posted_by"), 
							rs.getString("message_text"), 
							rs.getLong("time_posted_epoch") );
			messagesList.add(message);
		    }
	  }catch(SQLException e){
		System.out.println(e.getMessage());
	  }finally{
	  	if(rs != null){
			try{
			   rs.close();
			}catch(SQLException e){
			   System.out.println("Result Set will not close" + e.getMessage());
			}
		}
		if(preparedStatement != null){
			try{
			   preparedStatement.close();
			}catch(SQLException e){
			   System.out.println("PreparedStatement will not close" + e.getMessage());
			}
		}

	  }
	   return messagesList;
    }

	//This method creates a new message
	public Message insertMessage(Message message){
		Connection connection = ConnectionUtil.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;
		
		try{
			//SQL
			String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES(?,?,?)";
			preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			//Prepare statements
			preparedStatement.setInt(1, message.getPosted_by());
			preparedStatement.setString(2, message.getMessage_text());
			preparedStatement.setLong(3, message.getTime_posted_epoch());

			//Execute Update
			int rowsAffected = preparedStatement.executeUpdate();
			if(rowsAffected == 0){
				return null;
			}

			
			generatedKeys = preparedStatement.getGeneratedKeys();
			if(generatedKeys.next()){
				int generatedId = generatedKeys.getInt(1);
				message.setMessage_id(generatedId);
			}
			
			//Return newly created message
			return message;

		}catch(SQLException e){
			System.out.println("Cannot update: " + e.getMessage());
		}finally{
			if(preparedStatement != null){
				try{
					preparedStatement.close();
				}catch(SQLException e){
					System.out.println("Prepared statement will not close: " + e.getMessage());
				}

			}
			if(generatedKeys != null){
				try{
					generatedKeys.close();
				}catch(SQLException e){
					System.out.println("Generated Keys will not close:" + e.getMessage());
				}
			}
		}
		return null;
	}

	//This method delete a message by the message Id

	public Message deleteMessageByMessageId(int messageId){
		Connection connection = ConnectionUtil.getConnection();
		PreparedStatement preparedStatement = null;

		//This invokes the getAllMessagesById method with the param messageId and places it inside an obj var  
		Message messageToDelete = getAllMessagesById(messageId);

		//Checks to see if there is a message to delete
		if(messageToDelete == null){
			return null;
		}

		try{
			//SQL LOGIC
			String sql = "DELETE FROM message WHERE message_id = ?";
			preparedStatement = connection.prepareStatement(sql);

			//Written prepared statement
			preparedStatement.setInt(1, messageId);

			//Update db
			preparedStatement.executeUpdate();
		
			//Return deleted message
			return messageToDelete;

		}catch(SQLException e){
			System.out.println("Cannot Delete: " + e.getMessage());
		}finally{
			if(preparedStatement != null){
				try{
					preparedStatement.close();
				}catch(SQLException e){
					System.out.println("Prepared Statement cannot close: " + e.getMessage());
				}
			}
		}
		return null;

	}

	//This method UpdateMessageText
	public Message updatedMessage(int messageId, Message message){
		Connection connection = ConnectionUtil.getConnection();
		PreparedStatement preparedStatement = null;
		

		Message messageToUpdate = getAllMessagesById(messageId);

		//If statement to see if messageId exist
		if(messageToUpdate == null){
			return null;
		}

		if(message == null || message.getMessage_text().length() > 255 || message.getMessage_text().isEmpty()){
			return null;
		}

		try{
			//SQL LOGIC
			String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
			preparedStatement = connection.prepareStatement(sql);

			//Written preparedStatement
			preparedStatement.setString(1,message.getMessage_text());
			preparedStatement.setInt(2,messageId);

			//returns number of rows affected by update
			int rowsUpdated = preparedStatement.executeUpdate();

			if(rowsUpdated > 0 ){
				return getAllMessagesById(messageId);
			}else{
				return null;
			}
				
		}catch(SQLException e){
			System.out.println("Cannot update : " + e.getMessage());
			return null;
		}finally{
			if(preparedStatement != null){
				try{
					preparedStatement.close();
				}catch(SQLException e){
					System.out.println("PreparedStatement cannot close: " + e.getMessage());
					}
				}

			}			
		}
	}





