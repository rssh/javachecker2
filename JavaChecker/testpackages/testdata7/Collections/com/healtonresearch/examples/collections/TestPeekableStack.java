package com.healtonresearch.examples.collections;

import java.util.*;

public class TestPeekableStack {

  public static void main(String args[]) {
    PeekableStack<Integer> stack = new PeekableStack<Integer>();
    stack.push(1);
    stack.push(2);
    stack.push(3);

    for (int i : stack) {
      System.out.println(i);
    }

    System.out.println("Pop 1:" + stack.pop());
    System.out.println("Pop 2:" + stack.pop());
    System.out.println("Pop 3:" + stack.pop());
  }
}
