package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.Account;
import com.revature.models.AccountDTO;
import com.revature.models.DepositDTO;
import com.revature.models.TransferDTO;
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

		AccountDTO act = as.updateAccount(a);

		if (act != null) {
			// new user
			return act;
		} else {
			// failed to create new user
			return null;
		}
	}

	public List<Account> findAll() {
		// TODO Auto-generated method stub
		return as.findAll();
	}

	public Account findById(int id) {

		return as.findById(id);
	}

	public List<Account> findAcctByUserId(int id) {
		return as.findAcctByUserId(id);
	}

	public List<Account> findAcctByStatusId(int id) {

		return as.findAcctByStatusId(id);
	}

	public void deposit(HttpServletRequest req, HttpServletResponse res) throws IOException {
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

		DepositDTO dto = om.readValue(body, DepositDTO.class);
		// Need to get requested account information to see what is allowed.
		Account act = as.findById(dto.accountId);

		HttpSession ses = req.getSession(false); // false - doesn't create session if it doesn't already exist
		// If account cannot be found, OR dto.amount<0 OR (Not Admin AND Not current
		// user acount) return error
		// TODO could add a check to act.getStatus() to find out if the account is OPEN or not.
		System.out.println("Account : Multi condition : amt<0");
		System.out.println(act == null);
		System.out.println((act.getOwnerUserId()));
		System.out.println( (int) ses.getAttribute("userID"));
		System.out.println((ses.getAttribute("userType").equals("Admin")));
		System.out.println((dto.amount < 0));
		System.out.println(act.getOwnerUserId() == (int) ses.getAttribute("userID"));
		System.out.println(ses.getAttribute("userType").equals("Admin"));
		if ((act == null) || 
				(!((act.getOwnerUserId() == (int) ses.getAttribute("userID")) || ses.getAttribute("userType").equals("Admin"))) || 
				(dto.amount < 0)) {
			res.setStatus(400);
			res.getWriter().println("Invalid deposit request");
		}

		// put act into a DTO object and update SQL record
		AccountDTO adto = new AccountDTO();
		adto.accountId = act.getAccountId();
		adto.balance = dto.amount + act.getBalance();
		adto.acctType = act.getType();
		adto.ownerId = act.getOwnerUserId();
		adto.status = act.getStatus();
		
		adto = as.updateAccount(adto);
		
		
		
        if (adto != null)
		{
			// new user
    		String message = "$"+dto.amount+" has been deposited to Account #"+adto.accountId;
    		HashMap<String,String> retMsg = new HashMap<String,String>();
    		retMsg.put("message", message);
			res.setStatus(201);
			res.getWriter().println(om.writeValueAsString(retMsg));
		} else {
			// failed to create new user
			res.setStatus(400);
			res.getWriter().println("UnExpected error while updating SQL record");
		}

        return;
	}

	public void withdraw(HttpServletRequest req, HttpServletResponse res) throws IOException {
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

		DepositDTO dto = om.readValue(body, DepositDTO.class);
		// Need to get requested account information to see what is allowed.
		Account act = as.findById(dto.accountId);

		HttpSession ses = req.getSession(false); // false - doesn't create session if it doesn't already exist
		// If account cannot be found, OR dto.amount<0 OR (Not Admin AND Not current
		// user acount) return error
		// TODO could add a check to act.getStatus() to find out if the account is OPEN or not.
		System.out.println("Account : Multi condition : amt<0");
		System.out.println(act == null);
		System.out.println((act.getOwnerUserId()));
		System.out.println( (int) ses.getAttribute("userID"));
		System.out.println((ses.getAttribute("userType").equals("Admin")));
		System.out.println((dto.amount < 0));
		System.out.println(act.getOwnerUserId() == (int) ses.getAttribute("userID"));
		System.out.println(ses.getAttribute("userType").equals("Admin"));
		if ((act == null) || 
				(!((act.getOwnerUserId() == (int) ses.getAttribute("userID")) || ses.getAttribute("userType").equals("Admin"))) || 
				(dto.amount < 0)) {
			res.setStatus(400);
			res.getWriter().println("Invalid withdraw request");
		}

		// put act into a DTO object and update SQL record
		AccountDTO adto = new AccountDTO();
		adto.accountId = act.getAccountId();
		adto.balance = act.getBalance() - dto.amount;
		adto.acctType = act.getType();
		adto.ownerId = act.getOwnerUserId();
		adto.status = act.getStatus();
		
		adto = as.updateAccount(adto);
		
		
		
        if (adto != null)
		{
			// new user
    		String message = "$"+dto.amount+" has been withdrawn from Account #"+adto.accountId;
    		HashMap<String,String> retMsg = new HashMap<String,String>();
    		retMsg.put("message", message);
			res.setStatus(201);
			res.getWriter().println(om.writeValueAsString(retMsg));
		} else {
			// failed to create new user
			res.setStatus(400);
			res.getWriter().println("UnExpected error while updating SQL record");
		}

        return;
	}

	public void transfer(HttpServletRequest req, HttpServletResponse res) throws IOException {
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

		TransferDTO dto = om.readValue(body, TransferDTO.class);
		// Need to get requested account information to see what is allowed.
		Account sact = as.findById(dto.sourceAccountId);
		Account tact = as.findById(dto.targetAccountId);

		HttpSession ses = req.getSession(false); // false - doesn't create session if it doesn't already exist
		// If account cannot be found, OR dto.amount<0 OR (Not Admin AND Not current
		// user acount) return error
		// TODO could add a check to act.getStatus() to find out if the account is OPEN or not.
		System.out.println("Account : Multi condition : amt<0");
		System.out.println(sact == null);
		System.out.println((sact.getOwnerUserId()));
		System.out.println( (int) ses.getAttribute("userID"));
		System.out.println((ses.getAttribute("userType").equals("Admin")));
		System.out.println((dto.amount < 0));
		System.out.println(sact.getOwnerUserId() == (int) ses.getAttribute("userID"));
		System.out.println(ses.getAttribute("userType").equals("Admin"));
		if ((sact == null) || (tact == null) ||
				(!((sact.getOwnerUserId() == (int) ses.getAttribute("userID")) || ses.getAttribute("userType").equals("Admin"))) || 
				(dto.amount < 0)) {
			res.setStatus(400);
			res.getWriter().println("Invalid withdraw request");
		}

		// put act into a DTO object and update SQL records
		// for both the source sdto and target account tdto
		AccountDTO sdto = new AccountDTO();
		AccountDTO tdto = new AccountDTO();
		sdto.accountId = sact.getAccountId();
		tdto.accountId = tact.getAccountId();
		sdto.balance = sact.getBalance() - dto.amount;
		tdto.balance = tact.getBalance() + dto.amount;
		sdto.acctType = sact.getType();
		tdto.acctType = tact.getType();
		sdto.ownerId = sact.getOwnerUserId();
		tdto.ownerId = tact.getOwnerUserId();
		sdto.status = sact.getStatus();
		tdto.status = tact.getStatus();
		
		System.out.println("Updating the Source account for the Withdraw");
		sdto = as.updateAccount(sdto);
		System.out.println("Updating the Target account for the Deposit");
		tdto = as.updateAccount(tdto);
		
		
		
        if ((sdto != null)&&(tdto != null))
		{
			// new user
    		String message = "$"+dto.amount+" has been transferred from Account #"+sdto.accountId+" to Account #"+tdto.accountId;
    		HashMap<String,String> retMsg = new HashMap<String,String>();
    		retMsg.put("message", message);
			res.setStatus(201);
			res.getWriter().println(om.writeValueAsString(retMsg));
		} else {
			// Should NEVER get to this point.  It would mean we could read both records, but not update them.
			// If possible then one transaction could have succeeded while the other failed.
			// Real world these transactions should be grouped such that they ALL work or else everything is reverted!
			res.setStatus(400);
			res.getWriter().println("UnExpected error while updating SQL records during transfer");
		}

        return;
	}


}
