package br.com.globality.gtm.engine.common.domain.compositeId;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TransacaoPassoAcaoTodoCompositeId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9166825101885758891L;

	@Column(name = "NU_TRA_PASSO", nullable = false)
	private Long idTransacaoPasso;
    
	@Column(name = "CO_EVT_TIPO", nullable = false, length = 4)
	private String idEventoTipo;
	
	@Column(name = "NU_TRA_EVT_INSTANCIA", nullable = false, length = 36)
	private String idEventoInstancia;
	
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
		result = prime * result + ((idEventoInstancia == null) ? 0 : idEventoInstancia.hashCode());
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
		TransacaoPassoAcaoTodoCompositeId other = (TransacaoPassoAcaoTodoCompositeId) obj;
		if (idEventoInstancia == null) {
			if (other.idEventoInstancia != null)
				return false;
		} else if (!idEventoInstancia.equals(other.idEventoInstancia))
			return false;
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
