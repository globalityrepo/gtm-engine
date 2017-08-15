package br.com.globality.gtm.engine.common.util;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

public class AppConfigBundle {
	
	public static String getProperty(String chave) {	
		try {
			File file = new File(CommonConstants.APP_CONFIG_PATH);
			URL[] urls = {file.toURI().toURL()};
			ClassLoader loader = new URLClassLoader(urls);
			ResourceBundle rb = ResourceBundle.getBundle("application", Locale.getDefault(), loader);
			return rb.getString(chave);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
