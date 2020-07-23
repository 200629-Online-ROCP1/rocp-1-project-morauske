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
import com.revature.models.UserDTO;
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

	@Override
	public User addUser(UserDTO u) {
		try (Connection conn = ConnectionUtil.getConnection()) {
			// create DB select statement for users with roles added
			// No user can have a NULL role
			System.out.println("addUser with role id:" + u.role.getRoleID());

			String sql = "SELECT * FROM users  JOIN roles ON users.role_id_fk = roles.role_id WHERE username = ? OR email = ? ;";

			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, u.username);
			statement.setString(2, u.email);

			System.out.println(statement);
			ResultSet result = statement.executeQuery();

			if (result.next()) {
				System.out.println("");
				return null;
			} else {
				// Should be good to add a new user into the database
				System.out.println("REGISTER::Nothing was found from the search.  Good to add.");
				sql = "INSERT INTO users (username, passwrd, first_name,last_name,email,role_id_fk) VALUES (?,?,?,?,?,?);";
				int index = 0;
				statement = conn.prepareStatement(sql);

				statement.setString(++index, u.username);
				statement.setString(++index, u.password);
				statement.setString(++index, u.firstName);
				statement.setString(++index, u.lastName);
				statement.setString(++index, u.email);
				statement.setInt(++index, u.role.getRoleID());

				System.out.println(statement);
				statement.execute();  //  there are no results.

				// findUserByUsername
				User usr = findUserByUsername(u.username);
				if (usr != null) {
					// We have a new user now
					return usr;
				} else {
					return null;  // doesn't look to be successful at creation of a new user
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public User findUserByUsername(String username) {
		try (Connection conn = ConnectionUtil.getConnection()) {
			// create DB select statement for users with roles added
			// No user can have a NULL role
			String sql = "SELECT * FROM users JOIN roles ON users.role_id_fk = roles.role_id WHERE username = ?;";

			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, username);

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
