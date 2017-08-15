package br.com.globality.gtm.engine.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.xerces.dom.ElementNSImpl;
import org.w3c.dom.Document;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.MQTopic;
import com.ibm.mq.constants.CMQC;
import com.ibm.mq.constants.MQConstants;

public class CommonUtils {
		
	@SuppressWarnings("unchecked")
	public static MQQueueManager initQueueManager() throws MQException {	
		MQEnvironment.hostname = AppConfigBundle.getProperty("mq.hostname");
		MQEnvironment.channel = AppConfigBundle.getProperty("mq.channel");
		MQEnvironment.port = Integer.parseInt(AppConfigBundle.getProperty("mq.port"));
		MQEnvironment.userID = AppConfigBundle.getProperty("mq.userid");
		MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY, MQC.TRANSPORT_MQSERIES_CLIENT);
		return new MQQueueManager(AppConfigBundle.getProperty("mq.qmanager"));
	}
	
	public static String generateUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	public static <T> String convertObjToXml(T t, Class<?> clazz) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(t, baos);
			return baos.toString();
		} 
		catch (Exception e) {
			throw(e);
		} 
		finally {
			try {
				baos.close();
			} 
			catch (IOException ioe) {
				throw(ioe);
			}
		}
	}
	
	public static Object convertXmlToObj(String xml, Class<?> clazz) throws Exception {
		InputStream is = null;
		try {
			// Removendo o header da mensagem.
			String xmlReplaced = StringUtils.replace(xml, xml.substring(0, xml.indexOf("<?xml")), "");
			is = IOUtils.toInputStream(xmlReplaced, "UTF-8");
			JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return jaxbUnmarshaller.unmarshal(is);
		} 
		catch (Exception e) {
			throw(e);
		} 
		finally {
			try {
				is.close();
			} 
			catch (IOException ioe) {
				throw(ioe);
			}
		}
	}
	
	/**
	* Método para escrita em tópico MQ.
	* 
	* @author Leonardo Andrade
	* @param  queueManager - Objeto de conexão ao servidor MQ
	* @param  topicName - Nome do tópico MQ de destino
	* @param  inputMsg - Mensagem que será publicada no tópico
	* @throws MQException.
	*/
	public static void writeTopicMQ(MQQueueManager queueManager, String topicName, String inputMsg) throws MQException {
		MQTopic topic = null;
		try {
			// Acessando o tópico de destino.
			topic = queueManager.accessTopic("", topicName, CMQC.MQTOPIC_OPEN_AS_PUBLICATION, CMQC.MQOO_OUTPUT);
			// Encapsulando a mensagem a ser publicada.
			MQMessage message = new MQMessage();
			message.writeString(inputMsg);
			// Publicando a mensagem no tópico.
			MQPutMessageOptions put_message_options = new MQPutMessageOptions();
			topic.put(message, put_message_options);
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			if (topic!=null)
				topic.close();
		}
	}
	
	/**
	* Método para escrita em fila MQ.
	* 
	* @author Leonardo Andrade
	* @param  queueManager - Objeto de conexão ao servidor MQ
	* @param  queueName - Nome da fila MQ de destino
	* @param  inputMsg - Mensagem que será publicada na fila
	* @throws MQException.
	*/
	public static void writeQueueMQ(MQQueueManager queueManager, String queueName, String inputMsg) throws MQException {
		MQTopic topic = null;
		try {
			// Acessando o tópico de destino.
			MQQueue queue = queueManager.accessQueue( queueName, CMQC.MQOO_OUTPUT+CMQC.MQOO_FAIL_IF_QUIESCING);

	        // Encapsulando a mensagem a ser publicada.
			MQMessage message = new MQMessage();
			message.writeString(inputMsg);
			// Publicando a mensagem no tópico.
			MQPutMessageOptions put_message_options = new MQPutMessageOptions();
			
			queue.put(message, put_message_options);
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			if (topic!=null)
				topic.close();
		}
	}
	
	public static Object isNullToEmpty(Object obj) {
		return obj==null ? "" : obj;
	}
	
	public static String getContentAsString(Object content) throws Exception{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ElementNSImpl elementNSImpl = (ElementNSImpl) content;
	        Document document = elementNSImpl.getOwnerDocument();
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	        transformer.transform(new DOMSource(document),new StreamResult(baos));
			return baos.toString();
		} 
		catch (Exception e) {
			throw(e);
		} 
		finally {
			try {
				baos.close();
			} 
			catch (IOException ioe) {
				throw(ioe);
			}
		}
				
	}
	
	public static String readMQMessageBySyncPoint(MQQueueManager queueManager, String queueName) throws MQException, IOException {

		MQQueue queue = null;
		try {
			queue = queueManager.accessQueue(queueName, CMQC.MQOO_INPUT_AS_Q_DEF);

			MQGetMessageOptions getOptions = new MQGetMessageOptions();
			getOptions.options = CMQC.MQGMO_SYNCPOINT;
			getOptions.waitInterval = MQConstants.MQWI_UNLIMITED;
			
			MQMessage message = new MQMessage();
			try {
				queue.get(message, getOptions);
			}
			catch (MQException e) {
				// Retornar null se não houver mensagem ba fila.
				if (e.getReason()==2033)
					return null;
			}
			byte[] b = new byte[message.getMessageLength()];
			message.readFully(b);
			message.clearMessage();			
			return new String(b);
		} 
		catch (MQException e) {
			throw(e);
		}
		catch (IOException e) {
			throw(e);
		}
		finally {
			if (queue!=null) {
				try {
					queue.close();
				}
				catch (MQException e) {
					throw(e);
				}
			}
		}
	}
	
	public static void changeAppLocale(String locale) {
		if ("en_US".equals(locale)) {
			Locale.setDefault(new Locale("en","US"));
		}
		else if ("es_ES".equals(locale)) {
			Locale.setDefault(new Locale("es","ES"));
		}
		else {
			Locale.setDefault(new Locale("pt","BR"));
		}	
	}

}
