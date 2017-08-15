package br.com.globality.gtm.engine.logwriter.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.globality.gtm.engine.common.domain.Grupo;
import br.com.globality.gtm.engine.common.domain.Transacao;
import br.com.globality.gtm.engine.common.domain.TransacaoGrupo;
import br.com.globality.gtm.engine.common.domain.TransacaoPasso;
import br.com.globality.gtm.engine.common.exception.DAOException;
import br.com.globality.gtm.engine.logwriter.dao.LogWriterDAO;

/**
 * gtm-engine-listener
 * @author Leonardo Andrade
 * @since 25/04/2017
 */
@Repository
@Scope("prototype")
public class LogWriterDAOImpl implements LogWriterDAO {
	
	private static final Log logger = LogFactory.getLog(LogWriterDAOImpl.class);
	
	@PersistenceContext(unitName="br.com.globality.gtm")
	private EntityManager em;
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Boolean hasTransacaoByTrxCode(String trxCode) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Transacao> rootEntry = cq.from(Transacao.class);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			Predicate predicate = cb.equal(rootEntry.get("codigo"), trxCode);
			cq.where(predicate);
			TypedQuery<Long> query = em.createQuery(counter);
			return (query.getSingleResult()>0) ? Boolean.TRUE : Boolean.FALSE;
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Boolean hasGrupoByTrxGroupCode(String trxGroupCode) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Grupo> rootEntry = cq.from(Grupo.class);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			Predicate predicate = cb.equal(rootEntry.get("codigo"), trxGroupCode);
			cq.where(predicate);
			TypedQuery<Long> query = em.createQuery(counter);
			return (query.getSingleResult()>0) ? Boolean.TRUE : Boolean.FALSE;
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public TransacaoGrupo findIdTransacaoGrupoByTrxCodeAndTrxGroupCode(String trxCode, String trxGroupCode) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TransacaoGrupo> cq = cb.createQuery(TransacaoGrupo.class);
			Root<TransacaoGrupo> rootEntry = cq.from(TransacaoGrupo.class);
			CriteriaQuery<TransacaoGrupo> all = cq.select(rootEntry);
			Join<TransacaoGrupo, Transacao> transacaoJoin = rootEntry.join("transacao");
			Join<TransacaoGrupo, Grupo> grupoJoin = rootEntry.join("grupo");
			Predicate predicate = cb.equal(transacaoJoin.get("codigo"), trxCode);
			predicate = cb.and(predicate, cb.equal(grupoJoin.get("codigo"), trxGroupCode));
			cq.where(predicate);
			TypedQuery<TransacaoGrupo> query = em.createQuery(all);
			return query.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public TransacaoPasso findTransacaoPassoByTrxCodeAndTrxStepCode(String trxCode, String trxStepCode) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TransacaoPasso> cq = cb.createQuery(TransacaoPasso.class);
			Root<TransacaoPasso> rootEntry = cq.from(TransacaoPasso.class);
			CriteriaQuery<TransacaoPasso> all = cq.select(rootEntry);
			Join<TransacaoPasso, Transacao> transacaoJoin = rootEntry.join("transacao");
			Predicate predicate = cb.equal(transacaoJoin.get("codigo"), trxCode);
			predicate = cb.and(predicate, cb.equal(rootEntry.get("codigo"), trxStepCode));
			cq.where(predicate);
			TypedQuery<TransacaoPasso> query = em.createQuery(all);
			return query.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage(), e);
		}
	}
		
}
