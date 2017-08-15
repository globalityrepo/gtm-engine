package br.com.globality.gtm.engine.common.domain.compositeId;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TransacaoPassoEventoNivelCompositeId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1672571398040781807L;

	@Column(name = "NU_TRA_PASSO", nullable = false)
	private Long idTransacaoPasso;
    
	@Column(name = "CO_EVT_NIVEL", nullable = false, length = 1)
	private String idEventoNivel;
	
    public Long getIdTransacaoPasso() {
		return idTransacaoPasso;
	}
	
	public void setIdTransacaoPasso(Long idTransacaoPasso) {
		this.idTransacaoPasso = idTransacaoPasso;
	}
	
	public String getIdEventoNivel() {
		return idEventoNivel;
	}
	
	public void setIdEventoNivel(String idEventoNivel) {
		this.idEventoNivel = idEventoNivel;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idEventoNivel == null) ? 0 : idEventoNivel.hashCode());
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
		TransacaoPassoEventoNivelCompositeId other = (TransacaoPassoEventoNivelCompositeId) obj;
		if (idEventoNivel == null) {
			if (other.idEventoNivel != null)
				return false;
		} else if (!idEventoNivel.equals(other.idEventoNivel))
			return false;
		if (idTransacaoPasso == null) {
			if (other.idTransacaoPasso != null)
				return false;
		} else if (!idTransacaoPasso.equals(other.idTransacaoPasso))
			return false;
		return true;
	}
    
}
