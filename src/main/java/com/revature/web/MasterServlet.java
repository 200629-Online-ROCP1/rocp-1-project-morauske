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
					res.getWriter().println("Creating an account requires an existing session");
					return;
				}
				ac.addAccount(req, res);
				break;
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
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
			res.getWriter().println("httpReqGetHandler exception");
			res.setStatus(400);
		}

	}

}
