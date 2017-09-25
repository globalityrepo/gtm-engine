package br.com.globality.gtm.engine.common.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "APLIC")
@NamedQueries({ @NamedQuery(name = "Aplicacao.findAll", query = "select t from Aplicacao t") })
@SequenceGenerator(name = "seq_aplicacao", sequenceName = "SQ01_APLIC", initialValue = 1)
public class Aplicacao extends AbstractDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3796406151073051879L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_aplicacao")
	@Column(name = "N_APLIC", nullable = false, unique = true)
	private Long id;

	@Column(name = "C_APLIC", nullable = true, length = 64)
	private String codigo;

	@Column(name = "R_APLIC", nullable = true, length = 512)
	private String descricao;

	@ManyToOne(optional=true, cascade=CascadeType.REFRESH)
	@JoinColumn(name = "N_DIVIS", nullable = true)
	private Divisao divisao;
	
	@Transient
	private boolean selecionada;
	
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
	
	public boolean isSelecionada() {
		return selecionada;
	}

	public void setSelecionada(boolean selecionada) {
		this.selecionada = selecionada;
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
