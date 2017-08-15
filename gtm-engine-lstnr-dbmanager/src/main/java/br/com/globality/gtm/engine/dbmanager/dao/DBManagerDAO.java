package br.com.globality.gtm.engine.dbmanager.dao;

import java.util.List;

import br.com.globality.gtm.engine.common.domain.Aplicacao;
import br.com.globality.gtm.engine.common.domain.TransacaoGrupo;
import br.com.globality.gtm.engine.common.domain.TransacaoParametro;
import br.com.globality.gtm.engine.common.domain.TransacaoPasso;
import br.com.globality.gtm.engine.common.domain.TransacaoPassoAcao;
import br.com.globality.gtm.engine.common.domain.TransacaoPassoAcaoTodo;
import br.com.globality.gtm.engine.common.domain.TransacaoPassoEventoNivel;
import br.com.globality.gtm.engine.common.domain.TransacaoPassoInstancia;
import br.com.globality.gtm.engine.common.exception.DAOException;

/**
 * gtm-engine-listener
 * @author Leonardo Andrade
 * @since 13/03/2017
 */
public interface DBManagerDAO {
		
	public Boolean hasTransacaoByTrxCode(String trxCode) throws DAOException;
	
	public Boolean hasGrupoByTrxGroupCode(String trxGroupCode) throws DAOException;
	
	public TransacaoGrupo findIdTransacaoGrupoByTrxCodeAndTrxGroupCode(String trxCode, String trxGroupCode) throws DAOException;
	
	public TransacaoPasso findTransacaoPassoByTrxCodeAndTrxStepCode(String trxCode, String trxStepCode) throws DAOException;
				
	public TransacaoPassoInstancia findTransacaoPassoInstanciaByFiltro(String trxStpInstIdentifier, String trxInstIdentifier) throws DAOException;
	
	public TransacaoPassoEventoNivel findTransacaoPassoEventoNivelByIdTransacaoPassoAndTrxEventLevelCode(
			Long idTransacaoPasso, String trxEventLevelCode) throws DAOException;
		
	public Aplicacao findAplicacaoByTrxInstAppCode(String trxInstAppCode) throws DAOException;
	
	public List<TransacaoParametro> findTransacaoParametroByIdTransacao(Long idTransacao) throws DAOException;
	
	public Long findQtdeRetriesExecutados(Long idTransacaoPasso, String trxStepInstanceIdentifier) throws DAOException;
	
	public List<TransacaoPassoAcaoTodo> findTransacaoPassoAcaoTodoExecutadas(String trxEventTypeCode, Long idTransacaoPasso,
			String trxInstanceIdentifier) throws DAOException;
		
	public List<TransacaoPassoAcao> findTransacaoPassoAcaoByIdTransacaoPassoAndTrxEventTypeCode(Long idTransacaoPasso, 
			String trxEventTypeCode) throws DAOException;
	
	public Boolean hasConteudoByIdTransacaoInstanciaAndIdPassoInstancia(String idTransacaoInstancia, String idPassoInstancia) throws DAOException;
	
}
