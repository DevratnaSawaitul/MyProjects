package com.pms.db;

import javax.persistence.*;

import java.util.List;

import org.hibernate.*;
import org.hibernate.query.Query;
import com.pms.util.HibernateUtil;
import com.pms.util.MessageLog;

@Entity
@Table(name = "skills")
public class Skills {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long skill_id;
	private String skill_name;
	private Boolean active;

	public Long getSkillId() {
		return skill_id;
	}

	public void setSkillId(Long skillId) {
		this.skill_id = skillId;
	}

	public String getSkillName() {
		return skill_name;
	}

	public void setSkillName(String skillName) {
		this.skill_name = skillName;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Skills[] retrieveAllWhere(String condition) {
		MessageLog.info("In Skills retrieveAllWhere condition= " + condition);
		Session session = HibernateUtil.pmsSessionFactory.openSession();
		try {
			List<Skills> list = session.createQuery("from Skills " + condition).getResultList();
			return (Skills[]) list.toArray(new Skills[list.size()]);
		} catch (Exception e) {
			MessageLog.printError(e);
			return null;
		} finally {
			session.close();
		}
	}

	public boolean insert() {
		MessageLog.info("In Skills Insert");
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
		MessageLog.info("In Skills Update");
		Session session = HibernateUtil.pmsSessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Query query = session.createQuery("update Skills set active = :active where skill_name = :skill_name");
			query.setParameter("skill_name", this.skill_name);
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
		MessageLog.info("In Skills delete");
		Session session = HibernateUtil.pmsSessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Query query = session.createQuery("delete from Skills where skill_id = :skill_id");
			query.setParameter("skill_id", this.skill_id);
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
