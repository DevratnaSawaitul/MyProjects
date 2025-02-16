package com.pms.db;

import javax.persistence.*;
import org.hibernate.*;
import org.hibernate.query.Query;

import com.pms.util.HibernateUtil;
import com.pms.util.MessageLog;

import java.util.List;

@Entity
@Table(name = "sheet_process")
public class SheetProcess {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sheet_process_id;
	private String file_name;
	private String process_name;

	public Long getSheetProcessId() {
		return sheet_process_id;
	}

	public void setSheetProcessId(Long sheetProcessId) {
		this.sheet_process_id = sheetProcessId;
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

	public SheetProcess[] retrieveAllWhere(String condition) {
		MessageLog.info("In SheetProcess retrieveAllWhere");
		Session session = HibernateUtil.pmsSessionFactory.openSession();
		try {
			List<SheetProcess> list = session.createQuery("from SheetProcess " + condition).getResultList();
			return list.toArray(new SheetProcess[0]);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	public boolean insert() {
		MessageLog.info("In SheetProcess insert");
		Session session = HibernateUtil.pmsSessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.save(this);
			transaction.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
			return false;
		} finally {
			session.close();
		}
	}

	public boolean delete() {
		MessageLog.info("In SheetProcess delete");
		Session session = HibernateUtil.pmsSessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Query query = session.createQuery("delete from SheetProcess where sheet_process_id = :sheet_process_id");
			query.setParameter("sheet_process_id", this.sheet_process_id);
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

	public boolean delete(Steps[] stepList) {
		MessageLog.info("In Sheets delete");
		Session session = HibernateUtil.pmsSessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			if (stepList != null && stepList.length > 0) {
				for (Steps step : stepList) {
					Query query = session.createQuery("delete from Steps where step_id = :step_id");
					query.setParameter("step_id", step.getStepId());
					query.executeUpdate();
				}
			}
			Query sheetQuery = session
					.createQuery("delete from SheetProcess where sheet_process_id = :sheet_process_id");
			sheetQuery.setParameter("sheet_process_id", this.sheet_process_id);
			int sheetDeleteStatus = sheetQuery.executeUpdate();
			transaction.commit();
			return sheetDeleteStatus != 0;
		} catch (Exception e) {
			MessageLog.printError(e);
			transaction.rollback();
			return false;
		} finally {
			session.close();
		}

	}
}