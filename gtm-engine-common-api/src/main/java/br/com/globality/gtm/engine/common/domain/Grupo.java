package br.com.globality.gtm.engine.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "ISC_TB007_GRUPO")
@SequenceGenerator(name = "seq_grupo", sequenceName = "ISC_TB007_GRUPO_S", initialValue = 1)
public class Grupo extends AbstractDomain {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4467234167327449496L;

	@Id
	@Column(name = "NU_GRUPO", nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_grupo")
	private Long id;

	@Column(name = "CO_GRUPO", nullable = false, length = 64)
	private String codigo;

	@Column(name = "DE_GRUPO", nullable = false, length = 512)
	private String descricao;
	
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