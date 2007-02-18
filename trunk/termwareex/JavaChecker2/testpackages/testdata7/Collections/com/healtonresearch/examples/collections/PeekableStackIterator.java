package com.heatonresearch.examples.collections;

import java.util.*;

public class PeekableStackIterator<T> implements Iterator {

  private int version;
  private PeekableStack<T> source;
  private List<T> list;
  private int position;

  PeekableStackIterator(PeekableStack<T> source, List<T> list) {
    this.source = source;
    this.list = list;
    this.version = source.getVersion();
    this.position = 0;
  }

  public boolean hasNext() {
    if (version != source.getVersion())
      throw new ConcurrentModificationException();

    return (position < list.size());
  }

  public T next() {
    // check for concurrent modification
    if (version != source.getVersion())
      throw new ConcurrentModificationException();

    // if its empty then return null
    if (!hasNext())
      return null;

    // find the current position
    int last = list.size() - 1;
    last -= position;

    // if there are still elements left, return the
    // next one and update the current position.
    T result = null;

    if (last >= 0) {
      position++;
      result = list.get(last);
    }

    return result;
  }

  public void remove() {
  }

}
