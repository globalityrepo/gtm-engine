package br.com.globality.gtm.engine.web.api.dao.impl;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.dom4j.tree.AbstractEntity;

import br.com.globality.gtm.engine.common.exception.DAOException;
import br.com.globality.gtm.engine.web.api.dao.GenericDAO;

/**
 * DAO genérico.
 * 
 * @author Leonardo Andrade
 *
 */
@Stateless
@LocalBean
public class GenericDAOImpl implements GenericDAO {

	@PersistenceContext(unitName="br.com.globality.gtm")
	private EntityManager em;

	@Override
	public <T> T add(T t) throws DAOException {
		try {
			em.persist(t);
			return t;
		}
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}	
	}

	@Override
	public <T> T update(T t) throws DAOException {
		try {
			return em.merge(t);
		}
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}	
	}

	@Override
	public <K, T> void delete(Class<T> t, K k) throws DAOException  {
		try {
			AbstractEntity obj = (AbstractEntity)em.find(t, k);
			em.remove(obj);
		}
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}	
	}
	
	@Override
	public <K, T> T findById(Class<T> t, K k) throws DAOException {
		try {
			return em.find(t, k);
		}
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}		
	}
	
	public <T> List<T> findAll(Class<T> t) throws DAOException {
		try {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<T> query = builder.createQuery(t);
			Root<T> root = query.from(t);
			query.select(root);TypedQuery<T> q = em.createQuery(query);
			return q.getResultList();
		}
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}	
	}
	
}
