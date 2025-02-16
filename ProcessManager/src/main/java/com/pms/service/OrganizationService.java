package com.pms.service;

import java.util.HashSet;
import java.util.Set;

import org.apache.xpath.operations.Bool;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.pms.db.Skills;
import com.pms.db.Steps;
import com.pms.db.Subprocesses;
import com.pms.db.Process;
import com.pms.db.Tools;
import com.pms.util.MessageLog;

public class OrganizationService {

	public String getProcess(String request) {
		MessageLog.info("In OrganizationService getProcess() request= " + request);
		JSONObject response = new JSONObject();
		JSONParser parser = new JSONParser();
		JSONArray processJsonArray = new JSONArray();
		try {
			JSONObject processJsonObj = new JSONObject();
			Process[] processes = null;
			JSONObject info = (JSONObject) parser.parse(request);
			String loadType = info.get("load_type") != null ? (String) info.get("load_type") : "";
			if ("active".equalsIgnoreCase(loadType)) {
				processes = new Process().retrieveAllWhere(" where active='true' order by process_name");

				if (processes != null && processes.length > 0) {
					for (Process s1 : processes) {
						processJsonObj = new JSONObject();
						processJsonObj.put("process_name", s1.getProcessName());
						processJsonObj.put("process_id", s1.getProcessId());
						processJsonObj.put("active", s1.getActive());
						processJsonArray.add(processJsonObj);
					}
					response.put("success", true);
					response.put("processes", processJsonArray);
				} else {
					response.put("success", false);
					response.put("message", "no_process_found");
				}
			} else if ("all".equalsIgnoreCase(loadType)) {
				processes = new Process().retrieveAllWhere(" order by process_id desc");
				if (processes != null && processes.length > 0) {
					for (Process s1 : processes) {
						processJsonObj = new JSONObject();
						processJsonObj.put("process_name", s1.getProcessName());
						processJsonObj.put("process_id", s1.getProcessId());
						processJsonObj.put("active", s1.getActive());
						processJsonArray.add(processJsonObj);
					}
				}
				response.put("success", true);
				response.put("processes", processJsonArray);
			} else {
				response.put("success", false);
				response.put("message", "invalid_load_type");
			}
		} catch (Exception e) {
			response = new JSONObject();
			response.put("success", false);
			response.put("message", "some exception");
			MessageLog.printError(e);
		}
		return response.toString();
	}

