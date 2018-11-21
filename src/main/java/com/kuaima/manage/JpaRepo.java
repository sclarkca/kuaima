package com.kuaima.manage;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.hql.internal.ast.QueryTranslatorImpl;
import org.hibernate.internal.SessionFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.bstek.bdf3.dorado.jpa.JpaUtil;

@Component
public class JpaRepo {

	private static final Logger logger = LoggerFactory.getLogger(JpaRepo.class);
	@PersistenceContext
	protected EntityManager em;

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	public int getCountByHql(String totalHql) {
		return getCountListByHql(totalHql);
	}

	public List<Object[]> getPagingListByHqlGroupBy(String hql, Integer start, Integer limit) {
		Query query = em.createQuery(hql);
		if (start != null) {
			query.setFirstResult(start.intValue());
		}
		if (limit != null) {
			query.setMaxResults(limit.intValue());
		}
		return (List<Object[]>) query.getResultList();
	}

	public void updateSql(String sql) {
		this.jdbcTemplate.update(sql);
	}

	public int getCountListByHql(String hql) {
		int total = 0;
		try {
			Query query = em.createQuery(hql);
			List<Object> ls = (List<Object>) query.getResultList();
			if (!ls.isEmpty()) {
				total = Integer.valueOf(String.valueOf(ls.get(0)));
			}
		} catch (Exception e) {
			logger.info("Exception", e);
		}
		return total;

	}

	public List<Object> getExportListByHql(String hql) {
		List<Object> ls = new ArrayList<>();
		try {
			Query query = em.createQuery(hql);
			ls = (List<Object>) query.getResultList();
		} catch (Exception e) {
			logger.info("Exception", e);
		}
		return ls;

	}

	public void updateByHql(String hql) {
		try {
			Query query = em.createQuery(hql);
			query.getSingleResult();
		} catch (Exception e) {
			logger.info("Exception", e);
		}
	}

	public void hql2sqlPrint(String hql) {
		Session session = (Session) em.getDelegate();
		SessionFactoryImpl sfi = (SessionFactoryImpl) session.getSessionFactory();
		QueryTranslatorImpl queryTranslator = new QueryTranslatorImpl(hql, hql, Collections.emptyMap(), sfi);
		queryTranslator.compile(Collections.emptyMap(), false);
		System.out.println(queryTranslator.getSQLString());
	}

	public void excuteBySql(String sql) {
		try {
			Query query = JpaUtil.nativeQuery(sql);
			query.getSingleResult();
		} catch (Exception e) {
			logger.info("Exception", e);
		}
	}

	public Long getSumBySql(String sql) {
		Query query = JpaUtil.nativeQuery(sql);
		BigDecimal sum = (BigDecimal) query.getSingleResult();
		return null == sum ? 0 : sum.longValue();
	}

	public Long getCountBySql(String sql) {
		Query query = JpaUtil.nativeQuery(sql);
		BigInteger sum = (BigInteger) query.getSingleResult();
		return null == sum ? 0 : sum.longValue();
	}

	public Object[] getMapBySql(String sql) {
		try {
			Query query = JpaUtil.nativeQuery(sql);
			Object[] rows = (Object[]) query.getSingleResult();
			query.getResultList();
			return rows;
		} catch (Exception e) {
			logger.info("Exception", e);
			return null;
		}
	}

	public List<Object[]> getListBySql(String sql) {
		Query query = JpaUtil.nativeQuery(sql);
		List<Object[]> rows = query.getResultList();
		query.getResultList();
		return rows;
	}

	public Object[] getMapByNativeSql(String sql) {
		try {
			Query query = em.createNativeQuery(sql);
			Object[] row = (Object[]) query.getSingleResult();
			return row;
		} catch (Exception e) {
			logger.info("Exception", e);
			return null;
		}
	}

	public List<Object[]> getListByNativeSql(String sql) {
		try {
			Query query = em.createNativeQuery(sql);
			List<Object[]> rows = query.getResultList();
			return rows;
		} catch (Exception e) {
			logger.info("Exception", e);
			return new ArrayList<>();
		}
	}

	public Long getCountBySqlJdbc(String sql) {
		return jdbcTemplate.queryForObject(sql, null, Long.class);
	}

	public <E> List<E> getPagingListBySql(Class<E> clazz, String sql, int start, int limit) {
		Query query = em.createNativeQuery(sql, clazz);
		query.setFirstResult(start);
		query.setMaxResults(limit);
		return query.getResultList();
	}
	public <E> List<E> getListBySql(Class<E> clazz, String sql) {
		Query query = em.createNativeQuery(sql, clazz);
		return query.getResultList();
	}
}
