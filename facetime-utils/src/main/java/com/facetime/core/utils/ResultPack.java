package com.facetime.core.utils;

import java.io.Serializable;

/**
 * 用于结果返回的基本结构<P> 
 * <L>status 指定是成功 失败 还是Void(无返回)<BR>
 * <L>value 实际返回对象<BR>
 * <L>comment 对返回的注解
 * 使用{@link ResultPack}静态方法方便构建
 * <P>
 * 由于很多返回单指不够，尤其是需要带用户提示的(comment)，用“异常”形式无法用于远程过程调用，所以才有了Result的存在。<P>
 *
 *
 * @NOTE 在STREET中，Service-Point的方法呼叫的返回应使用Result。
 *
 * @author dzb2k9 dzb2k9@gmail.com
 *
 */
@SuppressWarnings("serial")
public final class ResultPack implements Serializable, Cloneable {

	public static final int VOID = 0;
	public static final int FAILED = -1;
	public static final int SUCCESS = 1;

	private int _status = 0; // enum _value in -1 0 1
	private String _comment = null;
	private Object _value = null;

	public ResultPack() {
	}

	public <T> ResultPack(int status, T value, String comment) {
		_status = status;
		_value = value;
		_comment = comment;
	}

	@Override
	public ResultPack clone() {
		return new ResultPack(_status, _value, comment());
	}

	/**
	 * Get packed _message
	 * 
	 * @return
	 */
	public String comment() {
		return _comment;
	}

	/**
	 * 设置说明
	 * @param comment 说明信息
	 * @return 自身
	 */
	public ResultPack comment(String comment, Object... args) {
		_comment = String.format(comment, args);
		return this;
	}

	public boolean isFailed() {
		return _status == ResultPack.FAILED;
	}

	/**
	 * 是否成功
	 * @return
	 */
	public boolean isSucceed() {
		return _status == ResultPack.SUCCESS;
	}

	/**
	 * 是否无结果
	 * @return
	 */
	public boolean isVoid() {
		return _status == ResultPack.VOID;
	}

	/**
	 * 重置Result为VOID状态。<P>废物利用，环保。
	 * @return
	 */
	public ResultPack reset() {
		_status = VOID;
		_value = null;
		_comment = null;
		return this;
	}

	/**
	 * 有时在一个串行化过程中，我们可能需要保留comment作为附加输出信息，这时候可以用这个方法
	 * @param keepComment 是否同时清除comment。
	 * @return
	 */
	public ResultPack reset(boolean keepComment) {
		_status = VOID;
		_value = null;
		if (!keepComment)
			_comment = null;
		return this;
	}

	/**
	 * Get pack _state _value
	 * 
	 * @return
	 */
	public int status() {
		return _status;
	}

	public ResultPack status(int status) {
		_status = status;
		return this;
	}

	@Override
	public String toString() {
		return new StringBuilder("RESULT:[").append(_status).append("] comment:[").append(_comment).append("] value:[")
				.append(String.valueOf(_value)).append("]").toString();
	}

	/**
	 * Get packed _value
	 * @return
	 */
	public <T> T value() {
		return (T) _value;
	}

	public <T> ResultPack value(T value) {
		_value = value;
		return this;
	}

	/**
	 * 得到失败的ResultPack的简易辅助方法的
	 * @return ResultPack
	 */
	public static ResultPack failed() {
		ResultPack result = new ResultPack();
		return result.status(ResultPack.FAILED);
	}

	/**
	 * 得到失败的ResultPack的简易辅助方法的
	 * @param  value
	 * @return ResultPack
	 */
	public static <T> ResultPack failed(T value) {
		ResultPack result = new ResultPack();
		return result.status(ResultPack.FAILED).value(value);
	}

	/**
	 * 返回无值结果的简易辅助方法的
	 * @return ResultPack
	 */
	public static ResultPack none() {
		ResultPack result = new ResultPack();
		return result.status(ResultPack.VOID);
	}

	/**
	 * 返回无值结果的简易辅助方法的
	 * @param  value
	 * @return ResultPack
	 */
	public static <T> ResultPack none(T value) {
		ResultPack result = new ResultPack();
		return result.status(ResultPack.VOID).value(value);
	}

	/**
	 * 返回成功的Pack的简易辅助方法的
	 * @return ResultPack
	 */
	public static ResultPack succeed() {
		ResultPack result = new ResultPack();
		return result.status(ResultPack.SUCCESS);
	}

	/**
	 * 返回成功的Pack的简易辅助方法的
	 * @param value
	 * @return ResultPack
	 */
	public static <T> ResultPack succeed(T value) {
		ResultPack result = new ResultPack();
		return result.status(ResultPack.SUCCESS).value(value);
	}

}
