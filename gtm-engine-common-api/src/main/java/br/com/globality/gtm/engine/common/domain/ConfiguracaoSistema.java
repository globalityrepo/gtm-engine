package br.com.globality.gtm.engine.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "CONFG_SIST")
public class ConfiguracaoSistema extends AbstractDomain {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7739944435301122347L;

	@Id
	@Column(name = "N_CONFG", nullable = false, unique = true)
	private Long id;
	
	@Column(name = "C_LOC", nullable = false, length = 30)
	@NotNull
	private String locale;
	
	@Column(name = "C_APRES", nullable = false, length = 30)
	@NotNull
	private String skin;
	
	@Column(name = "R_PATH", nullable = true, length = 80)
	private String pathStorage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	public String getPathStorage() {
		return pathStorage;
	}

	public void setPathStorage(String pathStorage) {
		this.pathStorage = pathStorage;
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
		ConfiguracaoSistema other = (ConfiguracaoSistema) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	
	}