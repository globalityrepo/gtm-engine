package br.com.globality.gtm.engine.common.type;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "EmailEnvelopeType", namespace = "http://www.globality.com.br/gtm/engine/common/type")
@XmlType(name = "EmailEnvelopeType", namespace = "http://www.globality.com.br/gtm/engine/common/type")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmailEnvelopeType implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6843382896407033259L;

	@XmlElement(name = "transacaoPassoId", required = true)
	private Long idTansacaoPasso;
	
	@XmlElement(name = "Destinatario", required = true)
	private String destinatario;
	
	@XmlElement(name = "GTMEnvelopeType", required = true)
	private GTMEnvelopeType gtmEnvelope;
	
	@XmlElement(name = "acao", required = true)
	private int acao;
	
	public Long getIdTansacaoPasso() {
		return idTansacaoPasso;
	}

	public void setIdTansacaoPasso(Long idTansacaoPasso) {
		this.idTansacaoPasso = idTansacaoPasso;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public GTMEnvelopeType getGtmEnvelope() {
		return gtmEnvelope;
	}

	public void setGtmEnvelope(GTMEnvelopeType gtmEnvelope) {
		this.gtmEnvelope = gtmEnvelope;
	}

	public int getAcao() {
		return acao;
	}

	public void setAcao(int acao) {
		this.acao = acao;
	}
	
}
