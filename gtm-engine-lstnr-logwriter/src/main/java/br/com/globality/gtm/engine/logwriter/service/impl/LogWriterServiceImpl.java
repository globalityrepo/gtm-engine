package br.com.globality.gtm.engine.logwriter.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.quote.ColumnQuoteMode;

import br.com.globality.gtm.engine.common.domain.ConfiguracaoSistema;
import br.com.globality.gtm.engine.common.domain.TransacaoGrupo;
import br.com.globality.gtm.engine.common.exception.BusinessException;
import br.com.globality.gtm.engine.common.exception.ServiceException;
import br.com.globality.gtm.engine.common.exception.ValidationException;
import br.com.globality.gtm.engine.common.type.GTMEnvelopeType;
import br.com.globality.gtm.engine.common.type.TrxInstParametersType;
import br.com.globality.gtm.engine.common.util.CommonUtils;
import br.com.globality.gtm.engine.common.util.MessageResource;
import br.com.globality.gtm.engine.logwriter.dao.GenericDAO;
import br.com.globality.gtm.engine.logwriter.dao.LogWriterDAO;
import br.com.globality.gtm.engine.logwriter.service.LogWriterService;

/**
 * Classe de regras de negócio da tarefa de escrita de arquivo texto da engine do GTM.
 * 
 * @author Leonardo Andrade
 * @version 1.0
 * @since 13/03/2017
 */
@Service("logWriterService")
@Scope("prototype")
public class LogWriterServiceImpl implements LogWriterService {

	private static final Log logger = LogFactory.getLog(LogWriterServiceImpl.class);
	
	@Autowired
	private MessageResource messageResource;
	
	@Autowired
	private GenericDAO genericDAO;
	
	@Autowired
	private LogWriterDAO logWriterDAO;
	
