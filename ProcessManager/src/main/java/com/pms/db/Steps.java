package com.pms.db;

import javax.persistence.*;

import java.util.List;

import org.hibernate.*;
import com.pms.util.HibernateUtil;
import com.pms.util.MessageLog;

import org.hibernate.query.Query;

@Entity
@Table(name = "steps")
public class Steps {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long step_id;
	private String file_name;
	private String process_name;
	private String subprocess_name;
	private int step_number;
	private String tool_name;
	private String tool_spec;
	private String special_instruction;
	private String skill;
	private int time_minutes;
	private String image_url;
	private Long sheet_process_id;

	public Long getStepId() {
		return step_id;
	}

	public void setStepId(Long stepId) {
		this.step_id = stepId;
	}

	public String getFileName() {
		return file_name;
	}

	public void setFileName(String fileName) {
		this.file_name = fileName;
	}

	public String getProcessName() {
		return process_name;
	}

	public void setProcessName(String processName) {
		this.process_name = processName;
	}

	public String getSubprocessName() {
		return subprocess_name;
	}

	public void setSubprocessName(String subprocessName) {
		this.subprocess_name = subprocessName;
	}

	public int getStepNumber() {
		return step_number;
	}

	public void setStepNumber(int stepNumber) {
		this.step_number = stepNumber;
	}

	public String getToolName() {
		return tool_name;
	}

	public void setToolName(String toolName) {
		this.tool_name = toolName;
	}

	public String getToolSpec() {
		return tool_spec;
	}

	public void setToolSpec(String toolSpec) {
		this.tool_spec = toolSpec;
	}

	public String getSpecialInstruction() {
		return special_instruction;
	}

	public void setSpecialInstruction(String specialInstruction) {
		this.special_instruction = specialInstruction;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public int getTimeMinutes() {
		return time_minutes;
	}

	public void setTimeMinutes(int timeMinutes) {
		this.time_minutes = timeMinutes;
	}

	public String getImageUrl() {
		return image_url;
	}

	public void setImageUrl(String imageUrl) {
		this.image_url = imageUrl;
	}

	public long getSheetProcess() {
		return sheet_process_id;
	}

	public void setSheetProcess(long sheetProcess) {
		this.sheet_process_id = sheetProcess;
	}

	public Steps[] retrieveAllWhere(String condition) {
		MessageLog.info("In steps retrieveAllWhere condition= " + condition);
		Session session = HibernateUtil.pmsSessionFactory.openSession();
		try {
			List<Steps> list = session.createQuery("from Steps " + condition).getResultList();
			return (Steps[]) list.toArray(new Steps[list.size()]);
		} catch (Exception e) {
			MessageLog.printError(e);
			return null;
		} finally {
			session.close();
		}
	}

	public boolean insert() {
		MessageLog.info("In Steps Insert");
		Session session = HibernateUtil.pmsSessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.save(this);
			transaction.commit();
			return true;
		} catch (Exception e) {
			MessageLog.printError(e);
			transaction.rollback();
			return false;
		} finally {
			session.close();
		}
	}

	public boolean update() {
		MessageLog.info("In Steps update");
		Session session = HibernateUtil.pmsSessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Query query = session.createQuery(
					"update Steps set process_name = :process_name, subprocess_name = :subprocess_name, step_number = :step_number, "
							+ "tool_name = :tool_name, tool_spec = :tool_spec, special_instruction = :special_instruction, "
							+ "skill = :skill, time_minutes = :time_minutes, image_url = :image_url where step_id = :step_id");
			query.setParameter("process_name", this.process_name);
			query.setParameter("subprocess_name", this.subprocess_name);
			query.setParameter("step_number", this.step_number);
			query.setParameter("tool_name", this.tool_name);
			query.setParameter("tool_spec", this.tool_spec);
			query.setParameter("special_instruction", this.special_instruction);
			query.setParameter("skill", this.skill);
			query.setParameter("time_minutes", this.time_minutes);
			query.setParameter("image_url", this.image_url);
			query.setParameter("step_id", this.step_id);

			int status = query.executeUpdate();
			transaction.commit();
			return status != 0;
		} catch (Exception e) {
			MessageLog.printError(e);
			transaction.rollback();
			return false;
		} finally {
			session.close();
		}
	}

	public boolean delete() {
		MessageLog.info("In Steps delete");
		Session session = HibernateUtil.pmsSessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Query query = session.createQuery("delete from Steps where step_id = :step_id");
			query.setParameter("step_id", this.step_id);
			int status = query.executeUpdate();
			transaction.commit();
			return status != 0;
		} catch (Exception e) {
			MessageLog.printError(e);
			transaction.rollback();
			return false;
		} finally {
			session.close();
		}
	}
}
