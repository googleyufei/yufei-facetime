package com.facetime.core.utils;

/**
 * 简单的名字对结构<p> 绝对没法在精简了 ;)
 * @author dzb2k9 (dzb2k9@gmail.com)
 *
 * @param <TK>
 * @param <TV>
 */
public class KV<TK, TV> {

	private TK k;

	private TV v;

	public KV() {
	}

	public KV(TK k, TV v) {
		this.k = k;
		this.v = v;
	}

	public TK k() {
		return k;
	}

	public KV<TK, TV> k(TK k) {
		this.k = k;
		return this;
	}

	public String toString() {
		StringBand sb = new StringBand(3);
		sb.append(k).append(":").append(v);
		return sb.toString();
	}

	public TV v() {
		return v;
	}

	public KV<TK, TV> v(TV v) {
		this.v = v;
		return this;
	}

	/**
	 * 静态辅助构造方法
	 * @param <TK>
	 * @param <TV>
	 * @param k
	 * @param v
	 * @return
	 */
	public static <K, V> KV<K, V> of(K k, V v) {
		return new KV<K, V>(k, v);
	}
}
