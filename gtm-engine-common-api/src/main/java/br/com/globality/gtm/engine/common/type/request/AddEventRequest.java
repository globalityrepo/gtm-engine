package br.com.globality.gtm.engine.common.type.request;

import java.io.Serializable;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import br.com.globality.gtm.engine.common.type.TrxInstParametersType;


/**
 * Elemento da mensagem de request de todas as operacoes.
 */
@XmlTransient
@XmlRootElement(name = "AddEventRequest", namespace = "http://www.globality.com.br/gtm/engine/common/request")
@XmlType(name = "AddEventRequest", namespace = "http://www.globality.com.br/gtm/engine/common/request")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AddEventRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3539717647439951166L;
	
	@XmlElement(name = "TrxCode", required = true)
	protected String trxCode;
	
	@XmlElement(name = "TrxGroupCode", required = true)
	protected String trxGroupCode;

	@XmlElement(name = "TrxStpCode", required = true)
	protected String trxStpCode;

	@XmlElement(name = "TrxEventLevelCode", required = true)
	protected String trxEventLevelCode;

	@XmlElement(name = "TrxEventTypeCode", required = true)
	protected String trxEventTypeCode;

	@XmlElement(name = "TrxInstParentIdentifier", required = false)
	protected String trxInstParentIdentifier;

	@XmlElement(name = "TrxEventInstDt", required = true)
	protected Calendar trxEventInstDt;

	@XmlElement(name = "TrxEventInstDesc", required = false)
	protected String trxEventInstDesc;

	@XmlElement(name = "TrxEventInstParentIdentifier", required = false)
	protected String trxEventInstParentIdentifier;

	@XmlElement(name = "TrxEventInstContent", required = false)
	protected String trxEventInstContent;

	@XmlElement(name = "TrxInstAppCode", required = true)
	protected String trxInstAppCode;
	
	@XmlElement(name = "TrxInstParameters", required = false)
	protected TrxInstParametersType trxInstParameters;
	
	public String getTrxCode() {
		return trxCode;
	}

	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}

	public String getTrxGroupCode() {
		return trxGroupCode;
	}

	public void setTrxGroupCode(String trxGroupCode) {
		this.trxGroupCode = trxGroupCode;
	}

	public String getTrxStpCode() {
		return trxStpCode;
	}

	public void setTrxStpCode(String trxStpCode) {
		this.trxStpCode = trxStpCode;
	}

	public String getTrxEventLevelCode() {
		return trxEventLevelCode;
	}

	public void setTrxEventLevelCode(String trxEventLevelCode) {
		this.trxEventLevelCode = trxEventLevelCode;
	}

	public String getTrxEventTypeCode() {
		return trxEventTypeCode;
	}

	public void setTrxEventTypeCode(String trxEventTypeCode) {
		this.trxEventTypeCode = trxEventTypeCode;
	}

	public String getTrxInstParentIdentifier() {
		return trxInstParentIdentifier;
	}

	public void setTrxInstParentIdentifier(String trxInstParentIdentifier) {
		this.trxInstParentIdentifier = trxInstParentIdentifier;
	}

	public Calendar getTrxEventInstDt() {
		return trxEventInstDt;
	}

	public void setTrxEventInstDt(Calendar trxEventInstDt) {
		this.trxEventInstDt = trxEventInstDt;
	}

	public String getTrxEventInstDesc() {
		return trxEventInstDesc;
	}

	public void setTrxEventInstDesc(String trxEventInstDesc) {
		this.trxEventInstDesc = trxEventInstDesc;
	}

	public String getTrxEventInstParentIdentifier() {
		return trxEventInstParentIdentifier;
	}

	public void setTrxEventInstParentIdentifier(String trxEventInstParentIdentifier) {
		this.trxEventInstParentIdentifier = trxEventInstParentIdentifier;
	}

	public String getTrxEventInstContent() {
		return trxEventInstContent;
	}

	public void setTrxEventInstContent(String trxEventInstContent) {
		this.trxEventInstContent = trxEventInstContent;
	}

	public String getTrxInstAppCode() {
		return trxInstAppCode;
	}

	public void setTrxInstAppCode(String trxInstAppCode) {
		this.trxInstAppCode = trxInstAppCode;
	}

	public TrxInstParametersType getTrxInstParameters() {
		return trxInstParameters;
	}

	public void setTrxInstParameters(TrxInstParametersType trxInstParameters) {
		this.trxInstParameters = trxInstParameters;
	}

}
