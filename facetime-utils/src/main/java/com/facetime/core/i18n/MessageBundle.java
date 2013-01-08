package com.facetime.core.i18n;

/**
 * 本地化消息束(keys and values) ，使用<code>MessageBundles</code>创建。
 * <p>可以附着在一个同名的Java类上(非内部类)，<br></>
 * 通常的做法是可以附着在一个包内(给一个包提供一个Messages消息类)
 * 本消息束跟随Java {@link java.util.ResourceBundle}的语义，并有所区别.
 * 
 * @author dzb2k9
 */
public interface MessageBundle {
	/**
	 * Returns true if the bundle contains the named key.
	 */
	boolean contains(String key);

	/**
	 * Returns the localized message for the given key. If catalog does not
	 * contain such a key, then a modified getVersion of the key is returned
	 * (converted to upper case and enclosed in brackets).
	 * 
	 * @param key
	 * @return localized message for key, or placeholder
	 */
	String get(String key);

	/**
	 * Convienience for accessing a formatter and formatting a localized message
	 * with arguments.
	 */
	String format(String key, Object... args);
}
