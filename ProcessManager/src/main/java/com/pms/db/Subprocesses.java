package com.pms.db;

import javax.persistence.*;

import java.util.List;

import org.hibernate.*;
import com.pms.util.HibernateUtil;
import com.pms.util.MessageLog;

import org.hibernate.query.Query;

@Entity
@Table(name = "subprocesses")
public class Subprocesses {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long subprocess_id;
	private String process_name;
	private String subprocess_name;
	private Boolean active = true;

	public Long getSubprocessId() {
		return subprocess_id;
	}

	public void setSubprocessId(Long subprocessId) {
		this.subprocess_id = subprocessId;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Subprocesses[] retrieveAllWhere(String condition) {
		MessageLog.info("In Subprocesses retrieveAllWhere condition= " + condition);
		Session session = HibernateUtil.pmsSessionFactory.openSession();
		try {
			List<Subprocesses> list = session.createQuery("from Subprocesses " + condition).getResultList();
			return (Subprocesses[]) list.toArray(new Subprocesses[list.size()]);
		} catch (Exception e) {
			MessageLog.printError(e);
			return null;
		} finally {
			session.close();
		}
	}

	public boolean insert() {
		MessageLog.info("In Subprocesses Insert");
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
		MessageLog.info("In Subprocesses Update");
		Session session = HibernateUtil.pmsSessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Query query = session.createQuery(
					"update Subprocesses set active = :active where subprocess_name = :subprocess_name and process_name = :process_name");
			query.setParameter("active", this.active);
			query.setParameter("subprocess_name", this.subprocess_name);
			query.setParameter("process_name", this.process_name);

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

	public boolean delete(Tools tool[]) {
		MessageLog.info("In Subprocesses delete");
		Session session = HibernateUtil.pmsSessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			if (tool != null && tool.length > 0) {
				for (Tools toolObj : tool) {
					Query query = session.createQuery("delete from Tools where tool_id = :tool_id");
					query.setParameter("tool_id", toolObj.getToolId());
					query.executeUpdate();
				}
			}
			Query sheetQuery = session.createQuery("delete from Subprocesses where subprocess_id = :subprocess_id");
			sheetQuery.setParameter("subprocess_id", this.subprocess_id);
			int spDeleteStatus = sheetQuery.executeUpdate();
			transaction.commit();
			return spDeleteStatus != 0;
		} catch (Exception e) {
			MessageLog.printError(e);
			transaction.rollback();
			return false;
		} finally {
			session.close();
		}
	}
}