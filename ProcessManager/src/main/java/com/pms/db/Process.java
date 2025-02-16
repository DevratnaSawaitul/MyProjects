package com.pms.db;

import java.util.List;

import javax.persistence.*;

import org.hibernate.*;
import org.hibernate.query.Query;

import com.pms.util.HibernateUtil;
import com.pms.util.MessageLog;

@Entity
@Table(name = "process")
public class Process {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long process_id;
	private String process_name;
	private Boolean active;

	public Long getProcessId() {
		return process_id;
	}

	public void setProcessId(Long processId) {
		this.process_id = processId;
	}

	public String getProcessName() {
		return process_name;
	}

	public void setProcessName(String processName) {
		this.process_name = processName;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Process[] retrieveAllWhere(String condition) {
		MessageLog.info("In Process retrieveAllWhere condition= " + condition);
		Session session = HibernateUtil.pmsSessionFactory.openSession();
		try {
			List<Process> list = session.createQuery(" from Process " + condition).getResultList();
			return (Process[]) list.toArray(new Process[list.size()]);
		} catch (Exception e) {
			MessageLog.printError(e);
			return null;
		} finally {
			session.close();
		}
	}

	public boolean insert() {
		MessageLog.info("In Process insert()");
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
		MessageLog.info("In Process update()");
		Session session = HibernateUtil.pmsSessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Query query = session.createQuery("update Process set active = :active where process_name = :process_name");
			query.setParameter("active", this.active);
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

	public boolean delete(Subprocesses spList[], Tools tool[]) {
		MessageLog.info("In Process delete");
		Session session = HibernateUtil.pmsSessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			if (spList != null && spList.length > 0) {
				for (Subprocesses sp : spList) {
					Query query = session.createQuery("delete from Subprocesses where subprocess_id = :subprocess_id");
					query.setParameter("subprocess_id", sp.getSubprocessId());
					query.executeUpdate();
				}
			}
			if (tool != null && tool.length > 0) {
				for (Tools sp : tool) {
					Query query = session.createQuery("delete from Tools where tool_id = :tool_id");
					query.setParameter("tool_id", sp.getToolId());
					query.executeUpdate();
				}
			}
			Query sheetQuery = session.createQuery("delete from Process where process_id = :process_id");
			sheetQuery.setParameter("process_id", this.process_id);
			int processDeleteStatus = sheetQuery.executeUpdate();
			transaction.commit();
			return processDeleteStatus != 0;
		} catch (Exception e) {
			MessageLog.printError(e);
			transaction.rollback();
			return false;
		} finally {
			session.close();
		}
	}
}
