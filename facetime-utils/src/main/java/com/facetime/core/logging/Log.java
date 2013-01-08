package com.facetime.core.logging;

import java.io.Serializable;

/**
 * 便利于程序重构替换的Log类
 * @author dzb
 *
 */
public interface Log extends Serializable {

    /** The trace logging level. */
    public static final int TRACE_LEVEL = -1;

    /** The debug logging level. */
    public static final int DEBUG_LEVEL = 0;

    /** The info logging level. */
    public static final int INFO_LEVEL = 1;

    /** The warn logging level. */
    public static final int WARN_LEVEL = 2;

    /** The error logging level. */
    public static final int ERROR_LEVEL = 3;
    /**
     * 获取日志级别<>p</>
     * 本get-set方法仅对Console和JDK生效,,其他的看具体配置文件
     */
    public int getLevel();
    /**
     * 设置日志级别<>p</>
     * 本get-set方法仅对Console和JDK生效,,其他的看具体配置文件
     */
    public void setLevel(int level);

	/**
     * 调试信息输出
     * @param message 信息输出
     * @param args  格式化变参
     */
    public void debug(Object message, Object... args);
    public void debug(Throwable e, Object message, Object... args);
    /**
     * 跟踪信息输出
     * @param message
     * @param args
     */
    public void trace(Object message, Object... args);
    public void trace(Throwable e, Object message, Object... args);
    /**
     * 通用信息输出
     * @param message 信息输出
     * @param args  格式化变参
     */
    public void info(Object message, Object... args);
    public void info(Throwable e, Object message, Object... args);
    
    /**
     * 警告信息输出
     * @param message 信息输出
     * @param args  格式化变参
     */
    public void warn(Object message, Object... args);
    public void warn(Throwable e, Object message, Object... args);
    /**
     * 错误信息输出
     * @param message  信息输出
     * @param args  格式化变参
     */
    public void error(Object message, Object... args);
    public void error(Throwable e, Object message, Object... args);
    /**
     * 用于提高运行效率的日志写入判断 
     * @return
     */
    public boolean isDebugEnabled();
    public boolean isTraceEnabled();
    public boolean isWarnEnabled();
    public boolean isInfoEnabled();
    public boolean isErrorEnabled();
}
