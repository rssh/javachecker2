package com.heatonresearch.examples.collections;

public class Example<ONE, TWO, THREE> {

  private ONE one;
  private TWO two;
  private THREE three;

  public ONE getOne() {
    return one;
  }

  public void setOne(ONE one) {
    this.one = one;
  }

  public THREE getThree() {
    return three;
  }

  public void setThree(THREE three) {
    this.three = three;
  }

  public TWO getTwo() {
    return two;
  }

  public void setTwo(TWO two) {
    this.two = two;
  }

  public static void main(String args[]) {
    Example<Double, Integer, String> example = new Example<Double, Integer, String>();

    example.setOne(1.5);
    example.setTwo(2);
    example.setThree("Three");
  }

}
