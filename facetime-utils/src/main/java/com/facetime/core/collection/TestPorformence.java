package com.facetime.core.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.facetime.core.utils.RandomGenerator;
import com.facetime.core.utils.StopWatch;

/**
 * Created by IntelliJ IDEA.
 * User: dzb
 * Date: 11-8-20
 * Time: 上午2:42
 * To change this template use File | Settings | File Templates.
 */
public class TestPorformence {

	public static ArrayEx fill(Map<String, String> map, int s) {
		ArrayEx array = new ArrayEx(s);
		for (int i = 0; i < s; i++) {
			String k = RandomGenerator.getRandomStr(6);
			String v = RandomGenerator.getRandomStr(6);
			array.add(k);
			map.put(k, v);
		}
		return array;
	}

	public static void run(Map<String, String> map, int num) {
		ArrayEx<String> array = fill(map, num);
		for (String k : array) {
			String v = map.get(k);
		}
	}

	public static void testMap(int num) {
		StopWatch sw = new StopWatch();
		Map<String, String> map1 = new HashMap<String, String>();
		sw.start("hash");
		run(map1, num);
		sw.stop();
		Map<String, String> map2 = new FastMap<String, String>();
		sw.start("fast");
		run(map1, num);
		sw.stop();
		System.out.println(sw.prettyPrint());

	}

	//一个个押进去在一个个取出来 然后再押回去
	public static void runStack(Stack<String> stack, int n) {
		for (int i = 0; i < n; i++) {
			String k = RandomGenerator.getRandomStr(6);
			stack.push(k);
		}
		for (int i = 0; i < n; i++) {
			stack.pop();
		}
		for (int i = 0; i < n; i++) {
			String k = RandomGenerator.getRandomStr(6);
			stack.push(k);
		}
	}

	public static void testStack(int num) {
		StopWatch sw = new StopWatch();
		//        Stack stack1 = new ArrayListStack();
		//        sw.start("list");
		//        runStack(stack1, num);
		//        sw.stop();
		Stack stack2 = new ArrayStack();
		sw.start("array");
		runStack(stack2, num);
		sw.stop();
		Stack stack3 = new LinkedStack();
		sw.start("linked");
		runStack(stack3, num);
		sw.stop();
		Stack stack4 = new ArrayEx();
		sw.start("array2");
		runStack(stack3, num);
		sw.stop();

		System.out.println(sw.prettyPrint());
	}

	public static void testList(int num) {

		ArrayEx<Integer> arrayEx = new ArrayEx<Integer>(num, true, Integer.class);
		Random r = new Random(1000);
		for (int i = 0; i < num; i++) {
			arrayEx.add(r.nextInt(num));
		}

		List<String> l1 = new ArrayList<String>(num);
		for (int i = 0; i < num; i++) {
			l1.add(RandomGenerator.getRandomStr(5));
		}

		StopWatch sw = new StopWatch();
		sw.start("array");
		for (int i = 0; i < num; i++) {
			l1.get(r.nextInt(num - 1));
		}
		sw.stop();

		List<String> l2 = new LinkedList<String>();
		for (int i = 0; i < num; i++) {
			l2.add(RandomGenerator.getRandomStr(5));
		}

		for (int i = 0; i < num; i++) {
			l1.get(arrayEx.get(i));
		}

		sw.start("linked");
		for (int i = 0; i < num; i++) {
			l2.get(arrayEx.get(i));

		}
		sw.stop();

		System.out.println("=========================");
		System.out.println(sw.prettyPrint());
	}

	public static void main(String[] args) {
		//        testStack(100);
		//        testMap(10000);

		testList(10000);
	}
}
