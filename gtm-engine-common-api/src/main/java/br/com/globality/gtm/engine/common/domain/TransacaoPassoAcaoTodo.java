package br.com.globality.gtm.engine.common.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.globality.gtm.engine.common.domain.compositeId.TransacaoPassoAcaoTodoCompositeId;

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "TRANS_PASSOACAOTODO")
@NamedQueries({ @NamedQuery(name = "TransacaoPassoAcaoTodo.findAll", query = "select t from TransacaoPassoAcaoTodo t") })
public class TransacaoPassoAcaoTodo extends AbstractDomain {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6766892817133700826L;

	@EmbeddedId
	private TransacaoPassoAcaoTodoCompositeId id;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="N_TRANS_PASSO", nullable=false, insertable=false, updatable=false)
	private TransacaoPasso transacaoPasso;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="C_EVNTO_TPO", nullable=false, insertable=false, updatable=false)
	private EventoTipo eventoTipo;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="N_TRANS_EVNTO_INSTN", nullable=false, insertable=false, updatable=false)
	private EventoInstancia eventoInstancia;
	
	@Column(name = "C_TRANS_ACAO_TODO_STTUS", nullable = true, length = 1)
	private String status;
	
	@Column(name = "Q_TRANS_ACAO_TODO_TENTV", nullable = true)
	private Long qtdeTentativas;
	
	@Column(name = "D_TRANS_ACAO_TODO", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	public TransacaoPassoAcaoTodoCompositeId getId() {
		return id;
	}

	public void setId(TransacaoPassoAcaoTodoCompositeId id) {
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

	public EventoInstancia getEventoInstancia() {
		return eventoInstancia;
	}

	public void setEventoInstancia(EventoInstancia eventoInstancia) {
		this.eventoInstancia = eventoInstancia;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getQtdeTentativas() {
		return qtdeTentativas;
	}

	public void setQtdeTentativas(Long qtdeTentativas) {
		this.qtdeTentativas = qtdeTentativas;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
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
		TransacaoPassoAcaoTodo other = (TransacaoPassoAcaoTodo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}