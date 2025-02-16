package com.pms.service;

import java.time.LocalDate;
import org.apache.poi.util.IOUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.pms.db.Process;
import com.pms.db.SheetProcess;
import com.pms.db.Sheets;
import com.pms.db.Steps;
import com.pms.util.FileUpload;
import com.pms.util.HibernateUtil;
import com.pms.util.MessageLog;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.persistence.Query;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.fileupload.FileItem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.hibernate.Session;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;

public class SheetService {

	String sep = System.getProperty("file.separator");
	String projectPath = FileUpload.filepath();
	String stepImagePath = projectPath + "Documents" + sep + "step_images" + sep;

	public String showRecentSheets(String request) {
		MessageLog.info("In SheetService showRecentSheets() request= " + request);
		JSONObject response = new JSONObject();
		try {
			JSONArray sheets = new JSONArray();
			Sheets s[] = new Sheets().retrieveAllWhere(
					" order by TO_TIMESTAMP(date_of_last_update, 'DD-MM-YYYY HH24:MI:SS') desc limit 6");
			if (s != null && s.length > 0) {
				for (Sheets s1 : s) {
					sheets.add(s1.getFileName());
				}
			}
			response.put("success", true);
			response.put("recentSheets", sheets);
		} catch (Exception e) {
			// Handle any unexpected errors
			response.put("success", false);
			response.put("message", e.getMessage());
		}
		return response.toString();
	}

