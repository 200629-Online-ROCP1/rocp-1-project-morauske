package com.revature.repos;

import com.revature.models.AccountDTO;

public interface IAccountDAO {

	public boolean addAccount(AccountDTO a);

	public AccountDTO updateAccount(AccountDTO a);
}
