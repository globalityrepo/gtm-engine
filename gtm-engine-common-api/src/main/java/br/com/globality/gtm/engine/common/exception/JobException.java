package br.com.globality.gtm.engine.common.exception;

/**
 * @author Leonardo Andrade
 *
 */
public class JobException extends Exception {

	private static final long serialVersionUID = -5060844520321022030L;

	public JobException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