	public String getSubProcess(String request) {
		MessageLog.info("In OrganizationService getSubProcess() request= " + request);
		JSONObject response = new JSONObject();
		JSONParser parser = new JSONParser();
		JSONArray subProcess = new JSONArray();
		try {
			JSONObject subProcessDetails = new JSONObject();
			Subprocesses[] subprocesses = null;
			JSONObject info = (JSONObject) parser.parse(request);
			String loadType = info.get("load_type") != null ? (String) info.get("load_type") : "";
			if ("process_related".equalsIgnoreCase(loadType)) {
				String process = info.get("process") != null ? (String) info.get("process") : "";
				if (!process.isEmpty()) {
					subprocesses = new Subprocesses()
							.retrieveAllWhere(" where process_name='" + process + "' and active='true' order by subprocess_name");
				} else {
					response.put("success", false);
					response.put("message", "process_name_required");
					return response.toString();
				}

				if (subprocesses != null && subprocesses.length > 0) {
					for (Subprocesses s1 : subprocesses) {
						subProcessDetails = new JSONObject();
						subProcessDetails.put("process_name", s1.getProcessName());
						subProcessDetails.put("subprocess_name", s1.getSubprocessName());
						subProcessDetails.put("subprocess_id", s1.getSubprocessId());
						subProcessDetails.put("active", s1.getActive());
						subProcess.add(subProcessDetails);
					}
					response.put("success", true);
					response.put("sub_process", subProcess);
				} else {
					response.put("success", false);
					response.put("message", "no_sub_process_found");
				}
			} else if ("all".equalsIgnoreCase(loadType)) {
				subprocesses = new Subprocesses().retrieveAllWhere(" order by subprocess_id desc");
				if (subprocesses != null && subprocesses.length > 0) {
					for (Subprocesses s : subprocesses) {
						subProcessDetails = new JSONObject();
						subProcessDetails.put("process_name", s.getProcessName());
						subProcessDetails.put("subprocess_name", s.getSubprocessName());
						subProcessDetails.put("subprocess_id", s.getSubprocessId());
						subProcessDetails.put("active", s.getActive());
						subProcess.add(subProcessDetails);
					}
				}
				response.put("success", true);
				response.put("sub_process", subProcess);
			} else if ("active".equalsIgnoreCase(loadType)) {
				subprocesses = new Subprocesses().retrieveAllWhere(" where active='true' order by subprocess_name");
				Set<String> addedSubProcessNames = new HashSet<>();
				if (subprocesses != null && subprocesses.length > 0) {
					for (Subprocesses s : subprocesses) {
						if (addedSubProcessNames.contains(s.getSubprocessName())) {
							continue;
						}
						subProcessDetails = new JSONObject();
						subProcessDetails.put("subprocess_name", s.getSubprocessName());
						subProcessDetails.put("subprocess_id", s.getSubprocessId());
						subProcessDetails.put("active", s.getActive());
						subProcess.add(subProcessDetails);
						addedSubProcessNames.add(s.getSubprocessName());
					}
				}
				response.put("success", true);
				response.put("sub_process", subProcess);
			} else {
				response.put("success", false);
				response.put("message", "invalid_load_type");
			}
		} catch (Exception e) {
			response = new JSONObject();
			response.put("success", false);
			response.put("message", "some exception");
			MessageLog.printError(e);
		}
		return response.toString();
	}

	public String getTools(String request) {
		MessageLog.info("In OrganizationService getTools() request= " + request);
		JSONObject response = new JSONObject();
		JSONParser parser = new JSONParser();
		JSONArray toolsJSONArray = new JSONArray();
		try {
			JSONObject toolsJSONObj = new JSONObject();
			Tools[] tools = null;
			JSONObject info = (JSONObject) parser.parse(request);
			String loadType = info.get("load_type") != null ? (String) info.get("load_type") : "";
			if ("sub_process_related".equalsIgnoreCase(loadType)) {
				String sub_process = info.get("sub_process") != null ? (String) info.get("sub_process") : "";
				if (!sub_process.isEmpty()) {
					tools = new Tools().retrieveAllWhere(" where sub_process='" + sub_process + "' and active='true' order by tool_name");
				} else {
					response.put("success", false);
					response.put("message", "sub_process_required");
					return response.toString();
				}

				if (tools != null && tools.length > 0) {
					for (Tools s1 : tools) {
						toolsJSONObj = new JSONObject();
						toolsJSONObj.put("tool_name", s1.getToolName());
						toolsJSONObj.put("sub_process", s1.getSubProcess());
						toolsJSONObj.put("tool_id", s1.getToolId());
						toolsJSONObj.put("active", s1.getActive());
						toolsJSONArray.add(toolsJSONObj);
					}
					response.put("success", true);
					response.put("tools", toolsJSONArray);
				} else {
					response.put("success", false);
					response.put("message", "no_tools_found_related_to_subprocess");
				}
			} else if ("all".equalsIgnoreCase(loadType)) {
				tools = new Tools().retrieveAllWhere(" order by tool_id desc");
				if (tools != null && tools.length > 0) {
					for (Tools s1 : tools) {
						toolsJSONObj = new JSONObject();
						toolsJSONObj.put("tool_name", s1.getToolName());
						toolsJSONObj.put("sub_process", s1.getSubProcess());
						toolsJSONObj.put("tool_id", s1.getToolId());
						toolsJSONObj.put("active", s1.getActive());
						toolsJSONArray.add(toolsJSONObj);
					}
				}
				response.put("success", true);
				response.put("tools", toolsJSONArray);
			} else {
				response.put("success", false);
				response.put("message", "invalid_load_type");
			}
		} catch (Exception e) {
			response = new JSONObject();
			response.put("success", false);
			response.put("message", "some exception");
			MessageLog.printError(e);
		}
		return response.toString();
	}

