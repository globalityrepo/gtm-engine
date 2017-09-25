package br.com.globality.gtm.engine.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Bryan Duarte
 *
 */
@Entity
@Table(name = "GRP")
@NamedQueries({ @NamedQuery(name = "Grupo.findAll", query = "select t from Grupo t") })
@SequenceGenerator(name = "seq_grupo", sequenceName = "SQ16_GRP", initialValue = 1)
public class Grupo extends AbstractDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6953278417112770729L;

	@Id
	@Column(name = "N_GRP", nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_grupo")
	private Long id;

	@Column(name = "C_GRP", nullable = false, length = 64)
	private String codigo;

	@Column(name = "R_GRP", nullable = false, length = 512)
	private String descricao;
	
	@Transient
	private boolean selecionado;
	
	@Transient 
	private Long idTransacaoGrupo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	public Long getIdTransacaoGrupo() {
		return idTransacaoGrupo;
	}

	public void setIdTransacaoGrupo(Long idTransacaoGrupo) {
		this.idTransacaoGrupo = idTransacaoGrupo;
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
		Grupo other = (Grupo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}