package com.facetime.core.utils;

import java.io.Serializable;

/**
 * 查询数量限制类
 * 
 * @since 2.5 升级为内部计算 limit count 和 offset
 * 
 * @author dzb2k9
 */
@SuppressWarnings("serial")
public final class Limits2 implements Serializable {

	private static final Limits2 NONE = new Limits2(0, 0);
	//当前限制数量
	private int limit = 0;
	//记录总数
	private long total = -1;
	//当前页
	private int current;
	//总分页
	private int count;

	public Limits2(int limit) {
		this.current = 1;
		setLimit(limit);
		setTotal(-1);
	}

	/** 
	 * @param total 记录总数
	 * @param limit 每次获取的数量
	 */
	public Limits2(int limit, long total) {
		this.current = 1;
		setLimit(limit);
		setTotal(total);
	}

	/**
	 * 重设limit的count总数
	 * @return
	 */
	public Limits2 reset() {
		count = -1;
		return this;
	}

	/**
	 * 获取起始点
	 * @return
	 */
	public int getOffset() {
		return limit * (current - 1);
	}

	/**
	 * 获取限制索引总数
	 * @return
	 */
	public int getLimitsCount() {
		if (count < 0)
			count = (int) Math.ceil((double) total / limit);
		return count;
	}

	/**
	 * 获取当前的限制索引
	 * @return
	 */
	public int getCurrent() {
		return current;
	}

	/**
	 * 设置当前的限制索引
	 * @param cur
	 * @return
	 */
	public Limits2 setCurrent(int cur) {
		this.current = cur;
		return this;
	}

	/**
	 * 获取限制数
	 * @return
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * 设置限制数
	 * @param limit
	 * @return
	 */
	public Limits2 setLimit(int limit) {
		if (limit > 0) {
			this.limit = limit;
			reset();
		}
		return this;
	}

	/**
	 * 是否第一页
	 * @return
	 */
	public boolean isFirst() {
		return current == 1;
	}

	/**
	 * 是否最后一页
	 * @return
	 */
	public boolean isLast() {
		//此处的判断决定是否可以无限制翻页
		//		if (count == 0)
		//			return true;
		return current == count;
	}

	/**
	 * 是否没有限制
	 * @return
	 */
	public boolean isNone() {
		return (limit <= 0);
	}

	/**
	 * 获取总数量
	 * @return
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * 设置总数量
	 * @param total
	 * @return
	 */
	public Limits2 setTotal(long total) {
		this.total = total > 0 ? total : 0;
		this.count = (int) Math.ceil((double) total / limit);
		return this;
	}

	/**
	 * 移至第一页
	 * @return
	 */
	public Limits2 first() {
		current = 1;
		return this;
	}

	/**
	 * 移动至最后一页
	 * @return
	 */
	public Limits2 last() {
		current = count;
		return this;
	}

	/**
	 * 移动到下一页
	 * @return
	 */
	public Limits2 next() {
		current++;
		return this;
	}

	/**
	 * 移动到上一页
	 * @return
	 */
	public Limits2 prev() {
		if (current > 1) {
			current--;
		}
		return this;
	}

	/**
	 * 移动到指定页
	 * @param pn
	 * @return
	 */
	public Limits2 move(int pn) {
		if (pn > 0) {
			if (count > 0 && pn > count)
				this.current = count;
			else
				this.current = pn;
		}
		return this;
	}

	/**
	 * Convenience method to get limits object
	 * @return 无限制的Limits对象
	 */
	public static final Limits2 none() {
		return NONE;
	}

	/**
	 * 便利的静态构造
	 * @param limit
	 * 			限制数
	 * @param total
	 * 			总数
	 * @return
	 */
	public static final Limits2 of(int limit, long total) {
		return new Limits2(limit, total);
	}

	/**
	 * 遍历的静态构造
	 * @param limit
	 * 			限制数
	 * @return
	 */
	public static final Limits2 of(int limit) {
		return new Limits2(limit);
	}

	@Override
	public boolean equals(Object limit) {
		if (limit == null || limit.getClass() != Limits2.class)
			return false;
		Limits2 o = (Limits2) limit;
		return (this.limit == o.limit) && (this.total == o.total);
	}

	@Override
	public String toString() {
		return String.format("limit: %d, offset: %d, total: %d, page: %d/%d", limit, getOffset(), total, current,
				getLimitsCount());
	}

}
