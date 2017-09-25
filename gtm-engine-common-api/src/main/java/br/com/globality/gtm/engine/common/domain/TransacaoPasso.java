package br.com.globality.gtm.engine.common.domain;

import java.util.List;

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
@Table(name = "TRANS_PASSO")
@NamedQueries({ @NamedQuery(name = "TransacaoPasso.findAll", query = "select t from TransacaoPasso t") })
@SequenceGenerator(name = "seq_transacao_passo", sequenceName = "SQ14_TRA_PASSO", initialValue = 1)
public class TransacaoPasso extends AbstractDomain {
	
	private static final long serialVersionUID = 2337198258061138851L;

	@Id
	@Column(name = "N_TRANS_PASSO", nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_transacao_passo")
	private Long id;
	
	@Column(name = "C_TRANS_PASSO", nullable = true, length = 64)
	private String codigo;
	
	@Column(name = "R_TRANS_PASSO", nullable = true, length = 512)
	private String descricao;
		
	@Column(name = "C_EVNTO_INSTN_CONTD", nullable = true, length = 1)
	private String gravarNaBase;
	
	@ManyToOne(optional=true, cascade=CascadeType.REFRESH)
	@JoinColumn(name="N_TRANS", nullable=true)
	private Transacao transacao;
	
	@Transient
	private List<EventoNivel> eventoNiveis;
	
	@Transient
	private List<TransacaoPassoAcao> acoes;
	
	@Transient
	private String gravarNaBaseFormatado;

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

	public String getGravarNaBase() {
		return gravarNaBase;
	}

	public void setGravarNaBase(String gravarNaBase) {
		this.gravarNaBase = gravarNaBase;
	}
	
	public String getGravarNaBaseFormatado() {
		return gravarNaBase!=null && gravarNaBase.equalsIgnoreCase("Y") ? "SIM" : "NÃO";
	}

	public void setGravarNaBaseFormatado(String gravarNaBaseFormatado) {
		this.gravarNaBaseFormatado = gravarNaBaseFormatado;
	}

	public Transacao getTransacao() {
		return transacao;
	}

	public void setTransacao(Transacao transacao) {
		this.transacao = transacao;
	}
	
	public List<EventoNivel> getEventoNiveis() {
		return eventoNiveis;
	}

	public void setEventoNiveis(List<EventoNivel> eventoNiveis) {
		this.eventoNiveis = eventoNiveis;
	}
		
	public List<TransacaoPassoAcao> getAcoes() {
		return acoes;
	}

	public void setAcoes(List<TransacaoPassoAcao> acoes) {
		this.acoes = acoes;
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
		TransacaoPasso other = (TransacaoPasso) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}

	
	
	