package br.com.globality.gtm.engine.common.type.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Elemento da mensagem de request da operacao utilizada para gerar uma
 * instancia de evento associada a uma nova instancia de passo de transacao.
 */
@XmlRootElement(name = "AddEventToStepRequest", namespace = "http://www.globality.com.br/gtm/engine/common/request")
@XmlType(
		name = "AddEventToStepRequest", 
		namespace = "http://www.globality.com.br/gtm/engine/common/request",
		propOrder={
				"trxInstIdentifier", "trxStpInstIdentifier", "trxCode", "trxGroupCode", 
				"trxStpCode", "trxEventLevelCode", "trxEventTypeCode", "trxInstParentIdentifier", 
				"trxEventInstDt", "trxEventInstDesc", "trxEventInstParentIdentifier", 
				"trxEventInstContent", "trxInstAppCode", "trxInstParameters"
		}
)
@XmlAccessorType(XmlAccessType.FIELD)
public class AddEventToStepRequest extends AddEventRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3493855727450417118L;

	@XmlElement(name = "TrxInstIdentifier", required = true)
	protected String trxInstIdentifier;
	
	@XmlElement(name = "TrxStpInstIdentifier", required = true)
	protected String trxStpInstIdentifier;

	public String getTrxInstIdentifier() {
		return trxInstIdentifier;
	}

	public void setTrxInstIdentifier(String trxInstIdentifier) {
		this.trxInstIdentifier = trxInstIdentifier;
	}

	public String getTrxStpInstIdentifier() {
		return trxStpInstIdentifier;
	}

	public void setTrxStpInstIdentifier(String trxStpInstIdentifier) {
		this.trxStpInstIdentifier = trxStpInstIdentifier;
	}

}
