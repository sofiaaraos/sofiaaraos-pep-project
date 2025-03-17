package Service;

import Model.Account;
import DAO.AccountDAO;



public class AccountService{
	private AccountDAO accountDAO;
	
	public AccountService(){
		accountDAO = new AccountDAO();
	}
	
	public AccountService(AccountDAO accountDAO){
		this.accountDAO = accountDAO;
	}

	//This method uses accountDAO method that creates a new user/new account
	public Account newUserAccount (Account account){
		return accountDAO.newAccount(account);
	}

	//This method uses accountDAO to let user log in to account
	public Account accountAccess(Account account){
		return accountDAO.logInAccount(account);
	}
	

}