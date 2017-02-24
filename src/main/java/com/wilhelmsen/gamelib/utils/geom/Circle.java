package com.wilhelmsen.gamelib.utils.geom;

import com.wilhelmsen.gamelib.utils.Vector2;

public class Circle {

  public float x;
  public float y;
  public float radius;

  public Circle(float x, float y, float radius) {
    this.x = x;
    this.y = y;
    this.radius = radius;
  }

  public Circle() {
    this.x = 0;
    this.y = 0;
    this.radius = 0;
  }

  public void setPos(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public void setPos(Vector2 pos) {
    this.x = pos.x;
    this.y = pos.y;
  }
}
