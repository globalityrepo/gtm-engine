package br.com.globality.gtm.engine.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Bryan Duarte
 *
 */
@Entity
@Table(name = "EVNTO_NVEL")
public class EventoNivel extends AbstractDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3148007011130592061L;

	@Id
	@Column(name = "C_EVNTO_NVEL", nullable = false, unique = true, length = 1)
	private String id;

	@Column(name = "R_EVNTO_NVEL", nullable = true, length = 512)
	private String descricao;

	@Column(name = "N_CLASS_NVEL_EVNTO", nullable = true)
	private Long ordem;
		
	@Transient
	private boolean selecionado;
	
	@Transient
	private String conteudo;
	
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
	
	public boolean getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
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
