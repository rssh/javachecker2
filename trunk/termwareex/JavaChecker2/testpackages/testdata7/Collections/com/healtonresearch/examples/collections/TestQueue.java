package com.heatonresearch.examples.collections;

public class TestQueue {

  public static void main(String args[]) {
    Queue<Integer> queue = new Queue<Integer>();
    queue.push(1);
    queue.push(2);
    queue.push(3);
    try {
      System.out.println("Pop 1:" + queue.pop());
      System.out.println("Pop 2:" + queue.pop());
      System.out.println("Pop 3:" + queue.pop());
    } catch (QueueException e) {
      e.printStackTrace();
    }
  }
}
