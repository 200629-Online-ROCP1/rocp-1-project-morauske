package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.AccountDTO;
import com.revature.services.AccountService;

public class AccountController {

	private static final AccountService as = new AccountService();
	private static final ObjectMapper om = new ObjectMapper();

	public void addAccount(HttpServletRequest req, HttpServletResponse res) throws IOException {
		BufferedReader reader = req.getReader();
		StringBuilder s = new StringBuilder();
		String line = reader.readLine();

		while (line != null) {
			s.append(line);
			System.out.println("AcctCntrlr req.getReader Info: " + line);
			line = reader.readLine();
		}

		String body = new String(s);
		System.out.println("account body: " + body);

		AccountDTO u = om.readValue(body, AccountDTO.class);

		HttpSession ses = req.getSession(false); // false - doesn't create session if it doesn't already exist

		String userType = (String) ses.getAttribute("userType");
		// if (userType != "Admin" && userType != "Employee") {
		if (!userType.equals("Admin") && !userType.equals("Employee")) {
			// Some field cannot be setup by just anyone
			System.out.println("AcctController:: Not ADMIN or Employee");
			u.ownerId = (int) ses.getAttribute("userID");
			u.balance = (float) 0.0;
			u.status = "Pending";
		}

		if (as.addAccount(u)) {
			// new user
			res.setStatus(201);
			res.getWriter().println("New account has been created");
		} else {
			// failed to create new user
			res.setStatus(400);
			res.getWriter().println("Invalid fields");
		}

		// ls.login() will reach out the the DBase to verify username/password
		// User usr = ls.login(l);

	}

	public AccountDTO update(HttpServletRequest req, HttpServletResponse res) throws IOException {
		BufferedReader reader = req.getReader();
		StringBuilder s = new StringBuilder();
		String line = reader.readLine();

		while (line != null) {
			s.append(line);
			System.out.println("AcctCntrlr req.getReader Info: " + line);
			line = reader.readLine();
		}

		String body = new String(s);
		System.out.println("account body: " + body);

		AccountDTO a = om.readValue(body, AccountDTO.class);

		HttpSession ses = req.getSession(false); // false - doesn't create session if it doesn't already exist

		AccountDTO act = as.updateAccount(a);

		if (act != null) {
			// new user
			return act;
		} else {
			// failed to create new user
			return null;
		}
	}

}
