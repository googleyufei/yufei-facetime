/*
 * Copyright (C) 2010 SUNRico Inc.
 * ------------------------------------------------------------------------------
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Visit  http://www.streets.com for more infomation.
 *
 * ----------------------------------------------------------------------------------
 */

package com.facetime.core.collection;

import com.facetime.core.utils.StopWatch;
import com.facetime.core.utils.StringBand;

/**
 * Created by IntelliJ IDEA.
 * User: dzb
 * Date: 10-12-13
 * Time: 下午10:06
 * To change this template use File | Settings | File Templates.
 */
public class PerformTest {

	public static void testStack() {
		StopWatch sw = new StopWatch();
		sw.start("stack");
		ArrayStack stack = new ArrayStack();
		for (int i = 0; i < 100000 * 10; i++) {
			Integer v = new Integer(i);
			stack.push(v);
		}
		while (!stack.isEmpty()) {
			stack.pop();
		}
		sw.stop();

		sw.start("stack2");
		LinkedStack stack2 = new LinkedStack();
		for (int i = 0; i < 100000 * 10; i++) {
			Integer v = new Integer(i);
			stack2.push(v);
		}
		while (!stack2.isEmpty()) {
			stack2.pop();
		}
		sw.stop();
		System.out.println(sw.prettyPrint());

	}

	public static void testStringBand() {
		StopWatch sw = new StopWatch();
		sw.start("stringbuffer");
		StringBuffer sb2 = new StringBuffer();
		for (int i = 0; i < 200000; i++) {
			sb2.append("AAAAAAA ").append(i);
			sb2.append("bbb");
			sb2.append("bbb cccc");
		}
		sw.stop();
		sw.start("stringbuilder");
		StringBuilder sb3 = new StringBuilder();
		for (int i = 0; i < 200000; i++) {
			sb3.append("AAAAAAA ").append(i);
			sb3.append("bbb");
			sb3.append("bbb cccc");
		}
		sw.stop();
		sw.start("stringband");
		StringBand sb = new StringBand();
		for (int i = 0; i < 200000; i++) {
			sb.append("AAAAAAA ").append(i);
			sb.append("bbb");
			sb.append("bbb cccc");
		}
		sw.stop();
		System.out.println(sw.prettyPrint());
	}

	public static void main(String[] args) {

		testStringBand();
		testStack();
	}
}
