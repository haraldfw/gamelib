package com.smokebox.lib.utils;

public class IntVector2 {

  public int x;
  public int y;

  public IntVector2(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public IntVector2() {
    this.x = 0;
    this.y = 0;
  }

  public IntVector2 add(IntVector2 toAdd) {
    this.x += toAdd.x;
    this.y += toAdd.y;
    return this;
  }

  public IntVector2 add(int x, int y) {
    this.x += x;
    this.y += y;
    return this;
  }

  public IntVector2 set(int x, int y) {
    this.x = x;
    this.y = y;
    return this;
  }

  public boolean isLike(int x, int y) {
    return this.x == x && this.y == y;
  }

  public boolean isZero() {
    return x == 0 && y == 0;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof IntVector2)) {
      return false;
    }

    IntVector2 v = (IntVector2) obj;
    return x == v.x && y == v.y;
  }
}
