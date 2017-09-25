package br.com.globality.gtm.engine.common.domain;

import java.util.Date;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "TRANS_PASSO_INSTN")
@NamedQueries({ @NamedQuery(name = "TransacaoPassoInstancia.findAll", query = "select t from TransacaoPassoInstancia t") })
public class TransacaoPassoInstancia extends AbstractDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6407429389099258293L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "N_TRANS_PASSO_INSTN", nullable = false, unique = true, length = 36)
	private String id;
		
	@ManyToOne(optional=true, cascade=CascadeType.REFRESH)
	@JoinColumn(name="N_TRANS_PASSO", nullable=true)
	private TransacaoPasso transacaoPasso;
	
	@ManyToOne(optional=true, cascade=CascadeType.REFRESH)
	@JoinColumn(name="N_TRANS_INSTN", nullable=true)
	private TransacaoInstancia transacaoInstancia;
		
	@Column(name = "A_TRANS_PASSO_INSTN", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TransacaoPasso getTransacaoPasso() {
		return transacaoPasso;
	}

	public void setTransacaoPasso(TransacaoPasso transacaoPasso) {
		this.transacaoPasso = transacaoPasso;
	}

	public TransacaoInstancia getTransacaoInstancia() {
		return transacaoInstancia;
	}

	public void setTransacaoInstancia(TransacaoInstancia transacaoInstancia) {
		this.transacaoInstancia = transacaoInstancia;
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
		TransacaoPassoInstancia other = (TransacaoPassoInstancia) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
