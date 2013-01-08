package com.facetime.core.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

import com.facetime.core.i18n.impl.MessageBundleImpl;

/**
 * 消息束<code>MessageBundle<code/>工厂类
 * 
 * @author dzb2k9
 */
@SuppressWarnings("rawtypes")
public class MessageBundles {

	public static MessageBundle of(Class messageClass) {
		Locale locale = Locale.getDefault();
		return of(messageClass, locale);
	}

	/**
	 * Obtain the message properties file getName same as class getName
	 * @param messageClass 指定的MessageClass 
	 * @param locale 指定的locale
	 * @return Messages for the class
	 */
	public static MessageBundle of(Class messageClass, Locale locale) {
		String messageName = messageClass.getName();
		int pos;
		if ((pos = messageName.indexOf('$')) > 0) {
			messageName = messageName.substring(0, pos);
		}
		ResourceBundle bundle = ResourceBundle.getBundle(messageName, locale, messageClass.getClassLoader());
		return new MessageBundleImpl(locale, bundle);
	}

	/**
	 * 兼容simple
	 * @param pkg
	 * @return
	 */
	public static MessageBundle of(String pkg) {
		String messageName = pkg + ".message";
		Locale locale = Locale.getDefault();
		ResourceBundle bundle = ResourceBundle.getBundle(messageName, locale, MessageBundles.class.getClassLoader());
		return new MessageBundleImpl(locale, bundle);
	}
}
