package com.revature.repos;

import java.util.List;

import com.revature.models.Account;
import com.revature.models.AccountDTO;

public interface IAccountDAO {

	public boolean addAccount(AccountDTO a);

	public AccountDTO updateAccount(AccountDTO a);

	public List<Account> findAll();

	public Account findById(int id);
}