	private final String CONTENT_FOLDER  = "content/";	
	private final String PARAM_FOLDER 	 = "param/";
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, readOnly = true)
	public void execute(String envXml) throws ServiceException, BusinessException {
		try {
			// Converte o XML do envelope para o objeto POJO de Log.
			GTMEnvelopeType envObj = (GTMEnvelopeType) CommonUtils.convertXmlToObj(envXml, GTMEnvelopeType.class);
			
			// Verifica se código do grupo é válido.
			if (!logWriterDAO.hasGrupoByTrxGroupCode(envObj.getTrxGroupCode())) {
				throw new ValidationException(messageResource.getMessage("message.group.code.invalid.error",
						new String[] { envObj.getTrxCode(), envObj.getTrxGroupCode() }, Locale.getDefault()));
			}
			// Verifica se código de transação é válido.
			if (!logWriterDAO.hasTransacaoByTrxCode(envObj.getTrxCode())) {
				throw new ValidationException(messageResource.getMessage("message.trasaction.code.invalid.error",
						new String[] { envObj.getTrxCode(), envObj.getTrxGroupCode() }, Locale.getDefault()));
			}
			// Verifica se relação entre transação e grupo é válida.
			TransacaoGrupo transacaoGrupo = logWriterDAO.findIdTransacaoGrupoByTrxCodeAndTrxGroupCode(envObj.getTrxCode(), envObj.getTrxGroupCode());
			if (transacaoGrupo == null) {
				throw new ValidationException(messageResource.getMessage("message.group.transaction.association.invalid.error",
						new String[] { envObj.getTrxCode(), envObj.getTrxGroupCode() }, Locale.getDefault()));
			}
			// Recupera o caminho principal de escrita de arquivos do sistema.
			final String DIR_PATH = getDirPath();
			
			// Escrita do arquivo de log principal.
			writeTrxLogFile(DIR_PATH, envObj);
			
			// Se houver conteúdo, o mesmo é salvo em arquivo XML.
			if (StringUtils.isNotBlank(envObj.getTrxEventInstContent())) {
				writeTrxContentFile(DIR_PATH, envObj.getTrxInstIdentifier(), envObj.getTrxEventInstContent());
			}
			
			// Se se houver parâmetros, os mesmos são salvos em arquivo XML.
			if (envObj.getTrxInstParameters()!=null 
					&& envObj.getTrxInstParameters().getParameter()!=null
					&& envObj.getTrxInstParameters().getParameter().length>0) {
				writeTrxParamFile(DIR_PATH, envObj.getTrxInstIdentifier(), envObj.getTrxInstParameters());
			}
		}
		catch (ValidationException e) {
			logger.error(e);
			throw new BusinessException(e.getMessage(), e);
		}
		catch (Exception e) {
			logger.error(e);
			throw new ServiceException(messageResource.getMessage("message.erro.ineperado", null, Locale.getDefault()), e);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS, readOnly = true)
	public String getDirPath() throws ServiceException {		
		try {
			// Recuperando o path de gravação em storage.
			ConfiguracaoSistema configApp = findConfiguracaoSistema();
			
			if (StringUtils.isBlank(configApp.getPathStorage())) {
				throw new ValidationException(
						messageResource.getMessage("message.config.pathstorage.notfound.error", null, Locale.getDefault()));
			}
 			
			// Validando e tratando a existência da estrutura principal do path.
			File mainDir = new File(configApp.getPathStorage());
			if (!mainDir.exists()) {
				mainDir.mkdirs();
			}
			
			// Validando e tratando a existência da pasta "content".
			File contentDir = new File(configApp.getPathStorage() + CONTENT_FOLDER);
			if (!contentDir.exists()) {
				contentDir.mkdir();
			}
			
			// Validando e tratando a existência da pasta "paran".
			File paramDir = new File(configApp.getPathStorage() + PARAM_FOLDER);
			if (!paramDir.exists()) {
				paramDir.mkdir();
			}			
			return configApp.getPathStorage();
		}
		catch (Exception e) {
			logger.error(e);
			throw new ServiceException(messageResource.getMessage("message.erro.ineperado", null, Locale.getDefault()), e);
		}		
	}
	
	/**
	* Método para escrita do reguistro da transação no arquivo de log principal CSV
	* 
	* @author Leonardo Andrade
	* @param  DIR_PATH - Path principal de escrita de arquivos do sistema.
	* @param  envObj - Objeto que encapsula os atributos da transação retirados da fila
	* @throws Exception
	*/
	@Override
	public void writeTrxLogFile(final String DIR_PATH, GTMEnvelopeType envObj) throws ServiceException {
		
		// Definição do nome do arquivo de log.		
		final DateFormat DF_FILE   = new SimpleDateFormat("yyyyMMdd");
		final String 	 FILE_NAME = "GTM_" + DF_FILE.format(new Date()) + ".csv";
		
		ICsvListWriter listWriter = null;

		try {			
			// Formatando a linha que será escrita no arquivo de log.
			final DateFormat DF_ROW = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			List<Object> row = Arrays.asList(new Object[] { DF_ROW.format(new Date()),
					CommonUtils.isNullToEmpty(envObj.getTrxInstIdentifier()),
					CommonUtils.isNullToEmpty(envObj.getTrxStpInstIdentifier()),
					CommonUtils.isNullToEmpty(envObj.getTrxEventInstIdentifier()),
					CommonUtils.isNullToEmpty(envObj.getTrxCode()), 
					CommonUtils.isNullToEmpty(envObj.getTrxStpCode()),
					CommonUtils.isNullToEmpty(envObj.getTrxEventLevelCode()),
					CommonUtils.isNullToEmpty(envObj.getTrxEventTypeCode()),
					CommonUtils.isNullToEmpty(envObj.getTrxInstParentIdentifier()),
					(envObj.getTrxEventInstDt() != null
							? new DateTime(envObj.getTrxEventInstDt(), DateTimeZone.getDefault()) : ""),
					CommonUtils.isNullToEmpty(envObj.getTrxEventInstDesc()),
					CommonUtils.isNullToEmpty(envObj.getTrxEventInstParentIdentifier()),
					(StringUtils.isNotBlank(envObj.getTrxEventInstContent()) ? "S" : "N"),
					CommonUtils.isNullToEmpty(envObj.getTrxInstAppCode()),
					(envObj.getTrxInstParameters() != null && envObj.getTrxInstParameters().getParameter()!=null && envObj.getTrxInstParameters().getParameter().length > 0
							? "S" : "N") });
			
			// Configurando colunas com valores entre aspas.
			boolean[] quoteCols = { false, true, true, true, true, true, true, true, true, true, true, true, true, true, true };

			final CsvPreference ALWAYS_QUOTE_NAME_COL = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE)
					.useQuoteMode(new ColumnQuoteMode(quoteCols)).build();

			// Criação do arquivo CSV.
			listWriter = new CsvListWriter(new FileWriter(DIR_PATH + FILE_NAME, true), ALWAYS_QUOTE_NAME_COL);

			// Escrita da linha no arquivo.
			listWriter.write(row);
		} catch (Exception e) {
			logger.error(e);
			throw new ServiceException(messageResource.getMessage("message.erro.ineperado", null, Locale.getDefault()), e);
		} finally {
			if (listWriter != null) {
				try {
					listWriter.close();
				} catch (IOException e1) {
					logger.error(e1);
					throw new ServiceException(messageResource.getMessage("message.erro.ineperado", null, Locale.getDefault()), e1);
				}
			}
		}
		
	}
	
	/**
	* Método para gravação do conteúdo da transação em arquivo XML.
	* 
	* @author Leonardo Andrade
	* @param  DIR_PATH - Path principal de escrita de arquivos do sistema
	* @param  UUID - Identificador da transação
	* @param  trxEventInstContent - Objeto que encapsula o conteúdo da transação
	* @throws JAXBException
	*/
	@Override
	public void writeTrxContentFile(final String DIR_PATH, final String UUID, String trxEventInstContent) throws ServiceException {
		try {
			File file = new File(DIR_PATH + CONTENT_FOLDER + UUID + ".xml");
			PrintWriter writer = new PrintWriter(file, "UTF-8");
		    writer.print(trxEventInstContent);
		    writer.close();
		} 
		catch (IOException e) {
			logger.error(e);
			throw new ServiceException(messageResource.getMessage("message.erro.escrita.arquivo.content.xml", null, Locale.getDefault()), e);
		}
		catch (Exception e) {
			logger.error(e);
			throw new ServiceException(messageResource.getMessage("message.erro.ineperado", null, Locale.getDefault()), e);
		}
	}
	
	/**
	* Método para gravação de parâmetros da transação em arquivo XML.
	* 
	* @author Leonardo Andrade
	* @param  DIR_PATH - Path principal de escrita de arquivos do sistema
	* @param  UUID - Identificador da transação
	* @param  trxInstParameters - Objeto que encapsula a lista a parâmetros
	* @throws JAXBException
	*/
	@Override
	public void writeTrxParamFile(final String DIR_PATH, final String UUID, TrxInstParametersType trxInstParameters) throws ServiceException {
		try {
			File file = new File(DIR_PATH + PARAM_FOLDER + UUID + ".xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(TrxInstParametersType.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(trxInstParameters, file);
		} 
		catch (JAXBException e) {
			logger.error(e);
			throw new ServiceException(messageResource.getMessage("message.erro.conversao.xml", null, Locale.getDefault()), e);
		}
		catch (Exception e) {
			logger.error(e);
			throw new ServiceException(messageResource.getMessage("message.erro.ineperado", null, Locale.getDefault()), e);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS, readOnly = true)
	public ConfiguracaoSistema findConfiguracaoSistema() throws ServiceException {		
		try {
			@SuppressWarnings("unchecked")
			List<ConfiguracaoSistema> lstConfig = (List<ConfiguracaoSistema>) genericDAO.findAll(ConfiguracaoSistema.class);
			if (lstConfig==null || lstConfig.isEmpty()) {
				throw new ValidationException(
						messageResource.getMessage("message.config.notfound.error", null, Locale.getDefault()));
			}
 			return lstConfig.get(0);
		}
		catch (Exception e) {
			logger.error(e);
			throw new ServiceException(messageResource.getMessage("message.erro.ineperado", null, Locale.getDefault()), e);
		}		
	}
			
}
