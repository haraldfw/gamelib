package com.wilhelmsen.gamelib.utils.geom;

import com.wilhelmsen.gamelib.utils.Vector2;


public class Rectangle {

  public float x;
  public float y;
  public float width;
  public float height;
  public float boundingRadius;

  public Rectangle(float x, float y, float width, float height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;

    boundingRadius = (float) Math.sqrt((width / 2) * (width / 2) + (height / 2) * (height / 2));
  }

  public Rectangle() {
    this.x = 0;
    this.y = 0;
    this.width = 0;
    this.height = 0;

    boundingRadius = 0;
  }

  public float area() {
    return width * height;
  }

  public Vector2 botLeftPos() {
    return new Vector2(x, y);
  }

  public Vector2 middlePos() {
    return new Vector2(x + width / 2, y + height / 2);
  }

  public void addToPosition(Vector2 v) {
    this.x += v.x;
    this.y += v.y;
  }

  public void addToPosition(float x, float y) {
    this.x += x;
    this.y += y;
  }

  public Rectangle getScaled(float f) {
    return new Rectangle(x * f, y * f, width * f, height * f);
  }

  public Rectangle round() {
    this.x = (float) Math.round(x);
    this.y = (float) Math.round(y);
    this.width = (float) Math.round(width);
    this.height = (float) Math.round(height);

    return this;
  }

  public Rectangle floor() {
    this.x = (float) Math.floor(x);
    this.y = (float) Math.floor(y);
    this.width = (float) Math.floor(width);
    this.height = (float) Math.floor(height);

    return this;
  }

  public float x2() {
    return x + width;
  }

  public float y2() {
    return y + height;
  }

  public Vector2 getMidPos() {
    return new Vector2(x + width / 2, y + height / 2);
  }

  public Rectangle set(float x, float y, float width, float height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    return this;
  }

  public Rectangle setDimensions(float width, float height) {
    this.width = width;
    this.height = height;
    return this;
  }

  public Rectangle setPos(Vector2 pos) {
    this.x = pos.x;
    this.y = pos.y;
    return this;
  }

  public Rectangle setPos(float x, float y) {
    this.x = x;
    this.y = y;
    return this;
  }

  public Rectangle ensureAxisAlignment() {
    if (width < 0) {
      x += width;
      width = -width;
    }
    if (height < 0) {
      y += height;
      height = -height;
    }
    return this;
  }

  @Override
  public String toString() {
    return "Rectangle [x:" + x + ", y:" + y + ", w:" + width + ", h:" + height + "]";
  }
}
