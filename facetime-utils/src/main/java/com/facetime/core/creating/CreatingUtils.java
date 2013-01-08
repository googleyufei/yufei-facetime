package com.facetime.core.creating;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.facetime.core.coercion.Coercer;
import com.facetime.core.collection.ArrayEx;
import com.facetime.core.utils.CE;
import com.facetime.core.utils.LE;


/**
 * 关于创建对象的一些帮助方法
 */
public abstract class CreatingUtils {

    /**
     * 根据参数类型数组获取一个对象的构建信息
     *
     * @param <T>      对象类型信息
     * @param type     对象类型
     * @param argTypes 构造参数类型数组
     * @return 构建信息对象
     */
    public static <T> CreatingContext<T> evalByArgTypes(Class<T> type, Class<?>... argTypes) {
        CreatingContext<T> re;
        if (argTypes.length == 0) {
            re = evalWithoutArgs(type);
        } else {
            re = evalWithArgTypes(true, type, argTypes, null);
        }
        return re;
    }

    /**
     * 根据参数类型数组获取一个对象的构建信息
     *
     * @param <T>  对象类型信息
     * @param type 对象类型
     * @param args 构造参数数组
     * @return 构建信息对象
     */
    public static <T> CreatingContext<T> eval(Class<T> type, Object... args) {
        CreatingContext<T> re;
        if (args.length == 0) {
            re = evalWithoutArgs(type);
        } else {
            re = evalWithArgs(type, args);
        }
        return re;
    }

    /**
     * 根据一个调用参数数组，获取一个对象的构建信息
     *
     * @param <T>  对象类型信息
     * @param type 对象类型
     * @param args 参考构建参数
     * @return 构建信息对象
     */
    private static <T> CreatingContext<T> evalWithArgs(Class<T> type, Object[] args) {
        // 准备变参数组
        Object dynaArg = LE.evalArgToRealArray(args);

        // 准备好参数类型
        Class<?>[] argTypes = LE.evalToTypes(args);

        CreatingContext<T> re = evalWithArgTypes(false, type, argTypes, dynaArg);

        if (null == re)
            return null;

        if (LE.MatchType.LACK == re.getMatchType()) {
            re.setArgs(ArrayEx.of(args).addValue(re.getLackArg()).toArray());
        } else {
            re.setArgs(args);
        }

        switch (re.getMatchType()) {
            case LACK:
                re.setArgs(ArrayEx.of(args).addValue(re.getLackArg()).toArray());
                break;
            case NEED_CAST:
                re.setArgs(LE.array2ObjectArray(args, re.getCastType()));
                break;
            default:
                re.setArgs(args);
        }

        return re;
    }

