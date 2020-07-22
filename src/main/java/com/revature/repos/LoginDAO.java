package com.revature.repos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.revature.models.LoginDTO;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.util.ConnectionUtil;

public class LoginDAO implements ILoginDAO {
	

	@Override
	public User findUserByUsername(LoginDTO loginUser) {
		// TODO Auto-generated method stub
		User usr = new User();
		System.out.println("LoginDAO findUserByUsername:");
		
		try(Connection conn = ConnectionUtil.getConnection()) {
			
			String sql = "SELECT * FROM users JOIN roles ON users.role_id_fk = roles.role_id WHERE username = '"+loginUser.username+"';";
			System.out.println("LoginDAO sql: ["+ sql+"]");
			Statement statement = conn.createStatement();
			ResultSet result =  statement.executeQuery(sql);
			if(result.next()) {
				usr.setUserId(result.getInt("user_id"));
				usr.setUsername(result.getString("username"));
				usr.setPassword(result.getString("passwrd"));
				usr.setFirstName(result.getString("first_name"));
				usr.setLastName(result.getString("last_name"));
				usr.setEmail(result.getString("email"));
				Role r = new Role(result.getInt("role_id"),result.getString("role_name"));
				usr.setRole(r);
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
