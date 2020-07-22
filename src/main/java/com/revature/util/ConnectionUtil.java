package com.revature.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

	public static Connection getConnection() throws SQLException {

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		String url = "jdbc:postgresql://localhost:5432/simplebanking"; // avengerdb is my data base, TimG used demo
		String username = "postgres"; // my username for my admin 
		String password = "postgres"; // my pw for my local DB

		return DriverManager.getConnection(url, username, password);

	}}
