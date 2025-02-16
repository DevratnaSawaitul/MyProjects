package com.pms.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import com.pms.service.SheetService;

@Path("/sheets")
public class SheetController {
	@Context
	HttpServletRequest request1;
	@Context
	HttpServletResponse response1;

	@POST
	@Path("/recent")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getRecentSheets(String request) {
		return new SheetService().showRecentSheets(request);
	}

	@POST
	@Path("/add_sheets")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String addSheet(String request) {
		return new SheetService().saveSheet(request);
	}

	@POST
	@Path("/showSheets")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String showSheets(String request) {
		return new SheetService().showSheets(request);
	}
//
//	@POST
//	@Path("/addStep")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
//	public String saveStep(String request) {
//		return new SheetService().saveStep(request);
//	}

	@POST
	@Path("/addStep")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String createPerson() {
		return new SheetService().saveStep(request1);
	}

	@POST
	@Path("/deleteSheet")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String deleteSheet(String request) {
		return new SheetService().deleteSheet(request);
	}

	@POST
	@Path("/deleteStep")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String deleteStep(String request) {
		return new SheetService().deleteStep(request);
	}

	@POST
	@Path("/addSheetProcess")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String addSheetProcess(String request) {
		return new SheetService().addSheetProcess(request);
	}

	@POST
	@Path("/deleteSheetProcess")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String deleteSheetProcess(String request) {
		return new SheetService().deleteSheetProcess(request);
	}

	@POST
	@Path("/setSheetStatus")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String setSheetStatus(String request) {
		return new SheetService().setSheetStatus(request);
	}
	
	@Path("/generateExcel")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public HttpServletResponse generateExcel(String info) {
		return new SheetService().generateExcel(info, response1);
	}
}
