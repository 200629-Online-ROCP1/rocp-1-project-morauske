package com.revature.services;

import java.util.List;

import com.revature.models.Account;
import com.revature.models.AccountDTO;
import com.revature.repos.AccountDAO;
import com.revature.repos.IAccountDAO;

public class AccountService {
	private final IAccountDAO dao = new AccountDAO();


	public boolean addAccount(AccountDTO a) {
		System.out.println("AccountDTO: "+ a.ownerId);
		Boolean act = dao.addAccount(a);
		
		if (act) {
			// Successful login
			System.out.println("AccountDTO returning true OWNERid="+a.ownerId);
			return true;
		}else {
			// Login failure
			System.out.println("AccountDTO returning false");
			return false;
		}
	}
	public AccountDTO updateAccount(AccountDTO a) {
		System.out.println("Update AccountDTO: "+ a.ownerId);
		AccountDTO act = dao.updateAccount(a);
		
		if (act != null) {
			// Successful login
			System.out.println("Service update account successful OWNERid="+act.ownerId);
			return act;
		}else {
			// update failure
			System.out.println("Service update account failed to update");
			return null;
		}
	}
	public List<Account> findAll() {
		return dao.findAll();
	}
	public Account findById(int id) {

		return dao.findById(id);
	}

}
