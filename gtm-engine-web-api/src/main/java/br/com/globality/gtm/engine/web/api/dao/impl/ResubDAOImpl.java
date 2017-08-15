package br.com.globality.gtm.engine.web.api.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.globality.gtm.engine.common.domain.EventoInstancia;
import br.com.globality.gtm.engine.common.domain.EventoTipo;
import br.com.globality.gtm.engine.common.domain.TransacaoPasso;
import br.com.globality.gtm.engine.common.domain.TransacaoPassoAcao;
import br.com.globality.gtm.engine.common.domain.TransacaoPassoAcaoTodo;
import br.com.globality.gtm.engine.common.exception.DAOException;
import br.com.globality.gtm.engine.web.api.dao.ResubDAO;

@Stateless
@LocalBean
public class ResubDAOImpl implements ResubDAO {

	@PersistenceContext(unitName="br.com.globality.gtm")
	private EntityManager em;
	
	@Override
	public TransacaoPassoAcao findTransacaoPassoAcao(Long idTransacaoPasso, String codigoEventoTipo) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TransacaoPassoAcao> cq = cb.createQuery(TransacaoPassoAcao.class);
			Root<TransacaoPassoAcao> rootEntry = cq.from(TransacaoPassoAcao.class);
			CriteriaQuery<TransacaoPassoAcao> all = cq.select(rootEntry);
			Join<TransacaoPassoAcao, TransacaoPasso> transacaoPassoJoin = rootEntry.join("transacaoPasso");
			Join<TransacaoPassoAcao, EventoTipo> eventoTipoJoin = rootEntry.join("eventoTipo");
			List<Predicate> pradicates = new ArrayList<Predicate>(); 		
			pradicates.add(cb.equal(transacaoPassoJoin.get("id"), idTransacaoPasso));
			pradicates.add(cb.equal(eventoTipoJoin.get("id"), codigoEventoTipo));
			cq.where(pradicates.toArray(new Predicate[]{})); 
			TypedQuery<TransacaoPassoAcao> allQuery = em.createQuery(all);
			return allQuery.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}

	@Override
	public TransacaoPassoAcaoTodo findTransacaoPassoAcaoTodo(Long idTransacaoPasso, String codigoEventoTipo, String idEventoInstancia) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TransacaoPassoAcaoTodo> cq = cb.createQuery(TransacaoPassoAcaoTodo.class);
			Root<TransacaoPassoAcaoTodo> rootEntry = cq.from(TransacaoPassoAcaoTodo.class);
			CriteriaQuery<TransacaoPassoAcaoTodo> all = cq.select(rootEntry);
			Join<TransacaoPassoAcaoTodo, TransacaoPasso> transacaoPassoJoin = rootEntry.join("transacaoPasso");
			Join<TransacaoPassoAcaoTodo, EventoTipo> eventoTipoJoin = rootEntry.join("eventoTipo");
			Join<TransacaoPassoAcaoTodo, EventoInstancia> eventoInstanciaJoin = rootEntry.join("eventoInstancia");
			List<Predicate> predicates = new ArrayList<Predicate>(); 		
			predicates.add(cb.equal(transacaoPassoJoin.get("id"), idTransacaoPasso));
			predicates.add(cb.equal(eventoTipoJoin.get("id"), codigoEventoTipo));
			predicates.add(cb.equal(eventoInstanciaJoin.get("id"), idEventoInstancia));
			predicates.add(cb.equal(rootEntry.<String>get("status"), "N"));
			predicates.add(cb.equal(rootEntry.<Long>get("qtdeTentativas"), 0l));
			cq.where(predicates.toArray(new Predicate[]{})); 
			TypedQuery<TransacaoPassoAcaoTodo> allQuery = em.createQuery(all);
			return allQuery.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}

}
