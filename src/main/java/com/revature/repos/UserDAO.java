package com.revature.repos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Role;
import com.revature.models.User;
import com.revature.util.ConnectionUtil;

public class UserDAO implements IUserDAO {

	// public static final ILoginDAO dao = new LoginDAO();

	@Override
	public List<User> findAll() {
		try (Connection conn = ConnectionUtil.getConnection()) {
			// create DB select statement for users with roles added
			// No user can have a NULL role
			String sql = "SELECT * FROM users JOIN roles ON users.role_id_fk = roles.role_id;";

			Statement statement = conn.createStatement();

			List<User> list = new ArrayList<>();

			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				User usr = new User();
				usr.setUserId(result.getInt("user_id"));
				usr.setUsername(result.getString("username"));
				usr.setPassword(result.getString("passwrd"));
				usr.setFirstName(result.getString("first_name"));
				usr.setLastName(result.getString("last_name"));
				usr.setEmail(result.getString("email"));
				Role role = new Role(result.getInt("role_id_fk"), result.getString("role_name"));
				usr.setRole(role);

				list.add(usr);
			}

			return list;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public User findById(int id) {
		try (Connection conn = ConnectionUtil.getConnection()) {
			// create DB select statement for users with roles added
			// No user can have a NULL role
			String sql = "SELECT * FROM users JOIN roles ON users.role_id_fk = roles.role_id WHERE user_id = ?;";

			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);

			ResultSet result = statement.executeQuery();
			

			if (result.next()) {
				User usr = new User();
				usr.setUserId(result.getInt("user_id"));
				usr.setUsername(result.getString("username"));
				usr.setPassword(result.getString("passwrd"));
				usr.setFirstName(result.getString("first_name"));
				usr.setLastName(result.getString("last_name"));
				usr.setEmail(result.getString("email"));
				Role role = new Role(result.getInt("role_id_fk"), result.getString("role_name"));
				usr.setRole(role);
				return usr;
			} else {
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
