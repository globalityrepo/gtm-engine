package br.com.globality.gtm.engine.common.type.fault;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.ws.WebFault;

@WebFault(
	name = "AddEventMessageFault", 
	targetNamespace = "http://www.globality.com.br/gtm/engine/common/fault"
)
@XmlRootElement(name = "AddEventMessageFault", namespace = "http://www.globality.com.br/gtm/engine/common/fault")
@XmlType(name = "AddEventMessageFault", namespace = "http://www.globality.com.br/gtm/engine/common/fault")
@XmlAccessorType(XmlAccessType.FIELD)
public class AddEventMessageFault extends Exception {

	private static final long serialVersionUID = 1487080184269L;
	
	public AddEventMessageFault() {
		super("AddEventMessageFault");
	}

	public AddEventMessageFault(java.lang.String s) {
		super(s);
	}

	public AddEventMessageFault(java.lang.String s, java.lang.Throwable ex) {
		super(s, ex);
	}

	public AddEventMessageFault(java.lang.Throwable cause) {
		super(cause);
	}
	
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
