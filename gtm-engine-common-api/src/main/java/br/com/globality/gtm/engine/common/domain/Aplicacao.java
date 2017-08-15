package br.com.globality.gtm.engine.common.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "ISC_TB001_APLICACAO")
@SequenceGenerator(name = "seq_aplicacao", sequenceName = "ISC_TB001_APLICACAO_S", initialValue = 1)
public class Aplicacao extends AbstractDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8447576111117282680L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_aplicacao")
	@Column(name = "NU_APLICACAO", nullable = false, unique = true)
	private Long id;

	@Column(name = "CO_APLICACAO", nullable = true, length = 64)
	private String codigo;

	@Column(name = "DE_APLICACAO", nullable = true, length = 512)
	private String descricao;

	@ManyToOne(optional=true, cascade=CascadeType.REFRESH)
	@JoinColumn(name = "NU_DIVISAO", nullable = true)
	private Divisao divisao;
	
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

	public Divisao getDivisao() {
		return divisao;
	}

	public void setDivisao(Divisao divisao) {
		this.divisao = divisao;
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
		Aplicacao other = (Aplicacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
