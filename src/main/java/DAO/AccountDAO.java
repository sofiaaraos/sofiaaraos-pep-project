package DAO;

import Util.ConnectionUtil;
//import  Model.Message;
import  Model.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
import java.sql.Statement;

public class AccountDAO {

	//This method creates a new user
	public Account newAccount(Account newAccount){
		Connection connection = ConnectionUtil.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;


		if(newAccount.getAccount_id() != 0 || newAccount.getUsername() == null ||  newAccount.getUsername().isEmpty() || 			   newAccount.getPassword() == null || newAccount.getPassword().length() < 4){
			return null;
		}
		try {
			//SQL LOGIC
			String sql = "INSERT INTO account (username, password) VALUES (?,?)";
			preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			//PreparedStatements
			preparedStatement.setString(1, newAccount.getUsername());
			preparedStatement.setString(2, newAccount.getPassword());

			//execute
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected == 0){
				return null;
			}
		
			generatedKeys = preparedStatement.getGeneratedKeys();
			if(generatedKeys.next()){
				newAccount.setAccount_id(generatedKeys.getInt(1));
			}
			return newAccount;
		}catch(SQLException e){
			System.out.println("Cannot create new Account: " + e.getMessage());
			return null;
		}finally{
			if (generatedKeys != null) {
				try {
					generatedKeys.close();
				} catch(SQLException e) {
					System.out.println("Generated keys could not be closed: " + e.getMessage());
				}
			}
			if(preparedStatement != null){
				try{
					preparedStatement.close();

				}catch(SQLException e){
					System.out.println("Cannot close prepared statement: " + e.getMessage());
				}
			}

		}
 

	}


	//This method checks if username and password matches in the db and lets users log in
	public Account logInAccount(Account account){
		Connection connection = ConnectionUtil.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try{
			//SQL LOGIC
			String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, account.getUsername());
			preparedStatement.setString(2, account.getPassword());

			rs = preparedStatement.executeQuery();
			
			if(rs.next()){
				account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));				
                return account;				
			}
		}catch(SQLException e){
			System.out.println("Cannot log in: " + e.getMessage());
			return null;
		}finally{
			if(preparedStatement != null){
				try{
					preparedStatement.close();
				}catch(SQLException e){
					System.out.println("Cannot close preparedStatement: " + e.getMessage());
				}
			}
			if(rs != null){
				try{
					rs.close();
				}catch(SQLException e){
					System.out.println("Cannot close Result Set: " + e.getMessage());
				}
			}
		}
		//Return null if no matching account was found
		return null;

	}



}