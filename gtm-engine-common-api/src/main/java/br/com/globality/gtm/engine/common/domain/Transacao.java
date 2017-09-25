package br.com.globality.gtm.engine.common.domain;

import java.util.List;

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
import javax.validation.constraints.NotNull;

/**
 * @author Bryan Duarte
 *
 */
@Entity
@Table(name = "TRANS")
@NamedQueries({ @NamedQuery(name = "Transacao.findAll", query = "select t from Transacao t") })
@SequenceGenerator(name = "seq_transacao", sequenceName = "SQ15_TRANS", initialValue = 1)
public class Transacao extends AbstractDomain {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3180924629857955885L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_transacao")
	@Column(name = "N_TRANS", nullable = false, unique = true)
	private Long id;
	
	@Column(name = "C_TRANS", nullable = true, length = 64)
	@NotNull
	private String codigo;
	
	@Column(name = "R_TRANS", nullable = true, length = 512)
	@NotNull
	private String descricao;
	
	@Column(name = "Q_DIA_EVNTO", nullable = true)
	@NotNull
	private Long qtdeDiaEvento;
	
	@Column(name = "Q_DIA_CONTD_EVNTO", nullable = true)
	@NotNull
	private Long qtdeDiaConteudoEvento;
	
	@Column(name = "C_TRANS_REST", nullable = true, length = 1)
	@NotNull
	private String restricao;
		
	@Transient
	private String qtdeDiaEventoFormatado;
	
	@Transient
	private String qtdeDiaConteudoEventoFormatado;
	
	@Transient
	private String restricaoFormatado;
	
	@Transient
	private String idTransacaoInstancia;
	
	@Transient
	private Long idParametro;
	
	@Transient
	private String valorParametro;
	
	@Transient
	private List<TransacaoParametro> parametros;
	
	@Transient
	private List<TransacaoPasso> passos;
	
	@Transient
	private List<TransacaoGrupo> grupos;
	
	@Transient 
	private boolean selecionado;
	
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
	
	public String getQtdeDiaEventoFormatado() {
		if (qtdeDiaEvento!=null && qtdeDiaEvento > 0L) {
			 if (qtdeDiaEvento > 1L) {
				 return qtdeDiaEvento + " dias";
			 }
			 else {
				 return qtdeDiaEvento + " dia";
			 }
		}
		return "-";
	}

	public void setQtdeDiaEventoFormatado(String qtdeDiaEventoFormatado) {
		this.qtdeDiaEventoFormatado = qtdeDiaEventoFormatado;
	}

	public Long getQtdeDiaConteudoEvento() {
		return qtdeDiaConteudoEvento;
	}

	public void setQtdeDiaConteudoEvento(Long qtdeDiaConteudoEvento) {
		this.qtdeDiaConteudoEvento = qtdeDiaConteudoEvento;
	}
	
	public String getQtdeDiaConteudoEventoFormatado() {
		if (qtdeDiaConteudoEvento!=null && qtdeDiaConteudoEvento > 0L) {
			 if (qtdeDiaConteudoEvento > 1L) {
				 return qtdeDiaConteudoEvento + " dias";
			 }
			 else {
				 return qtdeDiaConteudoEvento + " dia";
			 }
		}
		return "-";
	}

	public void setQtdeDiaConteudoEventoFormatado(String qtdeDiaConteudoEventoFormatado) {
		this.qtdeDiaConteudoEventoFormatado = qtdeDiaConteudoEventoFormatado;
	}

	public String getRestricao() {
		return restricao;
	}

	public void setRestricao(String restricao) {
		this.restricao = restricao;
	}
	
	public String getRestricaoFormatado() {
		return restricao!=null && restricao.equalsIgnoreCase("Y") ? "SIM" : "NÃO";
	}
	
	public void setRestricaoFormatado(String restricaoFormatado) {
		this.restricaoFormatado = restricaoFormatado;
	}
	
	public List<TransacaoParametro> getParametros() {
		return parametros;
	}

	public void setParametros(List<TransacaoParametro> parametros) {
		this.parametros = parametros;
	}

	public String getIdTransacaoInstancia() {
		return idTransacaoInstancia;
	}

	public void setIdTransacaoInstancia(String idTransacaoInstancia) {
		this.idTransacaoInstancia = idTransacaoInstancia;
	}

	public Long getIdParametro() {
		return idParametro;
	}

	public void setIdParametro(Long idParametro) {
		this.idParametro = idParametro;
	}

	public String getValorParametro() {
		return valorParametro;
	}

	public void setValorParametro(String valorParametro) {
		this.valorParametro = valorParametro;
	}

	public List<TransacaoPasso> getPassos() {
		return passos;
	}

	public void setPassos(List<TransacaoPasso> passos) {
		this.passos = passos;
	}

	public List<TransacaoGrupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<TransacaoGrupo> grupos) {
		this.grupos = grupos;
	}

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
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
