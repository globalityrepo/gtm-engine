package br.com.globality.gtm.engine.web.api.dao;

import java.util.List;

import br.com.globality.gtm.engine.common.exception.DAOException;

/**
 * DAO genérico.
 * 
 * @author Leonardo Andrade
 *
 */
public interface GenericDAO {
	
	public <T> T add(T t) throws DAOException;

	public <T> T update(T t) throws DAOException;

	public <K, T> void delete(Class<T> t, K k) throws DAOException;
	
	public <K, T> T findById(Class<T> t, K k) throws DAOException;
	
	public <T> List<T> findAll(Class<T> t) throws DAOException;
	
}
