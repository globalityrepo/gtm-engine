package br.com.globality.gtm.engine.common.domain.compositeId;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TransacaoPassoAcaoCompositeId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4791597194017164682L;
	
	@Column(name = "NU_TRA_PASSO", nullable = false)
	private Long idTransacaoPasso;
    
	@Column(name = "CO_EVT_TIPO", nullable = false, length = 4)
	private String idEventoTipo;

	public Long getIdTransacaoPasso() {
		return idTransacaoPasso;
	}

	public void setIdTransacaoPasso(Long idTransacaoPasso) {
		this.idTransacaoPasso = idTransacaoPasso;
	}

	public String getIdEventoTipo() {
		return idEventoTipo;
	}

	public void setIdEventoTipo(String idEventoTipo) {
		this.idEventoTipo = idEventoTipo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idEventoTipo == null) ? 0 : idEventoTipo.hashCode());
		result = prime * result + ((idTransacaoPasso == null) ? 0 : idTransacaoPasso.hashCode());
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
		TransacaoPassoAcaoCompositeId other = (TransacaoPassoAcaoCompositeId) obj;
		if (idEventoTipo == null) {
			if (other.idEventoTipo != null)
				return false;
		} else if (!idEventoTipo.equals(other.idEventoTipo))
			return false;
		if (idTransacaoPasso == null) {
			if (other.idTransacaoPasso != null)
				return false;
		} else if (!idTransacaoPasso.equals(other.idTransacaoPasso))
			return false;
		return true;
	}
	
}
