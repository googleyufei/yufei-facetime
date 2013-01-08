package com.facetime.core.i18n.impl;

import java.util.Locale;
import java.util.Map;

import com.facetime.core.collection.CaseInsensitiveMap;
import com.facetime.core.i18n.MessageBundle;
import com.facetime.core.lock.RWLockBarrier;
import com.facetime.core.utils.Invokable;

/**
 * MessageBundle的基础抽象实现
 * <>p</>
 * Abstract implementation of {@link MessageBundle} that doesn't know where values come from (that information is supplied in
 * a subclass, via the {@link #value(String)} method).
 */
public abstract class AbstractMessageBundle implements MessageBundle {

	private final RWLockBarrier barrier = new RWLockBarrier();
	/**
	 * String key to MF creating.
	 */
	private final Map<String, MessageFormatter> cache = new CaseInsensitiveMap<MessageFormatter>();

	private final Locale locale;

	protected AbstractMessageBundle(Locale locale) {
		this.locale = locale;
	}

	/**
	 * Invoked to provide the symbol for a particular key. This may be invoked multiple times even for the same key. The
	 * implementation should <em>ignore the case of the key</em>.
	 *
	 * @param key the key to obtain a symbol for (case insensitive)
	 * @return the symbol for the key, or null if this creating can not provide the symbol
	 */
	protected abstract String value(String key);

	public boolean contains(String key) {
		return value(key) != null;
	}

	public String get(String key) {
		if (contains(key))
			return value(key);
		return String.format("[[missing key: %s]]", key);
	}

	public MessageFormatter getFormatter(final String key) {

		MessageFormatter result = barrier.read(new Invokable<MessageFormatter>() {
			public MessageFormatter invoke() {
				return cache.get(key);
			}
		});

		if (result != null)
			return result;

		final MessageFormatter fmter = buildFormatter(key);

		barrier.write(new Runnable() {
			public void run() {
				cache.put(key, fmter);
			}
		});

		return fmter;
	}

	private MessageFormatter buildFormatter(String key) {
		String format = get(key);
		return new MessageFormatter(format, locale);
	}

	public String format(String key, Object... args) {
		return getFormatter(key).format(args);
	}

}
