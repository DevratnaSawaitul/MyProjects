package com.pms.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.pms.service.OrganizationService;

@Path("/org")
public class OrganizationController {

	@POST
	@Path("/show_tools")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getTools(String request) {
		return new OrganizationService().getTools(request);
	}

	@POST
	@Path("/show_skills")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getSkills(String request) {
		return new OrganizationService().getSkills(request);
	}

	@POST
	@Path("/show_process")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getProcess(String request) {
		return new OrganizationService().getProcess(request);
	}

	@POST
	@Path("/show_sub_process")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getSubProcess(String request) {
		return new OrganizationService().getSubProcess(request);
	}

	@POST
	@Path("/add_tools")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String addTools(String request) {
		return new OrganizationService().addTools(request);
	}

	@POST
	@Path("/add_skills")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String addSkills(String request) {
		return new OrganizationService().addSkills(request);
	}

	@POST
	@Path("/add_process")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String addProcess(String request) {
		return new OrganizationService().addProcess(request);
	}

	@POST
	@Path("/add_sub_process")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String addSubProcess(String request) {
		return new OrganizationService().addSubProcess(request);
	}

	@POST
	@Path("/delete_tools")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String deleteTools(String request) {
		return new OrganizationService().deleteTools(request);
	}

	@POST
	@Path("/delete_skills")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String deleteSkills(String request) {
		return new OrganizationService().deleteSkills(request);
	}

	@POST
	@Path("/delete_process")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String deleteProcess(String request) {
		return new OrganizationService().deleteProcess(request);
	}

	@POST
	@Path("/delete_sub_process")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String deleteSubProcess(String request) {
		return new OrganizationService().deleteSubProcess(request);
	}
}
