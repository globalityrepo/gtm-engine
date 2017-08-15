package br.com.globality.gtm.engine.common.type;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "TrxEventInstContentType", namespace = "http://www.globality.com.br/gtm/engine/common/type")
@XmlType(name = "TrxEventInstContentType", namespace = "http://www.globality.com.br/gtm/engine/common/type")
@XmlAccessorType(XmlAccessType.FIELD)
public class TrxEventInstContentType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3080808410135317923L;
	
	@XmlAnyElement
	protected Object extraElement;

	public Object getExtraElement() {
		return extraElement;
	}

	public void setExtraElement(Object extraElement) {
		this.extraElement = extraElement;
	}
	
}
