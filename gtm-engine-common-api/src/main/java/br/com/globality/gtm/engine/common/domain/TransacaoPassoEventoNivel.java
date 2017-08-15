package br.com.globality.gtm.engine.common.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.globality.gtm.engine.common.domain.compositeId.TransacaoPassoEventoNivelCompositeId;


/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "ISC_TB018_TRA_PAS_EVT_NIVEL")
public class TransacaoPassoEventoNivel extends AbstractDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5101784300193775129L;
	
	@EmbeddedId
	private TransacaoPassoEventoNivelCompositeId id;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="NU_TRA_PASSO", nullable=false, insertable=false, updatable=false)
	private TransacaoPasso transacaoPasso;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="CO_EVT_NIVEL", nullable=false, insertable=false, updatable=false)
	private EventoNivel eventoNivel;
	
	@Column(name = "IC_EVT_INS_CONT", nullable = true, length = 1)
	private String conteudo;
	
	public TransacaoPassoEventoNivelCompositeId getId() {
		return id;
	}

	public void setId(TransacaoPassoEventoNivelCompositeId id) {
		this.id = id;
	}

	public TransacaoPasso getTransacaoPasso() {
		return transacaoPasso;
	}

	public void setTransacaoPasso(TransacaoPasso transacaoPasso) {
		this.transacaoPasso = transacaoPasso;
	}

	public EventoNivel getEventoNivel() {
		return eventoNivel;
	}

	public void setEventoNivel(EventoNivel eventoNivel) {
		this.eventoNivel = eventoNivel;
	}
	
	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransacaoPassoEventoNivel other = (TransacaoPassoEventoNivel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}