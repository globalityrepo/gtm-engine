package br.com.globality.gtm.engine.dbmanager.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.globality.gtm.engine.common.domain.Aplicacao;
import br.com.globality.gtm.engine.common.domain.EventoInstancia;
import br.com.globality.gtm.engine.common.domain.EventoInstanciaConteudo;
import br.com.globality.gtm.engine.common.domain.EventoNivel;
import br.com.globality.gtm.engine.common.domain.EventoTipo;
import br.com.globality.gtm.engine.common.domain.Grupo;
import br.com.globality.gtm.engine.common.domain.Transacao;
import br.com.globality.gtm.engine.common.domain.TransacaoGrupo;
import br.com.globality.gtm.engine.common.domain.TransacaoInstancia;
import br.com.globality.gtm.engine.common.domain.TransacaoParametro;
import br.com.globality.gtm.engine.common.domain.TransacaoPasso;
import br.com.globality.gtm.engine.common.domain.TransacaoPassoAcao;
import br.com.globality.gtm.engine.common.domain.TransacaoPassoAcaoTodo;
import br.com.globality.gtm.engine.common.domain.TransacaoPassoEventoNivel;
import br.com.globality.gtm.engine.common.domain.TransacaoPassoInstancia;
import br.com.globality.gtm.engine.common.exception.DAOException;
import br.com.globality.gtm.engine.dbmanager.dao.DBManagerDAO;

/**
 * gtm-engine-listener
 * @author Leonardo Andrade
 * @since 13/03/2017
 */
@Repository
@Scope("prototype")
public class DBManagerDAOImpl implements DBManagerDAO {
	
