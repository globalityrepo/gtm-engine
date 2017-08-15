package br.com.globality.gtm.engine.web.api.dao;

import br.com.globality.gtm.engine.common.domain.TransacaoPassoAcao;
import br.com.globality.gtm.engine.common.domain.TransacaoPassoAcaoTodo;
import br.com.globality.gtm.engine.common.exception.DAOException;

public interface ResubDAO {

	public TransacaoPassoAcao findTransacaoPassoAcao(Long idTransacaoPasso, String codigoEventoTipo) throws DAOException;

	public TransacaoPassoAcaoTodo findTransacaoPassoAcaoTodo(Long idTransacaoPasso, String codigoEventoTipo, String idEventoInstancia) throws DAOException;

}
