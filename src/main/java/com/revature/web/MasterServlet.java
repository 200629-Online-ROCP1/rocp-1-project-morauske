package com.revature.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.controllers.AccountController;
import com.revature.controllers.LoginController;
import com.revature.controllers.UserController;
import com.revature.models.Account;
import com.revature.models.AccountDTO;
import com.revature.models.User;

public class MasterServlet extends HttpServlet {

	private static final ObjectMapper om = new ObjectMapper();
	private static final LoginController lc = new LoginController();
	private static final UserController uc = new UserController();
	private static final AccountController ac = new AccountController();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// super.doGet(req, res); // If this line is not commented out then the
		// following error occurs
		// java.lang.IllegalStateException: Cannot create a session after the response
		// has been committed
		System.out.println("doGet: Method requested was " + req.getMethod());
		httpReqGetHandler(req, res);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// super.doPost(req, res);// If this line is not commented out then the
		// following error occurs
		// java.lang.IllegalStateException: Cannot create a session after the response
		// has been committed
		System.out.println("doPost: Method requested was " + req.getMethod());
		res.setContentType("application/json");
		// this will set the default response to not found; we will change it later if
		// the request was successful
		res.setStatus(404);

		Boolean loggedIn = false; // default to not logged in
		int usrID = 0; // default to no real user

		String userType = ""; // Default to nothing

		HttpSession ses = req.getSession(false); // false - doesn't create session if it doesn't already exist
		if (ses != null) {
			loggedIn = (Boolean) ses.getAttribute("loggedin");
			System.out.println("SESSION Att Names:" + ses.getAttributeNames());
			System.out.println("current usrID " + ses.getAttribute("userID"));
			usrID = (int) ses.getAttribute("userID");
			userType = (String) ses.getAttribute("userType");
			System.out.println("userType" + userType);

		}

		System.out.println(req.getRequestURI());

		final String URI = req.getRequestURI().replace("/rocp-project/", "");

		String[] portions = URI.split("/");