	private static final Log logger = LogFactory.getLog(DBManagerDAOImpl.class);
	
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
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public TransacaoPassoInstancia findTransacaoPassoInstanciaByFiltro(String trxStpInstIdentifier, String trxInstIdentifier) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TransacaoPassoInstancia> cq = cb.createQuery(TransacaoPassoInstancia.class);
			Root<TransacaoPassoInstancia> rootEntry = cq.from(TransacaoPassoInstancia.class);
			CriteriaQuery<TransacaoPassoInstancia> all = cq.select(rootEntry);
			Join<TransacaoPassoInstancia, TransacaoInstancia> transacaoInstanciaJoin = rootEntry.join("transacaoInstancia");
			Predicate predicate = cb.equal(transacaoInstanciaJoin.<String>get("id"), trxInstIdentifier);
			predicate = cb.and(predicate, cb.equal(rootEntry.<String>get("id"), trxStpInstIdentifier));
			cq.where(predicate);
			TypedQuery<TransacaoPassoInstancia> query = em.createQuery(all);
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
	public TransacaoPassoEventoNivel findTransacaoPassoEventoNivelByIdTransacaoPassoAndTrxEventLevelCode(
			Long idTransacaoPasso, String trxEventLevelCode) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TransacaoPassoEventoNivel> cq = cb.createQuery(TransacaoPassoEventoNivel.class);
			Root<TransacaoPassoEventoNivel> rootEntry = cq.from(TransacaoPassoEventoNivel.class);
			CriteriaQuery<TransacaoPassoEventoNivel> all = cq.select(rootEntry);
			Join<TransacaoPassoEventoNivel, TransacaoPasso> transacaoPassoJoin = rootEntry.join("transacaoPasso");
			Join<TransacaoPassoEventoNivel, EventoNivel> eventoNivelJoin = rootEntry.join("eventoNivel");
			Predicate predicate = cb.equal(transacaoPassoJoin.<Long>get("id"), idTransacaoPasso);
			predicate = cb.and(predicate, cb.equal(eventoNivelJoin.<String>get("id"), trxEventLevelCode));
			cq.where(predicate);
			TypedQuery<TransacaoPassoEventoNivel> query = em.createQuery(all);
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
	public Aplicacao findAplicacaoByTrxInstAppCode(String trxInstAppCode) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Aplicacao> cq = cb.createQuery(Aplicacao.class);
			Root<Aplicacao> rootEntry = cq.from(Aplicacao.class);
			CriteriaQuery<Aplicacao> all = cq.select(rootEntry);
			Predicate predicate = cb.equal(rootEntry.<String>get("codigo"), trxInstAppCode);
			cq.where(predicate);
			TypedQuery<Aplicacao> query = em.createQuery(all);
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
	public List<TransacaoParametro> findTransacaoParametroByIdTransacao(Long idTransacao) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TransacaoParametro> cq = cb.createQuery(TransacaoParametro.class);
			Root<TransacaoParametro> rootEntry = cq.from(TransacaoParametro.class);
			CriteriaQuery<TransacaoParametro> all = cq.select(rootEntry);
			Join<TransacaoParametro, Transacao> transacaoJoin = rootEntry.join("transacao");
			Predicate predicate = cb.equal(transacaoJoin.<Long>get("id"), idTransacao);
			predicate = cb.and(predicate, cb.equal(rootEntry.<String>get("ativo"), "Y"));
			predicate = cb.and(predicate, cb.isNotNull(rootEntry.<String>get("caminho")));
			predicate = cb.and(predicate, cb.equal(cb.trim(rootEntry.<String>get("caminho")),""));
			cq.where(predicate);
			TypedQuery<TransacaoParametro> query = em.createQuery(all);
			return query.getResultList();
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
	public Long findQtdeRetriesExecutados(Long idTransacaoPasso, String trxInstanceIdentifier) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<TransacaoPassoAcaoTodo> rootEntry = cq.from(TransacaoPassoAcaoTodo.class);
			CriteriaQuery<Long> all = cq.select(cb.max(rootEntry.<Long>get("qtdeTentativas")));
			Join<TransacaoPassoAcaoTodo, EventoInstancia> eventoInstanciaJoin = rootEntry.join("eventoInstancia");
			Join<EventoInstancia, TransacaoPassoInstancia> transacaoPassoInstanciaJoin = eventoInstanciaJoin.join("transacaoPassoInstancia");
			Join<TransacaoPassoInstancia, TransacaoPasso> transacaoPassoJoin = transacaoPassoInstanciaJoin.join("transacaoPasso");
			Predicate predicate = cb.equal(transacaoPassoJoin.<Long>get("id"), idTransacaoPasso);
			predicate = cb.and(predicate, cb.equal(transacaoPassoInstanciaJoin.<String>get("id"), trxInstanceIdentifier));
			cq.where(predicate);
			TypedQuery<Long> query = em.createQuery(all);
			Long result = query.getSingleResult();
			return result == null ? 0l : result;
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
	public List<TransacaoPassoAcaoTodo> findTransacaoPassoAcaoTodoExecutadas(String trxEventTypeCode, Long idTransacaoPasso,
			String trxInstanceIdentifier) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TransacaoPassoAcaoTodo> cq = cb.createQuery(TransacaoPassoAcaoTodo.class);
			Root<TransacaoPassoAcaoTodo> rootEntry = cq.from(TransacaoPassoAcaoTodo.class);
			CriteriaQuery<TransacaoPassoAcaoTodo> all = cq.select(rootEntry);
			Join<TransacaoPassoAcaoTodo, EventoInstancia> eventoInstanciaJoin = rootEntry.join("eventoInstancia");
			Join<EventoInstancia, TransacaoPassoInstancia> transacaoPassoInstanciaJoin = eventoInstanciaJoin.join("transacaoPassoInstancia");
			Join<TransacaoPassoInstancia, TransacaoPasso> transacaoPassoJoin = transacaoPassoInstanciaJoin.join("transacaoPasso");
			Predicate predicate = cb.equal(transacaoPassoJoin.<Long>get("id"), idTransacaoPasso);
			predicate = cb.and(predicate, cb.equal(transacaoPassoInstanciaJoin.<String>get("id"), trxInstanceIdentifier));
			cq.where(predicate);
			TypedQuery<TransacaoPassoAcaoTodo> query = em.createQuery(all);
			return query.getResultList();
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
	public List<TransacaoPassoAcao> findTransacaoPassoAcaoByIdTransacaoPassoAndTrxEventTypeCode(Long idTransacaoPasso, 
			String trxEventTypeCode) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TransacaoPassoAcao> cq = cb.createQuery(TransacaoPassoAcao.class);
			Root<TransacaoPassoAcao> rootEntry = cq.from(TransacaoPassoAcao.class);
			CriteriaQuery<TransacaoPassoAcao> all = cq.select(rootEntry);
			Join<TransacaoPassoAcao, TransacaoPasso> transacaoPassoJoin = rootEntry.join("transacaoPasso");
			Join<TransacaoPassoAcao, EventoTipo> eventoTipoJoin = rootEntry.join("eventoTipo");
			Predicate predicate = cb.equal(transacaoPassoJoin.<Long>get("id"), idTransacaoPasso);
			predicate = cb.and(predicate, cb.equal(eventoTipoJoin.<String>get("id"), trxEventTypeCode));
			cq.where(predicate);
			TypedQuery<TransacaoPassoAcao> query = em.createQuery(all);
			return query.getResultList();
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
	@Transactional(propagation = Propagation.MANDATORY, readOnly = true)
	public Boolean hasConteudoByIdTransacaoInstanciaAndIdPassoInstancia(String idTransacaoInstancia, String idPassoInstancia) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<EventoInstancia> rootEntry = cq.from(EventoInstancia.class);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			Join<EventoInstancia, TransacaoPassoInstancia> transacaoPassoInstanciaJoin = rootEntry.join("transacaoPassoInstancia");
			Join<TransacaoPassoInstancia, TransacaoInstancia> transacaoInstanciaJoin = transacaoPassoInstanciaJoin.join("transacaoInstancia");
			
			// Exists na tabela de Evento Intância Conteúdo.
			Subquery<EventoInstanciaConteudo> subquery = counter.subquery(EventoInstanciaConteudo.class); 
			Root<EventoInstanciaConteudo> eventoInstanciaConteudoSubQuery = subquery.from(EventoInstanciaConteudo.class);  
			subquery.select(eventoInstanciaConteudoSubQuery);
			List<Predicate> subQueryPredicates = new ArrayList<Predicate>(); 
			subQueryPredicates.add(cb.equal(eventoInstanciaConteudoSubQuery.get("id"), rootEntry.get("id")));
			subquery.where(subQueryPredicates.toArray(new Predicate[]{})); 
			
			Predicate predicate = cb.equal(transacaoPassoInstanciaJoin.<String>get("id"), idPassoInstancia);
			predicate = cb.and(predicate, cb.equal(transacaoInstanciaJoin.<String>get("id"), idTransacaoInstancia));
			predicate = cb.and(predicate, cb.exists(subquery));
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
	
}
