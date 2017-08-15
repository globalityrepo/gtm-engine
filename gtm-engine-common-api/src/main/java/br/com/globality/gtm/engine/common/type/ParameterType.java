package br.com.globality.gtm.engine.common.type;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ParameterType", namespace = "http://www.globality.com.br/gtm/engine/common/type")
@XmlType(name = "ParameterType", namespace = "http://www.globality.com.br/gtm/engine/common/type")
@XmlAccessorType(XmlAccessType.FIELD)
public class ParameterType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7193317926809564738L;

	@XmlElement(name = "Name", required = true)
	protected String name;

	@XmlElement(name = "Value", required = true)
	protected String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
