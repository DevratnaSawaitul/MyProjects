package com.pms.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pms.service.LoginService;

@Path("/genral_controller")
@Produces(MediaType.APPLICATION_JSON)
public class GenralController {
	@Context
	HttpServletResponse res;
	@Context
	HttpServletRequest req;

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public String login(String user_login) {
		return new LoginService().userLogin(user_login, res, req);
	}
}