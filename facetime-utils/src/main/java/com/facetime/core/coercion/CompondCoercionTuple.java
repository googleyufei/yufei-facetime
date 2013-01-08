package com.facetime.core.coercion;


/**
 * 可以结合两个coercion元组创建一个媒介 coercion元组<>p</>
 * 就是说如果存在 A->B B->C 那么 自动存在 A->C
 * TODO: 因为转换框架调整 暂未没实现 有需求时候再说
 *
 * @param <S> The source (input) type
 * @param <I> The intermediate type
 * @param <T> The target (output) type
 * @author dzb2k9
 */
public class CompondCoercionTuple<S, I, T> extends AbstractCoercion<S, T> {

    private final CoercionTuple<S, I> op1;
    private final CoercionTuple<I, T> op2;

    public CompondCoercionTuple(CoercionTuple<S, I> op1, CoercionTuple<I, T> op2) {
        this.op1 = op1;
        this.op2 = op2;
    }

    public Class<?> getFromType() {
        return op1.getFromType();
    }

    public Class<?> getTargetType() {
        return op2.getTargetType();
    }

    public T coerce(S input) {
        // Run the input through the first operation (S --> I), then run the result of that through
        // the second operation (I --> T).
        I intermediate = op1.coerce(input);
        return op2.coerce(intermediate);
    }

    @Override
    public String toString() {
        return String.format("%s, %s", op1, op2);
    }
}