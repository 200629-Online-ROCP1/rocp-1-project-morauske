package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.LoginDTO;
import com.revature.models.User;
import com.revature.services.LoginService;

public class LoginController {

	private static final LoginService ls = new LoginService();
	private static final ObjectMapper om = new ObjectMapper();
	
	public void login(HttpServletRequest req, HttpServletResponse res) throws IOException {

		BufferedReader reader = req.getReader();
		StringBuilder       s = new StringBuilder();
		String           line = reader.readLine();
		
		while(line != null) {
			s.append(line);
			System.out.println("login req.getReader Info: "+line);
			line=reader.readLine();
		}
		
		String body = new String(s);
		System.out.println("login body: "+body);
		
		LoginDTO l = om.readValue(body, LoginDTO.class);
		
		// ls.login() will reach out the the DBase to verify username/password
		User usr = ls.login(l);
		// check password
		if(usr != null) {
			//Note - the no args getSession method will create a new session if one does not already exist for this client. 
			System.out.println("LoginController SESSION: setting user, loggedin and userType");
			if (usr.getPassword().equals(l.password)) {
			HttpSession ses = req.getSession();

			ses.setAttribute("user", l);
			ses.setAttribute("userID", usr.getUserId());
			ses.setAttribute("loggedin", true);
			ses.setAttribute("userType", usr.getRole().getRole());  // Getting the string form for Role eg. Admin,

			res.setStatus(200);
			res.getWriter().println(l.username + " Login Successful!");
			}
			else {
				res.setStatus(400);
				res.getWriter().println("Invalid Credentials");
				
			}
		} else {
			//will only return a session if one is already associated with the request, will not create a new one. 
			System.out.println("LoginController else invalidate session");
			HttpSession ses = req.getSession(false);
			if(ses!=null) {
				//This will throw out the session, the client will no longer have a session associated with their cookie. 
				ses.invalidate();
			}
			res.setStatus(400);
			res.getWriter().println("Invalid Credentials");
		} // TODO if a GET method was used with login what should the error be?

	}

	public void logout(HttpServletRequest req, HttpServletResponse res) throws IOException {
		HttpSession ses = req.getSession(false);
		
		if(ses!=null) {
			System.out.println("logout SESSION user     = "+((LoginDTO) ses.getAttribute("user")).username);
			System.out.println("logout SESSION userID   = "+ses.getAttribute("userID"));
			System.out.println("logout SESSION loggedin = "+ses.getAttribute("loggedin"));
			System.out.println("logout SESSION userType = "+ses.getAttribute("userType"));
			LoginDTO l = (LoginDTO) ses.getAttribute("user");
			System.out.println("logout and invalidate session" + l.username);
			ses.invalidate();
			res.setStatus(200);
			res.getWriter().println(l.username+" you logged out.");
		} else {
			System.out.println("logout You must be logged in to logout");
			res.setStatus(400);
			res.getWriter().println("You must be logged in to log out.");
		}
		
	}

}
