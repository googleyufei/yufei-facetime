package com.facetime.core.creating;

import com.facetime.core.utils.LE;

/**
 * 构建上下文
 *
 * @param <T>
 */
public class CreatingContext<T> {

    private Creating<T> creating;

    private Object[] args;

    private LE.MatchType matchType;

    private Object lackArg;

    private Class<?>[] castType;

    public Creating<T> getCreating() {
        return creating;
    }

    public CreatingContext<T> setCreating(Creating<T> creating) {
        this.creating = creating;
        return this;
    }

    public Object[] getArgs() {
        return args;
    }

    public CreatingContext<T> setArgs(Object[] args) {
        this.args = args;
        return this;
    }

    public LE.MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(LE.MatchType matchType) {
        this.matchType = matchType;
    }

    public Object getLackArg() {
        return lackArg;
    }

    public void setLackArg(Object lackArg) {
        this.lackArg = lackArg;
    }

    public Class<?>[] getCastType() {
        return castType;
    }

    public void setCastType(Class<?>[] castType) {
        this.castType = castType;
    }

    public T create() {
        return creating.create(args);
    }

}
