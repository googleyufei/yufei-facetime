package com.facetime.core.order;

/**
 * 排序索引接口<p>
 * 格式为数字加中划线构成，String的compare会很好的比较这类格式
 * 
 * @author dzb2k9 (dzb2k9@gmail.com)
 *
 */
public interface Orderable {
	/**
	 * 排序约束表达式<br>
	 * before:AAA|after:bbb<br>
	 * <li> before:(label)
	 * <li> after:(label)
	 * AAA BBB 为目标的标牌(getId)<p></>
     * 数字表达式<br>
     * 支持float 1.1 < 1.11
	 * @return 约束表达式
	 */
	String getOrder();
}
