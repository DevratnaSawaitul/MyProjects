package com.pms.db;

import org.hibernate.query.Query;
import javax.persistence.*;
import org.hibernate.*;

import com.pms.util.HibernateUtil;
import com.pms.util.MessageLog;

import java.util.List;

@Entity
@Table(name = "tools")
public class Tools {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tool_id;
	private String tool_name;
	private String sub_process;
	private Boolean active;

	public Long getToolId() {
		return tool_id;
	}

	public void setToolId(Long toolId) {
		this.tool_id = toolId;
	}

	public String getToolName() {
		return tool_name;
	}

	public void setToolName(String toolName) {
		this.tool_name = toolName;
	}

	public String getSubProcess() {
		return sub_process;
	}

	public void setSubProcess(String subProcess) {
		this.sub_process = subProcess;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Tools[] retrieveAllWhere(String condition) {
		MessageLog.info("In Tools retrieveAllWhere condition= " + condition);
		Session session = HibernateUtil.pmsSessionFactory.openSession();
		try {
			List<Tools> list = session.createQuery(" from Tools " + condition).getResultList();
			return (Tools[]) list.toArray(new Tools[list.size()]);
		} catch (Exception e) {
			MessageLog.printError(e);
			return null;
		} finally {
			session.close();
		}
	}

	public boolean insert() {
		MessageLog.info("In Tools Insert");
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
		MessageLog.info("In Tools Update");
		Session session = HibernateUtil.pmsSessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Query query = session.createQuery("update Tools set active = :active where tool_name = :tool_name and sub_process = :sub_process");
			query.setParameter("tool_name", this.tool_name);
			query.setParameter("sub_process", this.sub_process);
			query.setParameter("active", this.active);

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
		MessageLog.info("In Tools delete");
		Session session = HibernateUtil.pmsSessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Query query = session.createQuery("delete from Tools where tool_id = :tool_id");
			query.setParameter("tool_id", this.tool_id);
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