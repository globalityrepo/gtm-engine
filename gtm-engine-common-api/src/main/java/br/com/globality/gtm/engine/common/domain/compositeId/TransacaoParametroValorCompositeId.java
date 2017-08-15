package br.com.globality.gtm.engine.common.domain.compositeId;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TransacaoParametroValorCompositeId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6476541176301158111L;

	@Column(name = "NU_PARAMETRO", nullable = false)
	private Long idParametro;
    
	@Column(name = "NU_TRA_EVT_INSTANCIA", nullable = false, length = 36)
	private String idEventoInstancia;

	public Long getIdParametro() {
		return idParametro;
	}

	public void setIdParametro(Long idParametro) {
		this.idParametro = idParametro;
	}
	
	public String getIdEventoInstancia() {
		return idEventoInstancia;
	}

	public void setIdEventoInstancia(String idEventoInstancia) {
		this.idEventoInstancia = idEventoInstancia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idParametro == null) ? 0 : idParametro.hashCode());
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
		TransacaoParametroValorCompositeId other = (TransacaoParametroValorCompositeId) obj;
		if (idParametro == null) {
			if (other.idParametro != null)
				return false;
		} else if (!idParametro.equals(other.idParametro))
			return false;
		return true;
	}
	
}
