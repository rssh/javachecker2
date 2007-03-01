package com.healtonresearch.examples.collections;

import java.util.Iterator;

/**
 * @author jeff
 *
 */
public class TestPeekableStackException {
	/**
	 * @param args
	 */
	public static void main(String args[]) {
		PeekableStack<Integer> stack = new PeekableStack<Integer>();
		stack.push(1);
		stack.push(2);
		stack.push(3);

		Iterator<Integer> itr = stack.iterator();
		while (itr.hasNext()) {
			int i = itr.next();
			System.out.println("Iterating: " + i);
			stack.push(1);// this will cause hasNext to throw an exception
		}

		// This will never be reached. An exception will be thrown first
		System.out.println("Pop 1:" + stack.pop());
		System.out.println("Pop 2:" + stack.pop());
		System.out.println("Pop 3:" + stack.pop());

	}
}
