package com.wilhelmsen.gamelib.utils;

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
    x += v.x;
    y += v.y;
  }

  public void print() {
    System.out.println("-- Rectangle --");
    System.out.println("Position:   " + x + ", " + y);
    System.out.println("Dimensions: " + width + ", " + height);
  }

  public Rectangle getScaled(float f) {
    return new Rectangle(x * f, y * f, width * f, height * f);
  }

  public Rectangle round() {
    Math.round(x);
    Math.round(y);
    Math.round(width);
    Math.round(height);

    return this;
  }

  public Rectangle floor() {
    Math.floor(x);
    Math.floor(y);
    Math.floor(width);
    Math.floor(height);

    return this;
  }
}