	public String getSkills(String request) {
		MessageLog.info("In OrganizationService getSkills() request= " + request);
		JSONObject response = new JSONObject();
		JSONParser parser = new JSONParser();
		JSONArray skillsJSONArray = new JSONArray();
		try {
			JSONObject skillsJSONObj = new JSONObject();
			Skills[] skills = null;
			JSONObject info = (JSONObject) parser.parse(request);
			String loadType = info.get("load_type") != null ? (String) info.get("load_type") : "";
			if ("active".equalsIgnoreCase(loadType)) {
				skills = new Skills().retrieveAllWhere(" where active='true' order by skill_name");

				if (skills != null && skills.length > 0) {
					for (Skills s1 : skills) {
						skillsJSONObj = new JSONObject();
						skillsJSONObj.put("skill_name", s1.getSkillName());
						skillsJSONObj.put("skill_id", s1.getSkillId());
						skillsJSONObj.put("active", s1.getActive());
						skillsJSONArray.add(skillsJSONObj);
					}
					response.put("success", true);
					response.put("skills", skillsJSONArray);
				} else {
					response.put("success", false);
					response.put("message", "no_active_skills_found");
				}
			} else if ("all".equalsIgnoreCase(loadType)) {
				skills = new Skills().retrieveAllWhere(" order by skill_id desc");
				if (skills != null && skills.length > 0) {
					for (Skills s1 : skills) {
						skillsJSONObj = new JSONObject();
						skillsJSONObj.put("skill_name", s1.getSkillName());
						skillsJSONObj.put("skill_id", s1.getSkillId());
						skillsJSONObj.put("active", s1.getActive());
						skillsJSONArray.add(skillsJSONObj);
					}
				}
				response.put("success", true);
				response.put("skills", skillsJSONArray);
			} else {
				response.put("success", false);
				response.put("message", "invalid_load_type");
			}
		} catch (Exception e) {
			response = new JSONObject();
			response.put("success", false);
			response.put("message", "some exception");
			MessageLog.printError(e);
		}
		return response.toString();
	}

