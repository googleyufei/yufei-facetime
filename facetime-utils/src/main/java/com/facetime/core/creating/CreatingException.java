package com.facetime.core.creating;


import com.facetime.core.utils.LE;

@SuppressWarnings("serial")
public class CreatingException extends RuntimeException {

    public CreatingException(Class<?> type, Object[] args) {
        this(new RuntimeException("Don't know how to create it!"), type, args);
    }

    public CreatingException(Class<?> type, Class<?>[] argTypes) {
        this(new RuntimeException("Don't know how to create it!"), type, argTypes);
    }

    public CreatingException(Throwable e, Class<?> type, Object[] args) {
        super(makeMessage(e, type, args), e);
    }

    public CreatingException(Throwable e, Class<?> type, Class<?>[] argTypes) {
        super(makeMessage(e, type, argTypes), e);
    }

    private static String makeMessage(Throwable e, Class<?> type, Class<?>[] argTypes) {
        StringBuilder sb = new StringBuilder();
        String name = null == type ? "unknown" : type.getName();
        sb.append("Fail to create '").append(name).append('\'');
        if (null != argTypes && argTypes.length > 0) {
            sb.append("\n by args: [");
            for (Object argType : argTypes)
                sb.append("\n  @(").append(argType).append(')');
            sb.append("]");
        }
        if (null != e) {
            sb.append(" becasue:\n").append(LE.unwrapThrow(e).getMessage());
        }
        return sb.toString();
    }

    private static String makeMessage(Throwable e, Class<?> type, Object[] args) {
        StringBuilder sb = new StringBuilder();
        String name = null == type ? "unknown" : type.getName();
        sb.append("Fail to create '").append(name).append('\'');
        if (null != args && args.length > 0) {
            sb.append("\n by args: [");
            for (Object arg : args)
                sb.append("\n  @(").append(arg).append(')');
            sb.append("]");
        }
        if (null != e) {
            sb.append(" becasue:\n").append(LE.unwrapThrow(e).getMessage());
        }
        return sb.toString();
    }
}