	public String showSheets(String request) {
		MessageLog.info("In SheetService showSheets() request= " + request);
		JSONObject response = new JSONObject();
		JSONParser parser = new JSONParser();
		JSONArray sheets = new JSONArray();
		try {
			JSONObject info = (JSONObject) parser.parse(request);
			String loadType = info.get("load_type") != null ? (String) info.get("load_type") : "";
			if ("singleSheet".equalsIgnoreCase(loadType)) {
				String fileName = info.get("file_name") != null ? (String) info.get("file_name") : "";
				Sheets s[];

				if (!fileName.isEmpty()) {
					s = new Sheets().retrieveAllWhere(" where file_name='" + fileName + "'");
				} else {
					response.put("success", false);
					response.put("message", "sheet_id_or_file_name_required");
					return response.toString();
				}

				if (s != null) {
					JSONArray sheetProcessArr = new JSONArray();
					JSONObject sheetProcessObj = new JSONObject();
					JSONObject sheetDetails = new JSONObject();
					sheetDetails.put("sheet_id", s[0].getSheetId());
					sheetDetails.put("file_name", s[0].getFileName());
					sheetDetails.put("version", s[0].getVersion());
					sheetDetails.put("date", s[0].getDate());
					sheetDetails.put("department", s[0].getDepartment());
					sheetDetails.put("design_no", s[0].getDesignNo());
					sheetDetails.put("floor", s[0].getFloor());
					sheetDetails.put("last_updated_by", s[0].getLastUpdatedBy());
					sheetDetails.put("date_of_last_update", s[0].getDateOfLastUpdate());
					sheetDetails.put("status", s[0].getActive());
					sheets.add(sheetDetails);
					SheetProcess sp[] = new SheetProcess()
							.retrieveAllWhere("where file_name='" + s[0].getFileName() + "' order by sheet_process_id");
					if (sp != null && sp.length > 0) {
						for (SheetProcess spObj : sp) {
							JSONArray stepsArr = new JSONArray();
							JSONObject stepsObj = new JSONObject();
							sheetProcessObj = new JSONObject();
							sheetProcessObj.put("sheet_process_id", spObj.getSheetProcessId());
							sheetProcessObj.put("file_name", spObj.getFileName());
							sheetProcessObj.put("process_name", spObj.getProcessName());
							Steps step[] = new Steps().retrieveAllWhere(
									"where sheet_process_id='" + spObj.getSheetProcessId() + "' order by step_id");
							if (step != null && step.length > 0) {
								for (Steps stepObj : step) {
									stepsObj = new JSONObject();
									stepsObj.put("step_id", stepObj.getStepId());
									stepsObj.put("file_name", stepObj.getFileName());
									stepsObj.put("process_name", stepObj.getProcessName());
									stepsObj.put("subprocess_name", stepObj.getSubprocessName());
									stepsObj.put("step_number", stepObj.getStepNumber());
									stepsObj.put("tool_name", stepObj.getToolName());
									stepsObj.put("tool_spec", stepObj.getToolSpec());
									stepsObj.put("special_instruction", stepObj.getSpecialInstruction());
									stepsObj.put("skill", stepObj.getSkill());
									stepsObj.put("time_minutes", stepObj.getTimeMinutes());
									stepsObj.put("image_url", stepObj.getImageUrl());
									stepsObj.put("sheet_process_id", stepObj.getSheetProcess());
									stepsArr.add(stepsObj);
								}
							}
							sheetProcessObj.put("steps", stepsArr);
							sheetProcessArr.add(sheetProcessObj);
						}
					}
					response.put("success", true);
					response.put("sheets", sheets);
					response.put("sheet_process", sheetProcessArr);
				} else {
					response.put("success", false);
					response.put("message", "sheet_not_found");
				}
			} else if ("all".equalsIgnoreCase(loadType)) {
				String condition = "where 1=1 ";
				if (info.get("search_param") != null && !info.get("search_param").toString().isEmpty()) {
					condition = condition + " and lower(file_name) like '%"
							+ info.get("search_param").toString().toLowerCase().trim() + "%'";
				}
				if (info.get("from_date") != null && !info.get("from_date").toString().isEmpty()
						&& info.get("end_date") != null && !info.get("end_date").toString().isEmpty()) {
					condition = condition + " and date!='' and (to_date(date,'DD-MM-YYYY') between to_date('"
							+ (String) info.get("from_date") + "','DD-MM-YYYY') and to_date('"
							+ (String) info.get("end_date") + "','DD-MM-YYYY'))";
				} else if (info.get("from_date") != null && !info.get("from_date").toString().isEmpty()) {
					condition = condition + " and date!='' and to_date(date,'DD-MM-YYYY') = to_date('"
							+ (String) info.get("from_date") + "','DD-MM-YYYY')";
				} else if (info.get("end_date") != null && !info.get("end_date").toString().isEmpty()) {
					condition = condition + " and date!='' and to_date(date,'DD-MM-YYYY') = to_date('"
							+ (String) info.get("end_date") + "','DD-MM-YYYY')";
				}

				Sheets[] allSheets = new Sheets().retrieveAllWhere(condition + " order by sheet_id desc");

				if (allSheets != null && allSheets.length > 0) {
					for (Sheets s : allSheets) {
						JSONObject sheetDetails = new JSONObject();
						sheetDetails.put("sheet_id", s.getSheetId());
						sheetDetails.put("file_name", s.getFileName());
						sheetDetails.put("version", s.getVersion());
						sheetDetails.put("date", s.getDate());
						sheetDetails.put("department", s.getDepartment());
						sheetDetails.put("design_no", s.getDesignNo());
						sheetDetails.put("floor", s.getFloor());
						sheetDetails.put("last_updated_by", s.getLastUpdatedBy());
						sheetDetails.put("date_of_last_update", s.getDateOfLastUpdate());
						sheetDetails.put("status", s.getActive());
						sheets.add(sheetDetails);
					}
				}
				response.put("success", true);
				response.put("sheets", sheets);
			} else {
				response.put("success", false);
				response.put("message", "invalid_load_type");
			}
		} catch (Exception e) {
			response = new JSONObject();
			response.put("success", false);
			response.put("message", "exception");
			MessageLog.printError(e);
		}
		return response.toString();
	}

