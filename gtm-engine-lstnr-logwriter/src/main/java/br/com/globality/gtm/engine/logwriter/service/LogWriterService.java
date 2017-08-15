package br.com.globality.gtm.engine.logwriter.service;

import br.com.globality.gtm.engine.common.domain.ConfiguracaoSistema;
import br.com.globality.gtm.engine.common.exception.BusinessException;
import br.com.globality.gtm.engine.common.exception.ServiceException;
import br.com.globality.gtm.engine.common.type.GTMEnvelopeType;
import br.com.globality.gtm.engine.common.type.TrxInstParametersType;

/**
 * gtm-engine-listener
 * @author Leonardo Andrade
 * @since 13/03/2017
 */
public interface LogWriterService {
	
	public void execute(String envXml) throws ServiceException, BusinessException;
	
	public String getDirPath() throws ServiceException;
	
	public void writeTrxLogFile(final String DIR_PATH, GTMEnvelopeType envObj) throws ServiceException;
	
	public void writeTrxContentFile(final String DIR_PATH, final String UUID, String trxEventInstContent) throws ServiceException;
	
	public void writeTrxParamFile(final String DIR_PATH, final String UUID, TrxInstParametersType trxInstParameters) throws ServiceException;
	
	public ConfiguracaoSistema findConfiguracaoSistema() throws ServiceException;
	
}
