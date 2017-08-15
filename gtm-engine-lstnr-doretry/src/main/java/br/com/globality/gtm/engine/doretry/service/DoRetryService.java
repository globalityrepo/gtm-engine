package br.com.globality.gtm.engine.doretry.service;

import java.util.List;

import br.com.globality.gtm.engine.common.domain.ConfiguracaoSistema;
import br.com.globality.gtm.engine.common.domain.TransacaoPassoAcaoTodo;
import br.com.globality.gtm.engine.common.exception.BusinessException;
import br.com.globality.gtm.engine.common.exception.ServiceException;

/**
 * gtm-engine-listener
 * @author Leonardo Andrade
 * @since 22/03/2017
 */
public interface DoRetryService {
	
	public void execute(TransacaoPassoAcaoTodo transacaoPassoAcaoToDo) throws ServiceException, BusinessException;
	
	public List<TransacaoPassoAcaoTodo> findRetriesDisponiveis() throws ServiceException;
	
	public ConfiguracaoSistema findConfiguracaoSistema() throws ServiceException;
	
}