		System.out.println(Arrays.toString(portions));
		try {
			switch (portions[0]) {
			case "login":
				lc.login(req, res);
				break;

			case "logout":
				lc.logout(req, res);
				break;

			case "register":
				// if (!loggedIn ) {
				if (!loggedIn || !(userType.equals("Admin"))) {
					System.out.println("MS register");
					res.setStatus(400);
					res.getWriter().println("Register requires an Admin to be logged into the session");
					return;
				}
				uc.register(req, res);
				// Status code for pass or fail in UserController
				break;
			case "accounts":
				if (!loggedIn) {
					res.setStatus(400);
					res.getWriter().println("Request requires an existing session");
					return;
				}
				if (portions.length == 1) {
					ac.addAccount(req, res);
					break;
				} else if ((portions.length == 2))  {
					// Need to add some checking
					switch (portions[1]) {
					case "deposit":
						ac.deposit(req, res);
						break;
					case "withdraw":
						ac.withdraw(req, res);
						break;
					case "transfer":
						ac.transfer(req, res);
						break;
						
					}
				}
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
			res.getWriter().println("httpReqGetHandler exception");
			res.setStatus(400);
		}

	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// super.doPut(req, res);// If this line is not commented out then the following
		// error occurs
		// java.lang.IllegalStateException: Cannot create a session after the response
		// has been committed

		System.out.println("doPut: Method requested was " + req.getMethod());
		res.setContentType("application/json");
		// this will set the default response to not found; we will change it later if
		// the request was successful
		res.setStatus(404);

		Boolean loggedIn = false; // default to not logged in
		int usrID = 0; // default to no real user

		String userType = ""; // Default to nothing

		HttpSession ses = req.getSession(false); // false - doesn't create session if it doesn't already exist
		if (ses != null) {
			loggedIn = (Boolean) ses.getAttribute("loggedin");
			System.out.println("current usrID " + ses.getAttribute("userID"));
			usrID = (int) ses.getAttribute("userID");
			userType = (String) ses.getAttribute("userType");

		} else {
			// All PUT request require a valid login
			res.setStatus(400);
			res.getWriter().println("There was no user logged into the session");
			return;
		}
		System.out.println(req.getRequestURI());

		final String URI = req.getRequestURI().replace("/rocp-project/", "");

		String[] portions = URI.split("/");

		System.out.println(Arrays.toString(portions));
		try {
			switch (portions[0]) {
			case "accounts":
				if (portions.length == 1) {
					if (ses.getAttribute("userType").equals("Admin")) {
						// '/users' has a length of 1
						AccountDTO acct = ac.update(req, res);
						if (acct != null) {

							res.setStatus(200);
							res.getWriter().println(om.writeValueAsString(acct));
						} else {
							res.setStatus(400);
							res.getWriter().println("Invalid fields");
						}
						return;
					} else {
						res.setStatus(401);
						res.getWriter().println("The requested action is not permitted");
						return;
					}
				} else {
					res.setStatus(401);
					res.getWriter().println("The requested action is not permitted: Extra on URI");
					return;

				}
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
			res.getWriter().println("httpReqGetHandler exception");
			res.setStatus(400);
		}

	}

	private void httpReqGetHandler(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		res.setContentType("application/json");
		// Not Found is the default response status. Until it is reset.
		res.setStatus(404);
		HttpSession ses = req.getSession(false); // false - doesn't create session if it doesn't already exist
		if (ses == null) {
			// All GET request require a valid login
			res.setStatus(400);
			res.getWriter().println("There was no user logged into the session");
			return;
		}
		System.out.println("SESSION usrID " + ses.getAttribute("userID"));
		int usrID = (int) ses.getAttribute("userID");

		// System.out.println(req.getRequestURI());
		final String URI = req.getRequestURI().replace("/rocp-project/", "");
		String[] portions = URI.split("/");
		System.out.println(Arrays.toString(portions));

		try {
			Boolean adminOrEmployee = ((ses.getAttribute("userType").equals("Admin"))
					|| (ses.getAttribute("userType").equals("Employee")));
			System.out.println("GET request adminOrEmployee:" + adminOrEmployee);
			switch (portions[0]) {
			case "users":
				if (portions.length == 1) { // Find All Users
					if (adminOrEmployee) {
						// '/users' has a length of 1
						List<User> all = uc.findAll(); // uc user controller

						res.setStatus(200);
						res.getWriter().println(om.writeValueAsString(all));
						return;
					} else {
						res.setStatus(401);
						res.getWriter().println("The requested action is not permitted");
						return;
					}
				} else { // Find Users by id
					if (portions.length == 2) {
						// '/users/#' where # is the user's ID
						// Restricted to Admin/Employee or user with id requested.
						if ((usrID == Integer.parseInt(portions[1])) || adminOrEmployee) {
							User usr = uc.findById(Integer.parseInt(portions[1]));
							if (usr != null) {

								res.setStatus(213);
								res.getWriter().println(om.writeValueAsString(usr));
							} else {
								res.setStatus(400);
								res.getWriter().println("Invalid field");
							}
							return;
						} else {
							res.setStatus(401);
							res.getWriter().println("The requested action is not permitted");
							return;
						}
					}
				}
				res.setStatus(401);
				res.getWriter().println("The requested action is not permitted");
				return;
			case "accounts": // accounts, accounts/:id, accounts/status/:statusId, accounts/owner/:ownerId
				if ((portions.length == 1) && (adminOrEmployee)) {
					// Find Accounts []
					List<Account> all = ac.findAll();

					res.setStatus(200);
					res.getWriter().println(om.writeValueAsString(all));
					return;
				} else if (portions.length == 2) {
					// accounts/:id
					// for accounts by account_id we get the account then check to see
					// if the permissions allow for it to be returned in the request.
					Account act = ac.findById(Integer.parseInt(portions[1]));

					if (act != null) {
						if (adminOrEmployee) {
							res.setStatus(200);
							res.getWriter().println(om.writeValueAsString(act));
							return;
						} else {
							if (usrID == act.getOwnerUserId()) {
								// Valid request with Account to return
								res.setStatus(200);
								res.getWriter().println(om.writeValueAsString(act));
								return;
							} else {
								// Request was not valid User requested account of another user
								res.setStatus(400);
								res.getWriter().println("Invalid fields");
								return;
							}
						}
					} else {
						// Requested account was not found
						res.setStatus(404);
						res.getWriter().println("Account Not Found");
					}

				} else if (portions.length == 3) {
					// checking for accounts status or owner request
					switch (portions[1]) {
					case "status":
						if (adminOrEmployee) {
							List<Account> act = ac.findAcctByStatusId(Integer.parseInt(portions[2]));
							res.setStatus(200);
							res.getWriter().println(om.writeValueAsString(act));
							return;
						}
						break;
					case "owner":
						if ((usrID == Integer.parseInt(portions[2])) || adminOrEmployee) {
							List<Account> act = ac.findAcctByUserId(Integer.parseInt(portions[2]));
							res.setStatus(200);
							res.getWriter().println(om.writeValueAsString(act));
							return;
						}
					}
					// action is not permitted.
					res.setStatus(401);
					res.getWriter().println("The requested action is not permitted");
					return;
				}
				System.out.println("Length of portions" + portions.length);
				break;
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
			res.getWriter().println("httpReqGetHandler exception");
			res.setStatus(400);
		}

	}

}
