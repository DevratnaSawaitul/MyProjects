package com.pms.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.pms.db.SystemSettings;
import com.pms.util.MessageLog;
import com.pms.util.NormalEncrp;

public class LoginService {

    public String userLogin(String userLogin, HttpServletResponse res, HttpServletRequest request) {
    	MessageLog.info("in login");
    	JSONObject response = new JSONObject();
        try {
            // Parse the input JSON
            JSONParser parser = new JSONParser();
            JSONObject loginInfo = (JSONObject) parser.parse(userLogin);

            // Extract and decrypt the username and password
            String username = loginInfo.get("username") != null ? NormalEncrp.decrypt(loginInfo.get("username").toString().trim()) : null;
            String password = loginInfo.get("password") != null ? NormalEncrp.decrypt(loginInfo.get("password").toString().trim()) : null;

            // Validate fields
            if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
                response.put("success", false);
                response.put("msg", "Enter all fields");
                return response.toString();
            }

            // Check credentials in the database
            SystemSettings usernameCheck = new SystemSettings().retrieveByKey("username", username);
            SystemSettings passwordCheck = new SystemSettings().retrieveByKey("password", password);

            if (usernameCheck == null || passwordCheck == null) {
                response.put("success", false);
                response.put("msg", "Invalid username/password");
                return response.toString();
            }

            // Create success response
            response.put("success", true);
            response.put("msg", "Login Success");
            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("msg", "An exception occurred");
            return response.toString();
        }
    }
}
