package br.com.globality.gtm.engine.common.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.com.globality.gtm.engine.common.domain.compositeId.TransacaoParametroValorCompositeId;

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "TRANS_PARM_VLR")
@NamedQueries({ @NamedQuery(name = "TransacaoParametroValor.findAll", query = "select t from TransacaoParametroValor t") })
public class TransacaoParametroValor extends AbstractDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3064674850296558707L;
	
	@EmbeddedId
	private TransacaoParametroValorCompositeId id;
	
	@Column(name = "R_TRANS_EVNTO_INSTN_VLR", nullable = true, length = 512)
	private String valor;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="N_PARM", nullable=false, insertable=false, updatable=false)
	private TransacaoParametro transacaoParametro;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="N_TRANS_EVNTO_INSTN", nullable=false, insertable=false, updatable=false)
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
