package com.healtonresearch.examples.collections;

import java.util.*;

public class Queue<T> {

  private ArrayList<T> list = new ArrayList<T>();

  public void push(T obj) {
    list.add(obj);
  }

  public T pop() throws QueueException {
    if (size() == 0)
      throw new QueueException(
          "Tried to pop something from the queue, when it was empty");
    T result = list.get(0);
    list.remove(0);
    return result;
  }

  public boolean isEmpty() {
    return list.isEmpty();
  }

  public int size() {
    return list.size();
  }

  public void clear() {
    list.clear();
  }
}
