package br.com.globality.gtm.engine.common.type.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Elemento da mensagem de request da operacao utilizada para gerar uma
 * instancia de evento associado a uma instancia de passo de transacao
 * existente, associada a uma nova instancia de transacao.
 */
@XmlRootElement(name = "AddEventToNewTransactionRequest", namespace = "http://www.globality.com.br/gtm/engine/common/request")
@XmlType(name = "AddEventToNewTransactionRequest", namespace = "http://www.globality.com.br/gtm/engine/common/request")
@XmlAccessorType(XmlAccessType.FIELD)
public class AddEventToNewTransactionRequest extends AddEventRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1521964023041874492L;
	
}
