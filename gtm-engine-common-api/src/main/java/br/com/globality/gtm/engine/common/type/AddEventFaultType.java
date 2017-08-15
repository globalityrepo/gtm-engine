package br.com.globality.gtm.engine.common.type;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "AddEventFaultType", namespace = "http://www.globality.com.br/gtm/engine/common/type")
@XmlType(name = "AddEventFaultType", namespace = "http://www.globality.com.br/gtm/engine/common/type")
@XmlAccessorType(XmlAccessType.FIELD)
public class AddEventFaultType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7363799558288069910L;

	@XmlElement(name = "Code", required = true)
	protected String code;

	@XmlElement(name = "Desc", required = true)
	protected String desc;

	public String getCode() {
		return code;
	}

	public void setCode(String param) {
		this.code = param;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String param) {
		this.desc = param;
	}

}
