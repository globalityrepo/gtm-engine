package br.com.globality.gtm.engine.doretry.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.tree.AbstractEntity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import br.com.globality.gtm.engine.common.exception.DAOException;
import br.com.globality.gtm.engine.doretry.dao.GenericDAO;

/**
 * gtm-engine-listener
 * @author Leonardo Andrade
 * @since 13/03/2017
 */
@Repository("genericDAO")
@Scope("prototype")
public class GenericDAOImpl implements GenericDAO {
	
	private static final Log logger = LogFactory.getLog(GenericDAOImpl.class);
	
	@PersistenceContext(unitName="br.com.globality.gtm")
	private EntityManager em;

	/**
	 * Adiciona uma entidade ao banco.
	 * 
	 * @param t
	 * @return
	 */
	public <T> T add(T t) throws DAOException {
		try {
			em.persist(t);
			return t;
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage(), e);
		}	
	}

	/**
	 * Atualiza uma entidade
	 * 
	 * @param t
	 * @return
	 */
	public <T> T update(T t) throws DAOException {
		try {
			return em.merge(t);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage(), e);
		}	
	}

	/**
	 * Remove uma entidade
	 * 
	 * @param t
	 * @param k
	 */
	public <K, T> void delete(Class<T> t, K k) throws DAOException  {
		try {
			AbstractEntity obj = (AbstractEntity)em.find(t, k);
			em.remove(obj);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage(), e);
		}	
	}
	
	/**
	 * Busca uma entidade pelo identificador
	 * 
	 * @param t
	 * @param k
	 * @return
	 */
	public <K, T> T findById(Class<T> t, K k) throws DAOException {
		try {
			return em.find(t, k);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage(), e);
		}		
	}
	
	/**
	 * Lista todos os registros da entidade informada.
	 * 
	 * @param t
	 * @return
	 */
	public <T> List<T> findAll(Class<T> t) throws DAOException {
		try {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<T> query = builder.createQuery(t);
			Root<T> root = query.from(t);
			query.select(root);
			TypedQuery<T> q = em.createQuery(query);
			return q.getResultList();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage(), e);
		}	
	}
	
}
