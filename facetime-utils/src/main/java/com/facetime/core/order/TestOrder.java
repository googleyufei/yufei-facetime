package com.facetime.core.order;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: dzb
 * Date: 11-8-26
 * Time: 下午9:53
 * To change this template use File | Settings | File Templates.
 */
public class TestOrder {

    public static void testIndex() {

        OrderNode o1 = OrderNode.of("o1", Integer.valueOf(3), "2");
        OrderNode o3 = OrderNode.of("o3", Integer.valueOf(2), "1.3.11");
        OrderNode o2 = OrderNode.of("o2", Integer.valueOf(1), "1");
        Orderer orderer = new Orderer();
        orderer.add(o1);
        orderer.add(o3);
        orderer.add(o2);

        List<Integer> list = orderer.getOrdered();
        for (Integer o : list) {
            System.out.println(o);
        }
    }

    public static void testExpr() {
        OrderNode o0 = OrderNode.of("o4", Integer.valueOf(3), "before:o3|after:o2");
        OrderNode o1 = OrderNode.of("o3", Integer.valueOf(4), "after:o4");
        OrderNode o3 = OrderNode.of("o2", Integer.valueOf(2), "after:o1");
        OrderNode o2 = OrderNode.of("o1", Integer.valueOf(1), "before:*");
        Orderer orderer = new Orderer();
        orderer.add(o0);
        orderer.add(o1);
        orderer.add(o2);
        orderer.add(o3);
        List<Integer> list = orderer.getOrdered();
        for (Integer o : list) {
            System.out.println(o);
        }
    }


    public static void main(String[] args) {
        System.out.println("--- Expression ---------------------");
       testExpr();
        System.out.println("--- FloatIndex ---------------------");
       testIndex();
    }
}
