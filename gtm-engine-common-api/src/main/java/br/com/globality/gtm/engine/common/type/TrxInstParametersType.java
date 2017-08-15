package br.com.globality.gtm.engine.common.type;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "TrxInstParametersType", namespace = "http://www.globality.com.br/gtm/engine/common/type")
@XmlType(name = "TrxInstParametersType", namespace = "http://www.globality.com.br/gtm/engine/common/type")
@XmlAccessorType(XmlAccessType.FIELD)
public class TrxInstParametersType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7490193336276824427L;
	
	@XmlElement(name = "Parameter", required = true)
	protected ParameterType[] parameter;

	public ParameterType[] getParameter() {
		return parameter;
	}

	public void setParameter(ParameterType[] parameter) {
		this.parameter = parameter;
	}
	
}
