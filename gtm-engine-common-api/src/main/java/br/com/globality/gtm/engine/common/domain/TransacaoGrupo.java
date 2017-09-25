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

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "TRANS_GRP")
@NamedQueries({ @NamedQuery(name = "TransacaoGrupo.findAll", query = "select t from TransacaoGrupo t") })
@SequenceGenerator(name = "seq_transacao_grupo", sequenceName = "SQ12_TRA_GRUPO", initialValue = 1)
public class TransacaoGrupo extends AbstractDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8899438330866534742L;

	@Id
	@Column(name = "N_TRANS_GRP", nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_transacao_grupo")
	private Long id;
	
	@ManyToOne(optional=true, cascade=CascadeType.REFRESH)
	@JoinColumn(name="N_TRANS", nullable=true)
	private Transacao transacao;
	
	@ManyToOne(optional=true, cascade=CascadeType.REFRESH)
	@JoinColumn(name="N_GRP", nullable=true)
	private Grupo grupo;
		
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Transacao getTransacao() {
		return transacao;
	}

	public void setTransacao(Transacao transacao) {
		this.transacao = transacao;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
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
		TransacaoGrupo other = (TransacaoGrupo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}