package com.pms.db;

import javax.persistence.*;
import org.hibernate.*;
import org.hibernate.query.Query;
import com.pms.util.HibernateUtil;
import com.pms.util.MessageLog;

import java.util.List;

@Entity
@Table(name = "system_settings")
public class SystemSettings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "key")
	private String key;

	@Column(name = "value")
	private String value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public SystemSettings retrieveByKey(String key, String value) {
		MessageLog.info("In SystemSetting retrieveByKey key= " + key);
		Session session = null;
		try {
			session = HibernateUtil.pmsSessionFactory.openSession();
			Query<SystemSettings> query = session.createQuery(
					"from SystemSettings where lower(key) = lower(:key) and lower(value) = lower(:value)",
					SystemSettings.class);
			query.setParameter("key", key);
			query.setParameter("value", value);
			SystemSettings result = query.uniqueResult();
			return result;
		} catch (Exception e) {
			MessageLog.printError(e);
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public SystemSettings[] retrieveAllWhere(String condition) {
		MessageLog.info("In SystemSettings retrieveAllWhere condition= " + condition);
		Session session = null;
		try {
			session = HibernateUtil.pmsSessionFactory.openSession();
			List<SystemSettings> list = session.createQuery("from SystemSettings " + condition, SystemSettings.class)
					.getResultList();
			return list.toArray(new SystemSettings[0]);
		} catch (Exception e) {
			MessageLog.printError(e);
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public boolean insert() {
		MessageLog.info("In SystemSettings Insert");
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
		MessageLog.info("In SystemSettings Update");
		Session session = HibernateUtil.pmsSessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Query query = session.createQuery("update SystemSettings set value = :value where key = :key");
			query.setParameter("key", this.key);
			query.setParameter("value", this.value);

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