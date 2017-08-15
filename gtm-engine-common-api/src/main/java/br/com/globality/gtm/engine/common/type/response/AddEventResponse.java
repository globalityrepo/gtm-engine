package br.com.globality.gtm.engine.common.type.response;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Elemento da mensagem de response de todas as operacoes.
 */
@XmlRootElement(name = "AddEventResponse", namespace = "http://www.globality.com.br/gtm/engine/common/response")
@XmlType(name = "AddEventResponse", namespace = "http://www.globality.com.br/gtm/engine/common/response")
@XmlAccessorType(XmlAccessType.FIELD)
public class AddEventResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3198298805756117175L;

	@XmlElement(name = "TrxInstIdentifier", required = true)
	protected String trxInstIdentifier;

	@XmlElement(name = "TrxStpInstIdentifier", required = false)
	protected String trxStpInstIdentifier;

	@XmlElement(name = "TrxEventInstIdentifier", required = false)
	protected String trxEventInstIdentifier;

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

	public String getTrxEventInstIdentifier() {
		return trxEventInstIdentifier;
	}

	public void setTrxEventInstIdentifier(String trxEventInstIdentifier) {
		this.trxEventInstIdentifier = trxEventInstIdentifier;
	}

}