    /**
     * 根据一个调用参数类型数组，获取一个对象的构建信息
     *
     * @param <T>      对象类型信息
     * @param accurate 是否需要精确匹配
     * @param type     对象类型
     * @param argTypes 参考参数类型数组
     * @param dynaArg  参考参数类型信息是否是一个变参数组
     * @return 构建信息对象
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static <T> CreatingContext<T> evalWithArgTypes(boolean accurate,
                                                           Class<T> type,
                                                           Class<?>[] argTypes,
                                                           Object dynaArg) {
        // 准备好返回对象
        CreatingContext<T> re = new CreatingContext<T>();

        // 先看看有没对应的构造函数
        CE<T> ce = CE.of(type);
        for (Constructor<?> cc : type.getConstructors()) {
            Class<?>[] pts = cc.getParameterTypes();
            LE.MatchType mt = LE.matchParamTypes(pts, argTypes);
            re.setMatchType(mt);
            // 正好合适
            if (LE.MatchType.YES == mt) {
                return re.setCreating(new ConstructorCreating(cc));
            }
            // 差一个参数，说明这个构造函数有变参数组
            else if (LE.MatchType.LACK == mt) {
                re.setLackArg(LE.blankArrayArg(pts));
                return re.setCreating(new ConstructorCreating(cc));
            }
            // 看看整个输入的参数是不是变参
            else if (null != dynaArg && pts.length == 1 && pts[0] == dynaArg.getClass()) {
                return re.setCreating(new DynamicConstructorCreating(cc));
            }
        }

        // 看看有没有对应静态工厂函数
        Method[] sms = ce.getMethods(ce.getClassType(), Modifier.STATIC);
        for (Method m : sms) {
            Class<?>[] pts = m.getParameterTypes();
            LE.MatchType mt = LE.matchParamTypes(pts, argTypes);
            re.setMatchType(mt);
            if (LE.MatchType.YES == mt) {
                return re.setCreating(new MethodCreating<T>(m));
            } else if (LE.MatchType.LACK == mt) {
                re.setLackArg(LE.blankArrayArg(pts));
                return re.setCreating(new MethodCreating<T>(m));
            } else if (null != dynaArg && pts.length == 1) {
                if (pts[0] == dynaArg.getClass()) {
                    return re.setCreating(new DynamicMethodCreating<T>(m));
                }
            }
        }
        // 如果不是要精确查找的话
        if (!accurate) {
            // 找到一个长度合适的构造函数，准备转换
            try {
                for (Constructor<?> cc : type.getConstructors()) {
                    Class<?>[] pts = cc.getParameterTypes();
                    if (canBeCasted(argTypes, pts)) {
                        re.setMatchType(LE.MatchType.NEED_CAST);
                        re.setCastType(pts);
                        return re.setCreating(new ConstructorCastingCreating(cc));
                    }
                }
            } catch (RuntimeException e) {
            }
            // 有没有变参的静态构造方法
            try {
                for (Method m : sms) {
                    Class<?>[] pts = m.getParameterTypes();
                    if (canBeCasted(argTypes, pts)) {
                        re.setMatchType(LE.MatchType.NEED_CAST);
                        re.setCastType(pts);
                        return re.setCreating(new MethodCastingCreating<T>(m));
                    }
                }
            } catch (Exception e) {
            }
        }

        return null;
    }

    private static boolean canBeCasted(Class<?>[] argTypes, Class<?>[] pts) {
        if (pts.length != argTypes.length) return false;
        for (int i = 0; i < pts.length; i++) {
            if (!Coercer.get().isCoercable(argTypes[i], pts[i]))
                return false;
        }
        return true;
    }

    /**
     * 为一个给定类，寻找一个不需要参数的构造方法
     *
     * @param <T>  类
     * @param type 类实例
     * @return 构造信息
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static <T> CreatingContext<T> evalWithoutArgs(Class<T> type) {
        // 准备好返回对象
        CreatingContext<T> re = new CreatingContext<T>();
        CE<T> ce = CE.of(type);

        // 先看看有没有默认构造函数
        try {
            re.setCreating(new EmptyArgsConstructorCreating<T>(type.getConstructor()));
            return re.setArgs(new Object[0]);
        }
        // 如果没有默认构造函数 ...
        catch (Exception e) {
            // 看看有没有默认静态工厂函数
            Method[] stMethods = ce.getMethods(ce.getClassType(), Modifier.STATIC);
            for (Method m : stMethods) {
                if (m.getReturnType() == type && m.getParameterTypes().length == 0) {
                    return re.setCreating(new EmptyArgsMethodCreating<T>(m)).setArgs(new Object[0]);
                }
            }
            // 看看有没有带一个动态参数的构造函数
            for (Constructor<?> cons : type.getConstructors()) {
                Class<?>[] pts = cons.getParameterTypes();
                if (pts.length == 1 && pts[0].isArray()) {
                    Object[] args = new Object[1];
                    args[0] = LE.blankArrayArg(pts);
                    return re.setCreating(new ConstructorCreating(cons)).setArgs(args);
                }
            }
            // 看看有没有带一个动态参数的静态工厂函数
            for (Method m : stMethods) {
                Class<?>[] pts = m.getParameterTypes();
                if (m.getReturnType() == type
                        && m.getParameterTypes().length == 1
                        && pts[0].isArray()) {
                    Object[] args = new Object[1];
                    args[0] = LE.blankArrayArg(pts);
                    return re.setCreating(new MethodCreating<T>(m)).setArgs(args);
                }
            }
        }
        return null;
    }
}
