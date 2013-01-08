package com.facetime.core.i18n.impl;

import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import com.facetime.core.collection.CaseInsensitiveMap;
import com.facetime.core.i18n.MessageBundle;

/**
 * MessageBundle的具体实现<BR></>
 *
 * Implementation of {@link MessageBundle} based around a {@link java.util.ResourceBundle}.
 */
public class MessageBundleImpl extends AbstractMessageBundle {
	
    private final Map<String, String> properties = new CaseInsensitiveMap<String>();

    public MessageBundleImpl(Locale locale, ResourceBundle bundle) {
        super(locale);
        // Our best (threadsafe) chance to determine all the available keys.
        Enumeration<String> e = bundle.getKeys();
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            String value = bundle.getString(key);
            properties.put(key, value);
        }
    }

    public MessageBundleImpl(Locale locale, ResourceBundle[] bundles) {
    	 super(locale);
    	 for (ResourceBundle bundle : bundles) {
	         Enumeration<String> e = bundle.getKeys();
	         while (e.hasMoreElements()) {
	             String key = e.nextElement();
	             String value = bundle.getString(key);
	             properties.put(key, value);
	         }
    	 }
    }
    
    @Override
    protected String value(String key) {
        return properties.get(key);
    }

//    /**
//     * Finds the messages for a given Messages utility class. Strings the trailing "Messages" and replaces it with
//     * "Strings" to form the base path. Loads the bundle using the default locale, and the class' class loader.
//     * （只为兼容T5)
//     * @param clazz
//     * @return Messages for the class
//     */
//    public static MessageBundle forClass(Class clazz) {
//        String className = clazz.getName();
//        String messageName = className.replaceAll("Messages$", "Strings");
//        //class getName without Messages suffix we fill Strings default - fill by dzb
//        if (messageName.indexOf("Strings") < 0 ) {
//            messageName += "Strings";
//        }
//        Locale locale = Locale.getDefault();
//        ResourceBundle bundle = ResourceBundle.getBundle(messageName, locale, clazz.getClassLoader());
//        return new MessageBundleImpl(locale, bundle);
//    }
//
//    /**
//     * Obtain the message properties file getName same as class getName
//     * 同名资源文件
//     * @param messageClass
//     * @return Messages for the class
//     */
//    public static MessageBundle of(Class messageClass) {
//        Locale locale = Locale.getDefault();
//        return of(messageClass, locale);
//    }
//
//    /**
//     * Obtain the message properties file getName same as class getName
//     * @param messageClass 指定的MessageClass 
//     * @param locale 指定的locale
//     * @return Messages for the class
//     */
//    public static MessageBundle of(Class messageClass, Locale locale) {
//        
//        String messageName = messageClass.getName();
//        int pos;
//        if ((pos = messageName.indexOf('$')) > 0) {
//            messageName = messageName.substring(0, pos);
//        }
//        ResourceBundle bundle = ResourceBundle.getBundle(messageName, locale, messageClass.getClassLoader());
//        return new MessageBundleImpl(locale, bundle);
//    }
}