	public String saveSheet(String request) {
		MessageLog.info("In SheetService saveSheet() request= " + request);
		String currentTime = LocalDateTime.now()
				.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH));
		String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH));
		JSONObject response = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			JSONObject info = (JSONObject) parser.parse(request);
			Sheets s = new Sheets();
			s.setFileName(info.get("file_name") != null ? info.get("file_name").toString().trim() : "");

			if (s.getFileName().isEmpty()) {
				response.put("success", false);
				response.put("msg", "file_name_empty");
				return response.toString();
			}

			// Check if the sheet exists (for update logic)
			Sheets sList[] = s.retrieveAllWhere(" where lower(file_name)='" + s.getFileName().toLowerCase() + "'");

			// If no existing sheet is found, proceed with adding
			if (sList == null || sList.length <= 0) {
				if (info.get("operation").equals("update")) {
					response.put("success", false);
					response.put("msg", "file_name_not_exist");
					return response.toString();
				}

				// Add sheet logic
				s.setVersion(info.get("version") != null ? (String) info.get("version") : "1.0");
				s.setDate(currentDate);
				s.setDepartment(info.get("department") != null ? (String) info.get("department") : "");
				s.setDesignNo(info.get("design_no") != null ? (String) info.get("design_no") : "");
				s.setFloor(info.get("floor") != null ? (String) info.get("floor") : "");
				s.setLastUpdatedBy(info.get("last_updated_by") != null ? (String) info.get("last_updated_by") : "");
				s.setDateOfLastUpdate(currentTime);
				s.setActive(false);

				if (s.insert()) {
					response.put("success", true);
					response.put("msg", "sheet_added");
				} else {
					response.put("success", false);
					response.put("msg", "failed");
				}
			} else {
				// Update sheet logic
				if (info.get("operation").equals("add")) {
					response.put("success", false);
					response.put("msg", "file_name_already_exist");
					return response.toString();
				}

				s.setVersion(info.get("version") != null ? (String) info.get("version") : "1.0");
				s.setDate(info.get("date") != null ? (String) info.get("date") : "");
				s.setDepartment(info.get("department") != null ? (String) info.get("department") : "");
				s.setDesignNo(info.get("design_no") != null ? (String) info.get("design_no") : "");
				s.setFloor(info.get("floor") != null ? (String) info.get("floor") : "");
				s.setLastUpdatedBy(info.get("last_updated_by") != null ? (String) info.get("last_updated_by") : "");
				s.setDateOfLastUpdate(currentTime);
				boolean active = "yes".equalsIgnoreCase(info.get("status").toString())
						|| Boolean.TRUE.equals(info.get("status"));
				s.setActive(active);

				// Use update() instead of insert() for updating the sheet
				if (s.update()) {
					response.put("success", true);
					response.put("msg", "sheet_updated");
				} else {
					response.put("success", false);
					response.put("msg", "failed");
				}
			}
		} catch (Exception e) {
			response.put("success", false);
			response.put("msg", "some exception");
			MessageLog.printError(e);
		}
		return response.toString();
	}

	public String deleteSheet(String request) {
		MessageLog.info("In SheetService deleteSheet() request= " + request);
		JSONObject response = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			JSONObject info = (JSONObject) parser.parse(request);
			Sheets sheet = new Sheets();
			sheet.setSheetId(Long.parseLong(info.get("sheet_id").toString()));
			sheet.setFileName(info.get("file_name") != null ? info.get("file_name").toString().trim() : "");
			if (sheet.getFileName().isEmpty()) {
				response = new JSONObject();
				response.put("success", false);
				response.put("msg", "file_name_empty");
				return response.toString();
			}
			Sheets[] s = sheet.retrieveAllWhere("where sheet_id='" + sheet.getSheetId() + "' and lower(file_name)='"
					+ sheet.getFileName().toLowerCase() + "'");
			if (s == null || s.length <= 0) {
				response = new JSONObject();
				response.put("success", false);
				response.put("msg", "sheet_not_exist");
				return response.toString();
			}

			Steps step = new Steps();
			SheetProcess sheetProcess = new SheetProcess();
			Steps[] stepList = step
					.retrieveAllWhere("where lower(file_name)='" + sheet.getFileName().trim().toLowerCase() + "'");
			SheetProcess[] sheetProcessList = sheetProcess
					.retrieveAllWhere("where lower(file_name)='" + sheet.getFileName().trim().toLowerCase() + "'");
			if (sheet.delete(sheetProcessList, stepList)) {
				response.put("success", true);
				response.put("msg", "sheet_deleted");
			} else {
				response.put("success", false);
				response.put("msg", "failed");
			}
		} catch (Exception e) {
			response.put("success", false);
			response.put("msg", "some exception");
			MessageLog.printError(e);
		}
		return response.toString();
	}

	public String saveStep(HttpServletRequest request) {
		MessageLog.info("In SheetService saveStep()");
		JSONObject response = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			JSONObject info = FileUpload.convertRequetToJson(request);
			String operation = info.get("operation") != null ? info.get("operation").toString().trim().toLowerCase()
					: "";
			if (!operation.equals("add") && !operation.equals("update")) {
				response.put("success", false);
				response.put("msg", "invalid_operation");
				return response.toString();
			}

			Steps step = new Steps();
			step.setFileName(info.get("file_name") != null ? info.get("file_name").toString().trim() : "");
			step.setProcessName(info.get("process_name") != null ? info.get("process_name").toString().trim() : "");
			step.setSubprocessName(
					info.get("subprocess_name") != null ? info.get("subprocess_name").toString().trim() : "");
			step.setStepNumber(
					info.get("step_number") != null ? Integer.parseInt(info.get("step_number").toString()) : 0);
			step.setToolName(info.get("tool_name") != null ? info.get("tool_name").toString().trim() : "");
			step.setToolSpec(info.get("tool_spec") != null ? info.get("tool_spec").toString().trim() : "");
			step.setSpecialInstruction(
					info.get("special_instruction") != null ? info.get("special_instruction").toString().trim() : "");
			step.setSkill(info.get("skill") != null ? info.get("skill").toString().trim() : "");
			step.setTimeMinutes(
					info.get("time_minutes") != null ? Integer.parseInt(info.get("time_minutes").toString()) : 0);
			// Set image_url from JSON if provided (this may be empty)
			step.setImageUrl(info.get("image_url") != null ? info.get("image_url").toString().trim() : "");
			step.setSheetProcess(Long.parseLong(info.get("sheet_process_id").toString()));

			// --- Begin file upload and image resizing block ---
			FileItem item = null;
			if (info.get("file") != null && !info.get("file").equals("null") && !info.get("file").equals("")) {
				item = (FileItem) info.get("file");
			}
			if (item != null && item.getName() != null && item.getSize() > 0) {
				long maxSize = 5 * 1024 * 1024; // 5 MB
				if (item.getSize() > maxSize) {
					response.put("success", false);
					response.put("msg", "file_too_large");
					return response.toString();
				}

				BufferedImage originalImage = ImageIO.read(item.getInputStream());
				if (originalImage == null) {
					response.put("success", false);
					response.put("msg", "invalid_image");
					return response.toString();
				}

				int originalWidth = originalImage.getWidth();
				int originalHeight = originalImage.getHeight();
				double scale = Math.min(200.0 / originalWidth, 200.0 / originalHeight);
				if (scale > 1.0) {
					scale = 1.0;
				}
				int newWidth = (int) Math.round(originalWidth * scale);
				int newHeight = (int) Math.round(originalHeight * scale);

				BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = resizedImage.createGraphics();
				g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
				g2d.dispose();

				File folder = new File(stepImagePath);
				if (!folder.exists()) {
					folder.mkdirs();
				}
				String imageFileName = step.getFileName() + "_step_" + step.getSheetProcess() + "_"
						+ step.getSubprocessName() + "_" + System.currentTimeMillis() + ".jpg";

				File file = new File(stepImagePath + imageFileName);
				if (file.exists()) {
					file.delete();
				}
				ImageIO.write(resizedImage, "jpg", file);

				// Build public URL (make sure this folder is mapped for static serving)
				String publicUrl = "/ProcessManager/Documents/step_images/" + imageFileName;
				System.out.println(stepImagePath + imageFileName);
				System.out.println(publicUrl);
				step.setImageUrl(publicUrl);
			}
			// --- End file upload block ---

			if (operation.equals("update")) {
				step.setStepId(Long.parseLong(info.get("step_id").toString()));
				Steps[] existingStep = step.retrieveAllWhere("where step_id='" + step.getStepId() + "'");
				if (existingStep == null || existingStep.length <= 0) {
					response.put("success", false);
					response.put("msg", "step_id_not_exist");
					return response.toString();
				}
				if (step.update()) {
					Sheets s = new Sheets();
					s.updateLast(step.getFileName());
					response.put("success", true);
					response.put("msg", "step_updated");
				} else {
					response.put("success", false);
					response.put("msg", "failed");
				}
			} else { // Add operation
				if (step.insert()) {
					Sheets s = new Sheets();
					s.updateLast(step.getFileName());
					response.put("success", true);
					response.put("msg", "step_added");
				} else {
					response.put("success", false);
					response.put("msg", "failed");
				}
			}
		} catch (Exception e) {
			MessageLog.printError(e);
			response.put("success", false);
			response.put("msg", "some exception");
		}
		return response.toString();
	}

	public String deleteStep(String request) {
		MessageLog.info("In SheetService deleteStep() request= " + request);
		JSONObject response = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			JSONObject info = (JSONObject) parser.parse(request);
			Steps step = new Steps();
			step.setStepId(Long.parseLong(info.get("step_id").toString()));
			Steps[] s = step.retrieveAllWhere("where step_id='" + step.getStepId() + "'");
			if (s == null || s.length <= 0) {
				response = new JSONObject();
				response.put("success", false);
				response.put("msg", "step_not_exist");
				return response.toString();
			}
			if (step.delete()) {
				Sheets s1 = new Sheets();
				s1.updateLast(s[0].getFileName());
				response.put("success", true);
				response.put("msg", "step_deleted");
			} else {
				response.put("success", false);
				response.put("msg", "failed");
			}
		} catch (Exception e) {
			response.put("success", false);
			response.put("msg", "some exception");
			MessageLog.printError(e);
		}
		return response.toString();
	}

	public String addSheetProcess(String request) {
		MessageLog.info("In SheetService addStep() request= " + request);
		JSONObject response = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			JSONObject info = (JSONObject) parser.parse(request);
			SheetProcess sp = new SheetProcess();
			sp.setFileName(info.get("file_name") != null ? info.get("file_name").toString().trim() : "");
			sp.setProcessName(info.get("process_name") != null ? info.get("process_name").toString().trim() : "");
			if (sp.getFileName().isEmpty() || sp.getProcessName().isEmpty()) {
				response = new JSONObject();
				response.put("success", false);
				response.put("msg", "process_file_empty");
				return response.toString();
			}
			Sheets[] s = new Sheets()
					.retrieveAllWhere("where lower(file_name)='" + sp.getFileName().toLowerCase() + "'");
			Process[] p = new Process()
					.retrieveAllWhere("where lower(process_name)='" + sp.getProcessName().toLowerCase() + "'");
			if (s == null || s.length <= 0 || p == null || p.length <= 0) {
				response = new JSONObject();
				response.put("success", false);
				response.put("msg", "process_or_sheet_not_exist");
				return response.toString();
			}
			if (sp.insert()) {
				Sheets s1 = new Sheets();
				s1.updateLast(sp.getFileName());
				response.put("success", true);
				response.put("msg", "sheet_process_added");
			} else {
				response.put("success", false);
				response.put("msg", "failed");
			}
		} catch (Exception e) {
			response.put("success", false);
			response.put("msg", "some exception");
			MessageLog.printError(e);
		}
		return response.toString();
	}

	public String deleteSheetProcess(String request) {
		MessageLog.info("In SheetService deleteSheetProcess() request= " + request);
		JSONObject response = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			JSONObject info = (JSONObject) parser.parse(request);
			SheetProcess sheet_process = new SheetProcess();
			sheet_process.setSheetProcessId(Long.parseLong(info.get("sheet_process_id").toString()));

			Steps step = new Steps();
			SheetProcess sp[] = sheet_process
					.retrieveAllWhere("where sheet_process_id='" + sheet_process.getSheetProcessId() + "'");
			if (sp == null || sp.length <= 0) {
				response = new JSONObject();
				response.put("success", false);
				response.put("msg", "sheet_process_not_exist");
				return response.toString();
			}
			Steps[] stepList = step
					.retrieveAllWhere("where sheet_process_id='" + sheet_process.getSheetProcessId() + "'");
			if (sheet_process.delete(stepList)) {
				Sheets s1 = new Sheets();
				s1.updateLast(sp[0].getFileName());
				response.put("success", true);
				response.put("msg", "sheet_process_deleted");
			} else {
				response.put("success", false);
				response.put("msg", "failed");
			}
		} catch (Exception e) {
			response.put("success", false);
			response.put("msg", "some exception");
			MessageLog.printError(e);
		}
		return response.toString();

	}

	public String setSheetStatus(String request) {
		MessageLog.info("In SheetService setSheetStatus() request= " + request);
		JSONObject response = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			JSONObject info = (JSONObject) parser.parse(request);
			String fileName = info.get("file_name") != null ? info.get("file_name").toString().trim() : "";
			if (fileName.isEmpty()) {
				response.put("success", false);
				response.put("msg", "file_name_empty");
				return response.toString();
			}
			Long sheetId = Long.parseLong(info.get("sheet_id").toString());
			boolean active = "yes".equalsIgnoreCase(info.get("status").toString())
					|| Boolean.TRUE.equals(info.get("status"));
			Sheets s[] = new Sheets()
					.retrieveAllWhere("where sheet_id='" + sheetId + "' and file_name='" + fileName + "'");
			if (s != null && s.length > 0) {
				s[0].setActive(active);
				s[0].setDateOfLastUpdate(
						LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH)));
				if (s[0].update()) {
					response.put("success", true);
					response.put("msg", "sheet_status_updated");
				} else {
					response.put("success", false);
					response.put("msg", "sheet_updation_failed");
				}
			} else {
				response.put("success", false);
				response.put("msg", "sheet_not_found");
			}
		} catch (Exception e) {
			MessageLog.printError(e);
			response.put("success", false);
			response.put("msg", "some exception");
		}
		return response.toString();
	}

	public HttpServletResponse generateExcel(String req, HttpServletResponse response) {
		MessageLog.info("In SheetService generateExcel() req=" + req);
		Session session = HibernateUtil.pmsSessionFactory.openSession();
		JSONParser parser = new JSONParser();

		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			JSONObject info = (JSONObject) parser.parse(req);
			Long sheet_id = Long.parseLong(info.get("sheet_id").toString());
			String filename = "Sheet_Report.xlsx";

			if (sheet_id != null) {
				Sheets sheet[] = new Sheets().retrieveAllWhere("where sheet_id='" + sheet_id + "'");
				if (sheet != null && sheet.length > 0) {
					XSSFSheet excelSheet = workbook.createSheet("Sheet Data");
					int rowNum = 0;
					filename = sheet[0].getFileName() + ".xlsx";

					// ** Cell Style (Border for All Cells) **
					XSSFCellStyle borderStyle = workbook.createCellStyle();
					borderStyle.setBorderTop(BorderStyle.THIN);
					borderStyle.setBorderBottom(BorderStyle.THIN);
					borderStyle.setBorderLeft(BorderStyle.THIN);
					borderStyle.setBorderRight(BorderStyle.THIN);
					borderStyle.setAlignment(HorizontalAlignment.CENTER);
					borderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					borderStyle.setWrapText(true);

					XSSFCellStyle headerStyle = workbook.createCellStyle();
					headerStyle.cloneStyleFrom(borderStyle);
					headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
					headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					XSSFFont boldFont = workbook.createFont();
					boldFont.setBold(true);
					headerStyle.setFont(boldFont);

					// ** Column Headers **
					Row columnHeaderRow = excelSheet.createRow(rowNum++);
					String[] headers = { "Step #", "Sub Process", "Tools", "Tool Spec", "Special Instruction",
							"Skill (SK)", "Time (min)", "Image" };
					for (int i = 0; i < headers.length; i++) {
						Cell cell = columnHeaderRow.createCell(i);
						cell.setCellValue(headers[i]);
						cell.setCellStyle(headerStyle);
					}

					// ** Set Column Widths **
					for (int i = 0; i < headers.length; i++) {
						excelSheet.setColumnWidth(i, 5000);
					}
					excelSheet.setColumnWidth(7, 7500); // Image column width

					// ** Fetch Sheet Processes **
					SheetProcess sheetProcessArr[] = new SheetProcess().retrieveAllWhere(
							"where file_name='" + sheet[0].getFileName() + "' order by sheet_process_id");
					if (sheetProcessArr != null && sheetProcessArr.length > 0) {
						for (SheetProcess sp : sheetProcessArr) {

							// ** Get Total Time for Steps in Process **
							String avg_condition = "select sum(time_minutes) from Steps where sheet_process_id='"
									+ sp.getSheetProcessId() + "'";
							Query query = session.createQuery(avg_condition);
							List<?> stepAgrTime = query.getResultList();
							Long totalTime = 0L;
							if (stepAgrTime != null && !stepAgrTime.isEmpty() && stepAgrTime.get(0) instanceof Number) {
								totalTime = ((Number) stepAgrTime.get(0)).longValue();
							}

							// ** Process Row with Total Time **
							Row processRow = excelSheet.createRow(rowNum++);
							Cell processCell = processRow.createCell(0);
							processCell.setCellValue(sp.getProcessName() + " (" + totalTime + " min)");
							excelSheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 2));

							// ** Fetch Steps for Process **
							Steps[] stepsArr = new Steps().retrieveAllWhere(
									"where sheet_process_id='" + sp.getSheetProcessId() + "' order by step_number");
							if (stepsArr != null && stepsArr.length > 0) {
								for (Steps ste : stepsArr) {
									Row dataRow = excelSheet.createRow(rowNum++);

									for (int i = 0; i < 7; i++) {
										Cell cell = dataRow.createCell(i);
										cell.setCellStyle(borderStyle);
									}

									dataRow.getCell(0).setCellValue(ste.getStepNumber());
									dataRow.getCell(1).setCellValue(ste.getSubprocessName());
									dataRow.getCell(2).setCellValue(ste.getToolName());
									dataRow.getCell(3).setCellValue(ste.getToolSpec());
									dataRow.getCell(4).setCellValue(ste.getSpecialInstruction());
									dataRow.getCell(5).setCellValue(ste.getSkill());
									dataRow.getCell(6).setCellValue(ste.getTimeMinutes());

									// ** Image Handling (Keep Aspect Ratio, Center Inside 200x200) **
									if (ste.getImageUrl() != null && !ste.getImageUrl().isEmpty()
											&& stepImagePath != null && !stepImagePath.isEmpty()) {
										dataRow.setHeight((short) (200 * 15.5)); // Fixed row height 200px
										String publicUrl = ste.getImageUrl();
										String fileName = Paths.get(publicUrl).getFileName().toString();
										String fullFilePath = Paths.get(stepImagePath, fileName).toString();

										if (new File(fullFilePath).exists()) {
											InputStream inputStream = new FileInputStream(fullFilePath);
											byte[] imageBytes = IOUtils.toByteArray(inputStream);
											inputStream.close();

											int pictureIdx = workbook.addPicture(imageBytes,
													Workbook.PICTURE_TYPE_JPEG);
											XSSFDrawing drawing = excelSheet.createDrawingPatriarch();
											CreationHelper helper = workbook.getCreationHelper();

											// ** Anchor for Image Placement (Centered in Cell) **
											XSSFClientAnchor anchor = (XSSFClientAnchor) helper.createClientAnchor();
											anchor.setCol1(7); // Image in column 8
											anchor.setRow1(rowNum - 1);
											anchor.setCol2(8); // Cover only one column
											anchor.setRow2(rowNum);
											anchor.setDx1(512);
											anchor.setDy1(256);
											anchor.setDx2(512);
											anchor.setDy2(256);

											// ** Add Image to Sheet **
											XSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
											picture.resize(); // Ensure it stays within the 200px box

											// ** Apply Border to Image Cell **
											Cell imageCell = dataRow.createCell(7);
											imageCell.setCellStyle(borderStyle);
										}
									} else {
										Cell imageCell = dataRow.createCell(7);
										imageCell.setCellStyle(borderStyle);
										dataRow.setHeight((short) (200 * 5.5)); // Fixed row height 200px
									}
								}
							}
						}
					}
				} else {
					// ** Create New Sheet and Show "No Data Found" **
					XSSFSheet sheet1 = workbook.createSheet("Sheet1");
					Row row = sheet1.createRow(0);
					Cell cell = row.createCell(0);
					cell.setCellValue("No Data Found");

					XSSFCellStyle noDataStyle = workbook.createCellStyle();
					noDataStyle.setAlignment(HorizontalAlignment.CENTER);
					noDataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					cell.setCellStyle(noDataStyle);

					sheet1.autoSizeColumn(0);
				}
			}

			// ** Write workbook to response **
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			workbook.write(outByteStream);
			byte[] bytes = outByteStream.toByteArray();

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.setContentLength(bytes.length);

			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(bytes);
			outputStream.flush();
		} catch (Exception e) {
			MessageLog.printError(e);
		}
		return response;
	}
}