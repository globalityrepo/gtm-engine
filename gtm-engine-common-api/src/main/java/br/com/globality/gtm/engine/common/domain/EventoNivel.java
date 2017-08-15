package br.com.globality.gtm.engine.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "ISC_TB010_EVT_NIVEL")
public class EventoNivel extends AbstractDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3848729964218316272L;

	@Id
	@Column(name = "CO_EVT_NIVEL", nullable = false, unique = true, length = 1)
	private String id;

	@Column(name = "DE_EVT_NIVEL", nullable = true, length = 512)
	private String descricao;

	@Column(name = "CLASSE_NIVEL_EVENTO", nullable = true)
	private Long ordem;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getOrdem() {
		return ordem;
	}

	public void setOrdem(Long ordem) {
		this.ordem = ordem;
	}
		
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventoNivel other = (EventoNivel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}