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
@Table(name = "ISC_TB008_EVT_INSTANCIA")
public class EventoInstancia extends AbstractDomain {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1540474551635030359L;

	@Id
	@Column(name = "NU_TRA_EVT_INSTANCIA", nullable = false, unique = true, length = 36)
	private String id;
		
	@Column(name = "DE_EVT_INSTANCIA", nullable = true,  length = 512)
	private String descricao;
	
	@Column(name = "TS_EVT_INSTANCIA", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
		
	@Column(name = "IC_EVT_INS_CONTEUDO", nullable = true,  length = 1)
	private String conteudo;
		
	@ManyToOne(optional=true, cascade=CascadeType.REFRESH)
	@JoinColumn(name ="NU_TRA_PAS_INSTANCIA", nullable = true)
	private TransacaoPassoInstancia transacaoPassoInstancia;
		
	@ManyToOne(optional=true, cascade=CascadeType.REFRESH)
	@JoinColumn(name = "CO_EVT_NIVEL", nullable = true)
	private EventoNivel eventoNivel;
	
	@ManyToOne(optional=true, cascade=CascadeType.REFRESH)
	@JoinColumn(name="CO_EVT_TIPO", nullable = true)
	private EventoTipo eventoTipo;
	
	@ManyToOne(optional = true, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "NU_APLICACAO", nullable = true)
	private Aplicacao aplicacao;
	
	@ManyToOne
	@JoinColumn(name = "NU_TRA_EVT_INS_PAI", nullable = true)
	private EventoInstancia eventoInstanciaPai;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public TransacaoPassoInstancia getTransacaoPassoInstancia() {
		return transacaoPassoInstancia;
	}

	public void setTransacaoPassoInstancia(TransacaoPassoInstancia transacaoPassoInstancia) {
		this.transacaoPassoInstancia = transacaoPassoInstancia;
	}

	public EventoNivel getEventoNivel() {
		return eventoNivel;
	}

	public void setEventoNivel(EventoNivel eventoNivel) {
		this.eventoNivel = eventoNivel;
	}

	public EventoTipo getEventoTipo() {
		return eventoTipo;
	}

	public void setEventoTipo(EventoTipo eventoTipo) {
		this.eventoTipo = eventoTipo;
	}

	public Aplicacao getAplicacao() {
		return aplicacao;
	}

	public void setAplicacao(Aplicacao aplicacao) {
		this.aplicacao = aplicacao;
	}

	public EventoInstancia getEventoInstanciaPai() {
		return eventoInstanciaPai;
	}

	public void setEventoInstanciaPai(EventoInstancia eventoInstanciaPai) {
		this.eventoInstanciaPai = eventoInstanciaPai;
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
		EventoInstancia other = (EventoInstancia) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}