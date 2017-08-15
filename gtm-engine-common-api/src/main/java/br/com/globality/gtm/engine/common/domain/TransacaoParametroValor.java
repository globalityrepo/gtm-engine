package br.com.globality.gtm.engine.common.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.globality.gtm.engine.common.domain.compositeId.TransacaoParametroValorCompositeId;

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "ISC_TB014_TRA_PAR_VALOR")
public class TransacaoParametroValor extends AbstractDomain {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7478699832758817288L;

	@EmbeddedId
	private TransacaoParametroValorCompositeId id;
	
	@Column(name = "TXT_TRA_EVT_INS_VALOR", nullable = true, length = 512)
	private String valor;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="NU_PARAMETRO", nullable=false, insertable=false, updatable=false)
	private TransacaoParametro transacaoParametro;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="NU_TRA_EVT_INSTANCIA", nullable=false, insertable=false, updatable=false)
	private EventoInstancia eventoInstancia;
	
	public TransacaoParametroValorCompositeId getId() {
		return id;
	}

	public void setId(TransacaoParametroValorCompositeId id) {
		this.id = id;
	}

	public TransacaoParametro getTransacaoParametro() {
		return transacaoParametro;
	}

	public void setTransacaoParametro(TransacaoParametro transacaoParametro) {
		this.transacaoParametro = transacaoParametro;
	}

	public EventoInstancia getEventoInstancia() {
		return eventoInstancia;
	}

	public void setEventoInstancia(EventoInstancia eventoInstancia) {
		this.eventoInstancia = eventoInstancia;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
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
		TransacaoParametroValor other = (TransacaoParametroValor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
