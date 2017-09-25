package br.com.globality.gtm.engine.common.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "EVNTO_INSTN_CONTD")
@NamedQueries({ @NamedQuery(name = "EventoInstanciaConteudo.findAll", query = "select t from EventoInstanciaConteudo t") })
public class EventoInstanciaConteudo extends AbstractDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4207453230741659800L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "N_TRANS_EVNTO_INSTN", nullable = false, unique = true, length = 36)
	private String id;
	
	@Column(name = "B_TRANS_EVNTO_INSTN_CONTD", columnDefinition = "CLOB", nullable = true)
	@Lob
	private String conteudo;

	@Column(name = "A_TRANS_EVNTO_INSTN_CONTD", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	@ManyToOne(optional = true, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "N_APLIC", nullable = true)
	private Aplicacao aplicacao;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Aplicacao getAplicacao() {
		return aplicacao;
	}

	public void setAplicacao(Aplicacao aplicacao) {
		this.aplicacao = aplicacao;
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
		EventoInstanciaConteudo other = (EventoInstanciaConteudo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
