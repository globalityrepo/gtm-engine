package br.com.globality.gtm.engine.common.exception;

/**
 * @author Leonardo Andrade
 *
 */
public class BusinessException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7228480358626407344L;

	public BusinessException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
