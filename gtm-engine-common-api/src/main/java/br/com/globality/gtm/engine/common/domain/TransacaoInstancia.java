package br.com.globality.gtm.engine.common.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "ISC_TB012_TRA_INSTANCIA")
public class TransacaoInstancia extends AbstractDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4051785710485129L;

	@Id
	@Column(name = "NU_TRA_INSTANCIA", nullable = false, unique = true, length = 36)
	private String id;

	@Column(name = "TS_TRA_INSTANCIA", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	@ManyToOne(optional=true, cascade=CascadeType.REFRESH)
	@JoinColumn(name="NU_TRA_GRUPO", nullable=true)
	private TransacaoGrupo transacaoGrupo;
	
	@ManyToOne(optional=true, cascade=CascadeType.REFRESH)
	@JoinColumn(name="NU_TRA_INS_PAI", nullable=true)
	private TransacaoInstancia transacaoInstanciaPai;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public TransacaoGrupo getTransacaoGrupo() {
		return transacaoGrupo;
	}

	public void setTransacaoGrupo(TransacaoGrupo transacaoGrupo) {
		this.transacaoGrupo = transacaoGrupo;
	}

	public TransacaoInstancia getTransacaoInstanciaPai() {
		return transacaoInstanciaPai;
	}

	public void setTransacaoInstanciaPai(TransacaoInstancia transacaoInstanciaPai) {
		this.transacaoInstanciaPai = transacaoInstanciaPai;
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
		TransacaoInstancia other = (TransacaoInstancia) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
}
