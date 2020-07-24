package com.revature.repos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Account;
import com.revature.models.AccountDTO;
import com.revature.util.ConnectionUtil;

public class AccountDAO implements IAccountDAO {

	@Override
	public boolean addAccount(AccountDTO a) {
		// First check to see if AccountDTO already in use
		// AccountDTO chk = new AccountDTO();
		// make sure that the ownerId is a valid owner
		UserDAO usr = new UserDAO();
		if ((findAcctByOwnerId(a.ownerId) != null) || (usr.findById(a.ownerId) == null)) {
			System.out.println("Only 1 account per userId currently");
			return false;
		}

		try (Connection conn = ConnectionUtil.getConnection()) {

			int index = 0;

			String sql = "INSERT INTO accounts(owner_user_id, balance, status, acct_type)" + " VALUES(?,?,?,?);";

			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(++index, a.ownerId);
			statement.setLong(++index, (long) a.balance);
			statement.setString(++index, a.status);
			statement.setString(++index, a.acctType);

			statement.execute();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public AccountDTO findAcctByOwnerId(int ownerID) {
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM accounts WHERE owner_user_id = " + ownerID + ";";

			Statement statement = conn.createStatement();

			ResultSet result = statement.executeQuery(sql);

			if (result.next()) {
				AccountDTO a = new AccountDTO();
				a.accountId = result.getInt("account_id");
				a.ownerId = result.getInt("owner_user_id");
				a.balance = result.getDouble("balance");
				a.status = result.getString("status");
				a.acctType = result.getString("acct_type");

				return a;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public AccountDTO updateAccount(AccountDTO a) {
		// TODO even though the Admin can there should be some checks
		// to make sure that the admin doesn't change some fields
		// like owner_user_id.
		System.out.println("updating account information ####");
		try (Connection conn = ConnectionUtil.getConnection()) {

			int index = 0;

			String sql = "UPDATE accounts set balance = ?, status = ?, acct_type = ? where owner_user_id = ?;";

			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setDouble(++index, a.balance);
			statement.setString(++index, a.status);
			statement.setString(++index, a.acctType);
			statement.setInt(++index, a.ownerId);

			statement.execute();

			AccountDTO adto = findAcctByOwnerId(a.ownerId);
			if (adto != null) {
				return adto;
			} else {
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Account> findAll() {
		try (Connection conn = ConnectionUtil.getConnection()) {
			// create DB select statement for users with roles added
			// No user can have a NULL role
			String sql = "SELECT * FROM accounts JOIN users ON accounts.owner_user_id = users.user_id;";

			Statement statement = conn.createStatement();

			List<Account> list = new ArrayList<>();

			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				Account act = new Account();
				act.setAccountId(result.getInt("account_id"));
				act.setOwnerUserId(result.getInt("owner_user_id"));
				act.setBalance(result.getDouble("balance"));
				act.setStatus(result.getString("status"));
				act.setType(result.getString("acct_type"));

				list.add(act);
			}

			return list;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
