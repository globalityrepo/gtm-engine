package br.com.globality.gtm.engine.common.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.globality.gtm.engine.common.domain.compositeId.TransacaoPassoAcaoCompositeId;

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "TRANS_PASSO_ACAO")
@NamedQueries({ @NamedQuery(name = "TransacaoPassoAcao.findAll", query = "select t from TransacaoPassoAcao t") })
public class TransacaoPassoAcao extends AbstractDomain {

	private static final long serialVersionUID = 2337198258061138851L;
	
	@EmbeddedId
	private TransacaoPassoAcaoCompositeId id;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="N_TRANS_PASSO", nullable=false, insertable=false, updatable=false)
	private TransacaoPasso transacaoPasso;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="C_EVNTO_TPO", nullable=false, insertable=false, updatable=false)
	private EventoTipo eventoTipo;
	
	@Column(name = "Q_INTVL", nullable = true)
	private Long intervalo;
	
	@Column(name = "Q_TENTV", nullable = true)
	private Long qtdeTentativas;
	
	@Column(name = "R_FILA_DSTNO", nullable = true, length = 512)
	private String filaDestino;
	
	@Column(name = "R_DSTNA", nullable = true, length = 512)
	private String destinatario;
	
	@Column(name = "C_REENV", nullable = true, length = 1)
	private String reenvio;
	
	@Column(name = "R_TRANS_NRO_CMNHO", nullable = true, length = 512)
	private String xpath;
	
	@Column(name = "R_TRANS_NRO_ENDER", nullable = true, length = 512)
	private String namespace;
	
	@Column(name = "R_TRANS_NRO_PREFX", nullable = true, length = 512)
	private String prefixoNameSpace;
	
	@Transient
	private String reenvioFormatado;
	
	@Transient 
	private Long idFake;
	
	public TransacaoPassoAcaoCompositeId getId() {
		return id;
	}
	
	public void setId(TransacaoPassoAcaoCompositeId id) {
		this.id = id;
	}

	public TransacaoPasso getTransacaoPasso() {
		return transacaoPasso;
	}
	
	public void setTransacaoPasso(TransacaoPasso transacaoPasso) {
		this.transacaoPasso = transacaoPasso;
	}

	public EventoTipo getEventoTipo() {
		return eventoTipo;
	}

	public void setEventoTipo(EventoTipo eventoTipo) {
		this.eventoTipo = eventoTipo;
	}

	public Long getIntervalo() {
		return intervalo;
	}

	public void setIntervalo(Long intervalo) {
		this.intervalo = intervalo;
	}

	public Long getQtdeTentativas() {
		return qtdeTentativas;
	}

	public void setQtdeTentativas(Long qtdeTentativas) {
		this.qtdeTentativas = qtdeTentativas;
	}

	public String getFilaDestino() {
		return filaDestino;
	}

	public void setFilaDestino(String filaDestino) {
		this.filaDestino = filaDestino;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getReenvio() {
		return reenvio;
	}

	public void setReenvio(String reenvio) {
		this.reenvio = reenvio;
	}

	public String getReenvioFormatado() {
		return reenvio!=null && reenvio.equalsIgnoreCase("Y") ? "SIM" : "NÃO";
	}

	public void setReenvioFormatado(String reenvioFormatado) {
		this.reenvioFormatado = reenvioFormatado;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getPrefixoNameSpace() {
		return prefixoNameSpace;
	}

	public void setPrefixoNameSpace(String prefixoNameSpace) {
		this.prefixoNameSpace = prefixoNameSpace;
	}
	
	public Long getIdFake() {
		return idFake;
	}

	public void setIdFake(Long idFake) {
		this.idFake = idFake;
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
		TransacaoPassoAcao other = (TransacaoPassoAcao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}