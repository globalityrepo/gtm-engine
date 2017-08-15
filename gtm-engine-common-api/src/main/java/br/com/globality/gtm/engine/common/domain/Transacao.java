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
@Table(name = "ISC_TB003_TRANSACAO")
@SequenceGenerator(name = "seq_transacao", sequenceName = "ISC_TB003_TRANSACAO_S", initialValue = 1)
public class Transacao extends AbstractDomain {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5883495206956347910L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_transacao")
	@Column(name = "NU_TRANSACAO", nullable = false, unique = true)
	private Long id;
	
	@Column(name = "CO_TRANSACAO", nullable = true, length = 64)
	private String codigo;
	
	@Column(name = "DE_TRANSACAO", nullable = true, length = 512)
	private String descricao;
	
	@Column(name = "QT_DIA_EVENTO", nullable = true)
	private Long qtdeDiaEvento;
	
	@Column(name = "QT_DIA_CONTEUDO_EVENTO", nullable = true)
	private Long qtdeDiaConteudoEvento;
	
	@Column(name = "IC_TRANSACAO_RESTRICAO", nullable = true, length = 1)
	private String restricao;
	
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

	public Long getQtdeDiaEvento() {
		return qtdeDiaEvento;
	}

	public void setQtdeDiaEvento(Long qtdeDiaEvento) {
		this.qtdeDiaEvento = qtdeDiaEvento;
	}

	public Long getQtdeDiaConteudoEvento() {
		return qtdeDiaConteudoEvento;
	}

	public void setQtdeDiaConteudoEvento(Long qtdeDiaConteudoEvento) {
		this.qtdeDiaConteudoEvento = qtdeDiaConteudoEvento;
	}

	public String getRestricao() {
		return restricao;
	}

	public void setRestricao(String restricao) {
		this.restricao = restricao;
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
		Transacao other = (Transacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