	public String addProcess(String request) {
		MessageLog.info("In OrganizationService addProcess() request= " + request);
		JSONObject response = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			JSONObject info = (JSONObject) parser.parse(request);
			String operation = info.get("operation") != null ? (String) info.get("operation") : "";
			boolean active = "yes".equalsIgnoreCase(info.get("active").toString())
					|| Boolean.TRUE.equals(info.get("active"));
			String process_name = info.get("process_name") != null ? info.get("process_name").toString().trim() : "";
			if (process_name.isEmpty()) {
				response = new JSONObject();
				response.put("success", false);
				response.put("message", "process_name_is_manadatory");
				return response.toString();
			}
			Process[] p1 = new Process()
					.retrieveAllWhere("where lower(process_name)='" + process_name.toLowerCase() + "'");
			Process p = new Process();
			if (operation.equalsIgnoreCase("add")) {
				if (p1 != null && p1.length > 0) {
					response = new JSONObject();
					response.put("success", false);
					response.put("message", "already_exist");
					return response.toString();
				}
				p.setProcessName(process_name);
				p.setActive(true);
				if (p.insert()) {
					response = new JSONObject();
					response.put("success", true);
					response.put("message", "process_added");
				} else {
					response = new JSONObject();
					response.put("success", false);
					response.put("message", "failed");
				}
			} else if (operation.equalsIgnoreCase("update")) {
				if (p1 == null || p1.length <= 0) {
					response = new JSONObject();
					response.put("success", false);
					response.put("message", "process_name_not_exist");
					return response.toString();
				}
				p.setProcessName(p1[0].getProcessName());
				p.setActive(active);
				if (p.update()) {
					response = new JSONObject();
					response.put("success", true);
					response.put("message", "process_updated");
				} else {
					response = new JSONObject();
					response.put("success", false);
					response.put("message", "failed");
				}
			}
		} catch (Exception e) {
			response = new JSONObject();
			response.put("success", false);
			response.put("message", "some exception");
			MessageLog.printError(e);
		}
		return response.toString();
	}

	public String addSubProcess(String request) {
		MessageLog.info("In OrganizationService addSubProcess() request= " + request);
		JSONObject response = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			JSONObject info = (JSONObject) parser.parse(request);
			String operation = info.get("operation") != null ? (String) info.get("operation") : "";
			boolean active = "yes".equalsIgnoreCase(info.get("active").toString())
					|| Boolean.TRUE.equals(info.get("active"));
			String process_name = info.get("process_name") != null ? info.get("process_name").toString().trim() : "";
			String subprocess_name = info.get("subprocess_name") != null ? info.get("subprocess_name").toString().trim()
					: "";
			if (subprocess_name.isEmpty()) {
				response = new JSONObject();
				response.put("success", false);
				response.put("message", "subprocess_name_is_manadatory");
				return response.toString();
			}
			if (process_name.isEmpty()) {
				response = new JSONObject();
				response.put("success", false);
				response.put("message", "process_name_is_manadatory");
				return response.toString();
			}
			Process[] pro = new Process()
					.retrieveAllWhere("where lower(process_name)='" + process_name.toLowerCase() + "'");
			if (pro == null || pro.length <= 0) {
				response = new JSONObject();
				response.put("success", false);
				response.put("msg", "process_not_exist");
				return response.toString();
			}
			Subprocesses[] p1 = new Subprocesses()
					.retrieveAllWhere("where lower(process_name)='" + process_name.toLowerCase()
							+ "' and lower(subprocess_name)='" + subprocess_name.toLowerCase() + "'");
			Subprocesses p = new Subprocesses();
			if (operation.equalsIgnoreCase("add")) {
				if (p1 != null && p1.length > 0) {
					response = new JSONObject();
					response.put("success", false);
					response.put("message", "already_exist");
					return response.toString();
				}
				p.setProcessName(process_name);
				p.setSubprocessName(subprocess_name);
				p.setActive(true);
				if (p.insert()) {
					response = new JSONObject();
					response.put("success", true);
					response.put("message", "sub_process_added");
				} else {
					response = new JSONObject();
					response.put("success", false);
					response.put("message", "failed");
				}
			} else if (operation.equalsIgnoreCase("update")) {
				if (p1 == null || p1.length <= 0) {
					response = new JSONObject();
					response.put("success", false);
					response.put("message", "not_exist");
					return response.toString();
				}
				p.setProcessName(p1[0].getProcessName());
				p.setSubprocessName(p1[0].getSubprocessName());
				p.setActive(active);
				if (p.update()) {
					response = new JSONObject();
					response.put("success", true);
					response.put("message", "sub_process_updated");
				} else {
					response = new JSONObject();
					response.put("success", false);
					response.put("message", "failed");
				}
			}
		} catch (Exception e) {
			response = new JSONObject();
			response.put("success", false);
			response.put("message", "some exception");
			MessageLog.printError(e);
		}
		return response.toString();
	}

	public String addTools(String request) {
		MessageLog.info("In OrganizationService addTools() request= " + request);
		JSONObject response = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			JSONObject info = (JSONObject) parser.parse(request);
			String operation = info.get("operation") != null ? (String) info.get("operation") : "";
			boolean active = "yes".equalsIgnoreCase(info.get("active").toString())
					|| Boolean.TRUE.equals(info.get("active"));
			String tool_name = info.get("tool_name") != null ? info.get("tool_name").toString().trim() : "";
			String sub_process = info.get("sub_process") != null ? info.get("sub_process").toString().trim() : "";
			if (sub_process.isEmpty()) {
				response = new JSONObject();
				response.put("success", false);
				response.put("message", "subprocess_name_is_manadatory");
				return response.toString();
			}
			if (tool_name.isEmpty()) {
				response = new JSONObject();
				response.put("success", false);
				response.put("message", "tool_name_is_manadatory");
				return response.toString();
			}
			Tools[] t1 = new Tools().retrieveAllWhere("where lower(tool_name)='" + tool_name.toLowerCase()
					+ "' and lower(sub_process)='" + sub_process.toLowerCase() + "'");
			Tools t = new Tools();
			if (operation.equalsIgnoreCase("add")) {
				if (t1 != null && t1.length > 0) {
					response = new JSONObject();
					response.put("success", false);
					response.put("message", "already_exist");
					return response.toString();
				}
				t.setActive(true);
				t.setSubProcess(sub_process);
				t.setToolName(tool_name);
				if (t.insert()) {
					response = new JSONObject();
					response.put("success", true);
					response.put("message", "tool_added");
				} else {
					response = new JSONObject();
					response.put("success", false);
					response.put("message", "failed");
				}
			} else if (operation.equalsIgnoreCase("update")) {
				if (t1 == null || t1.length <= 0) {
					response = new JSONObject();
					response.put("success", false);
					response.put("message", "not_exist");
					return response.toString();
				}
				t.setActive(active);
				t.setSubProcess(sub_process);
				t.setToolName(tool_name);
				if (t.update()) {
					response = new JSONObject();
					response.put("success", true);
					response.put("message", "tool_updated");
				} else {
					response = new JSONObject();
					response.put("success", false);
					response.put("message", "failed");
				}
			}
		} catch (Exception e) {
			response = new JSONObject();
			response.put("success", false);
			response.put("message", "some exception");
			MessageLog.printError(e);
		}
		return response.toString();
	}

	public String addSkills(String request) {
		MessageLog.info("In OrganizationService addSkills() request= " + request);
		JSONObject response = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			JSONObject info = (JSONObject) parser.parse(request);
			String operation = info.get("operation") != null ? (String) info.get("operation") : "";
			boolean active = "yes".equalsIgnoreCase(info.get("active").toString())
					|| Boolean.TRUE.equals(info.get("active"));
			String skill_name = info.get("skill_name") != null ? info.get("skill_name").toString().trim() : "";
			if (skill_name.isEmpty()) {
				response = new JSONObject();
				response.put("success", false);
				response.put("message", "skill_name_is_manadatory");
				return response.toString();
			}
			Skills[] s1 = new Skills().retrieveAllWhere("where lower(skill_name)='" + skill_name.toLowerCase() + "'");
			Skills s = new Skills();
			if (operation.equalsIgnoreCase("add")) {
				if (s1 != null && s1.length > 0) {
					response = new JSONObject();
					response.put("success", false);
					response.put("message", "already_exist");
					return response.toString();
				}
				s.setSkillName(skill_name);
				s.setActive(true);
				if (s.insert()) {
					response = new JSONObject();
					response.put("success", true);
					response.put("message", "skill_added");
				} else {
					response = new JSONObject();
					response.put("success", false);
					response.put("message", "failed");
				}
			} else if (operation.equalsIgnoreCase("update")) {
				if (s1 == null || s1.length <= 0) {
					response = new JSONObject();
					response.put("success", false);
					response.put("message", "skill_name_not_exist");
					return response.toString();
				}
				s.setSkillName(s1[0].getSkillName());
				s.setActive(active);
				if (s.update()) {
					response = new JSONObject();
					response.put("success", true);
					response.put("message", "skill_updated");
				} else {
					response = new JSONObject();
					response.put("success", false);
					response.put("message", "failed");
				}
			}
		} catch (Exception e) {
			response = new JSONObject();
			response.put("success", false);
			response.put("message", "some exception");
			MessageLog.printError(e);
		}
		return response.toString();
	}

	public String deleteProcess(String request) {
		MessageLog.info("In OrganizationService deleteSubProcess() request= " + request);
		JSONObject response = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			JSONObject info = (JSONObject) parser.parse(request);
			Process process = new Process();
			process.setProcessId(Long.parseLong(info.get("process_id").toString()));
			process.setProcessName(info.get("process_name") != null ? info.get("process_name").toString().trim() : "");
			if (process.getProcessName().isEmpty()) {
				response = new JSONObject();
				response.put("success", false);
				response.put("msg", "process_empty");
				return response.toString();
			}
			Process p[] = process.retrieveAllWhere("where process_id='" + process.getProcessId()
					+ "' and lower(process_name)='" + process.getProcessName().toLowerCase() + "'");
			if (p == null || p.length <= 0) {
				response = new JSONObject();
				response.put("success", false);
				response.put("msg", "process_not_exist");
				return response.toString();
			}
			Subprocesses sp[] = new Subprocesses().retrieveAllWhere(
					"where lower(process_name)='" + process.getProcessName().toLowerCase().trim() + "'");
			Tools tool[] = new Tools().retrieveAllWhere(
					"where sub_process in (select subprocess_name from Subprocesses where lower(process_name)='"
							+ process.getProcessName().toLowerCase().trim() + "')");
			if (process.delete(sp, tool)) {
				response.put("success", true);
				response.put("msg", "process_deleted");
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

	public String deleteSubProcess(String request) {
		MessageLog.info("In OrganizationService deleteSubProcess() request= " + request);
		JSONObject response = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			JSONObject info = (JSONObject) parser.parse(request);
			Subprocesses subprocesses = new Subprocesses();
			subprocesses.setSubprocessId(Long.parseLong(info.get("subprocess_id").toString()));
			subprocesses.setSubprocessName(
					info.get("subprocess_name") != null ? info.get("subprocess_name").toString().trim() : "");
			if (subprocesses.getSubprocessName().isEmpty()) {
				response = new JSONObject();
				response.put("success", false);
				response.put("msg", "sub_process_empty");
				return response.toString();
			}
			Subprocesses s[] = subprocesses
					.retrieveAllWhere("where subprocess_name='" + subprocesses.getSubprocessName() + "'");
			if (s == null || s.length <= 0) {
				response = new JSONObject();
				response.put("success", false);
				response.put("msg", "sub_process_not_exist");
				return response.toString();
			}
			Tools tool[] = new Tools().retrieveAllWhere("where sub_process='" + subprocesses.getSubprocessName() + "'");
			if (subprocesses.delete(tool)) {
				response.put("success", true);
				response.put("msg", "subprocess_deleted");
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

	public String deleteTools(String request) {
		MessageLog.info("In OrganizationService deleteTools() request= " + request);
		JSONObject response = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			JSONObject info = (JSONObject) parser.parse(request);
			Tools tool = new Tools();
			tool.setToolId(Long.parseLong(info.get("tool_id").toString()));
			Tools t[] = tool.retrieveAllWhere("where tool_id='" + tool.getToolId() + "'");
			if (t == null || t.length <= 0) {
				response = new JSONObject();
				response.put("success", false);
				response.put("msg", "tool_not_exist");
				return response.toString();
			}
			if (tool.delete()) {
				response.put("success", true);
				response.put("msg", "tool_deleted");
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

	public String deleteSkills(String request) {
		MessageLog.info("In OrganizationService deleteSkills() request= " + request);
		JSONObject response = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			JSONObject info = (JSONObject) parser.parse(request);
			Skills skill = new Skills();
			skill.setSkillId(Long.parseLong(info.get("skill_id").toString()));
			Skills[] s = skill.retrieveAllWhere("where skill_id='" + skill.getSkillId() + "'");
			if (s == null || s.length <= 0) {
				response = new JSONObject();
				response.put("success", false);
				response.put("msg", "skill_not_exist");
				return response.toString();
			}
			if (skill.delete()) {
				response.put("success", true);
				response.put("msg", "skill_deleted");
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
}