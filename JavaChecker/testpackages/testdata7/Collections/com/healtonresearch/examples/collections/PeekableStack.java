package com.healtonresearch.examples.collections;

import java.util.*;

public class PeekableStack<T> implements Iterable<T> {

  private int version;
  private ArrayList<T> list = new ArrayList<T>();

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public Iterator<T> iterator() {
    PeekableStackIterator peekableStackIterator = new PeekableStackIterator(
        this, list);
    return peekableStackIterator;
  }

  public void push(T obj) {
    version++;
    list.add(obj);
  }

  public T pop() {
    // find the last element
    int last = list.size() - 1;

    // is the stack empty?
    if (last < 0)
      return null;

    // return the last element and remove it
    T result = list.get(last);
    list.remove(last);
    return result;
  }

  public int size() {
    return list.size();
  }

}
