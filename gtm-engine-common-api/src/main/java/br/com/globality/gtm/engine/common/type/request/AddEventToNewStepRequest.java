package br.com.globality.gtm.engine.common.type.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Elemento da mensagem de request da operacao utilizada para gerar uma
 * instancia de evento associado a uma instancia de passo de transacao
 * existente, associada a uma instancia de transacao existente.
 */
@XmlRootElement(name = "AddEventToNewStepRequest", namespace = "http://www.globality.com.br/gtm/engine/common/request")
@XmlType(
		name = "AddEventToNewStepRequest", 
		namespace = "http://www.globality.com.br/gtm/engine/common/request",
		propOrder={
				"trxInstIdentifier", "trxCode", "trxGroupCode", 
				"trxStpCode", "trxEventLevelCode", "trxEventTypeCode", "trxInstParentIdentifier", 
				"trxEventInstDt", "trxEventInstDesc", "trxEventInstParentIdentifier", 
				"trxEventInstContent", "trxInstAppCode", "trxInstParameters"
		}
)
@XmlAccessorType(XmlAccessType.FIELD)
public class AddEventToNewStepRequest extends AddEventRequest {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6532187081619073224L;
	
	@XmlElement(name = "TrxInstIdentifier", required = true)
	protected String trxInstIdentifier;

	public String getTrxInstIdentifier() {
		return trxInstIdentifier;
	}

	public void setTrxInstIdentifier(String trxInstIdentifier) {
		this.trxInstIdentifier = trxInstIdentifier;
	}
		
}
